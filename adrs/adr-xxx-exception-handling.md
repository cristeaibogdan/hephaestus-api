## Exception handling

## Status
Accepted: Decision approved and in effect. Please don't hesitate to challenge it.

## Context
We have no established way of throwing and handling exceptions.

Pain points:
- Multiple `Exception` classes that differ only in name.
- Checked exception being re-thrown as unchecked exception without the original exception (exception decapitation).
- Unclear exception messages that do not help developers debug.
- No support for multi-language error messages.
- No structured error response format.

## Decision
Use a single `CustomException` class across the application, paired with an `ErrorCode` enum for status/translation lookup, 
localized messages, a `GlobalExceptionHandler`, and structured validation errors — all returned as `ProblemDetail`.

### 1. CustomException & ErrorCode
One exception type instead of multiple near-identical Exception subclasses — every throw site is forced through 
an ErrorCode, so nothing escapes without a status/message mapping.
```java
public enum ErrorCode {
    GENERAL (HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_DATE (HttpStatus.BAD_REQUEST),
    SERIAL_NUMBER_NOT_FOUND (HttpStatus.NOT_FOUND),
	REPORT_GENERATION_FAIL (HttpStatus.INTERNAL_SERVER_ERROR);

    public final HttpStatus statusCode;

    ErrorCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
```
```java
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String[] parameters;

    public CustomException(ErrorCode errorCode, String... parameters) {
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public CustomException(String message, ErrorCode errorCode, String... parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public CustomException(Throwable cause, ErrorCode errorCode, String... parameters) {
        super(cause);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public CustomException(String message, Throwable cause, ErrorCode errorCode, String... parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }
}
```

### 2. Localized Messages
Messages live in properties files, not hardcoded in Java, so translation doesn't require a code change or redeploy.
Create messages.properties (and messages_ro, ... ) for languages under `/resources`
> The locale after messages_<locale>.properties has to be lowercase.
> 
> A test can be written for checking missing locales, see `ErrorCodeMessagesTest`.
```json lines
GENERAL = Internal server error
INVALID_DATE = Invalid date. Format must be YYYY/MM/DD
SERIAL_NUMBER_NOT_FOUND = No product with serial number {0} found
REPORT_GENERATION_FAIL = Could not generate report. Please try again later
```

### 3. GlobalExceptionHandler
Centralizing in one @RestControllerAdvice guarantees every error — expected or not — comes back as the same 
ProblemDetail shape. NoResourceFoundException is explicitly rethrown because it's 
framework noise (missing static resource), not an application error.
```java
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler {
	private final MessageSource messageSource;
	
	@ExceptionHandler(Exception.class)
	ProblemDetail onAnyException(Exception e, HttpServletRequest request) throws Exception {
		if (e instanceof NoResourceFoundException) {
			throw e; // if you don't want to handle certain exceptions (security, ...), re-throw
		}

		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				"Internal Translation Error",
				request.getLocale()
		);

		log.error("Unexpected {}: {}", ErrorCode.GENERAL, userMessage, e);
		ProblemDetail problemDetail = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
		problemDetail.setDetail(userMessage);
		return problemDetail;
	}

	@ExceptionHandler(CustomException.class)
	ProblemDetail onCustomException(CustomException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				e.getErrorCode().name(),
				e.getParameters(),
				"Internal Translation Error",
				request.getLocale()
		);
		log.error("CustomException {}: {}", e.getErrorCode(), userMessage, e);

		ProblemDetail problemDetail = ProblemDetail.forStatus(e.getErrorCode().statusCode);
		problemDetail.setDetail(userMessage);
		return problemDetail;
	}
}
```

### 4. Structured Validation Errors
Validation failures don't map to a single ErrorCode — they're often multiple fields at once — so they get their 
own ValidationError list instead of being forced into CustomException.

```java
record ValidationError(String detail, String jsonPointer) {}
```

#### 4.1 DTO validation (BindException)
```java
public record SearchWashingMachineRequest(
		@Min(value = 0, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageIndex,
		@Min(value = 1, message = "{NUMERIC_MINIMUM_VALUE}")
		int pageSize,

		@Pattern(regexp = "^(asc|desc)?$", message = "Sort direction must be 'asc', 'desc', or empty")
		String sortDirection
) {}

public Page<SearchWashingMachineResponse> search(@Valid @RequestBody SearchWashingMachineRequest request) { ... }
```

```java
@ExceptionHandler(BindException.class)
ProblemDetail onValidationException(BindException e) {
    // Field errors: violations tied to a single property (e.g. @NotBlank on a field)
    List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
            .map(this::toValidationError)
            .toList();

    // Global errors: violations tied to the object as a whole (e.g. class-level
    // cross-field validators like @PasswordsMatch), no single field to blame
    List<ValidationError> globalErrors = e.getBindingResult().getGlobalErrors().stream()
            .map(this::toValidationError)
            .toList();

    List<ValidationError> allErrors = List.copyOf(
            Stream.concat(
                    errors.stream(),
                    globalErrors.stream()
            ).toList()
    );

    log.error("Object validation failed: {}", allErrors, e);

    ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
    problemDetail.setTitle("Your request is not valid.");
    problemDetail.setProperty("errors", allErrors);
    return problemDetail;
}
```

#### 4.2 Parameter validation (HandlerMethodValidationException)
```java
@PostMapping("/many")
public Map<String, GetWashingMachineFullResponse> loadMany(
        @RequestBody
        @NotEmpty(message = "{LIST_NOT_EMPTY}")
        @Size(max = 10, message = "{LIST_MAX_SIZE}")
        Set<@NotBlank(message = "{FIELD_NOT_BLANK}") String> serialNumbers
) { ... }
```

```java
@ExceptionHandler(HandlerMethodValidationException.class)
ProblemDetail onMethodParameterValidationException(HandlerMethodValidationException e) {
    List<ValidationError> validationErrors = e.getAllValidationResults().stream()
            .flatMap(result -> result.getResolvableErrors().stream()
                    .map(error -> new ValidationError(
                                    error.getDefaultMessage(),
                                    toJsonPointer(result.getMethodParameter().getParameterName())
                            )
                    )
            )
            .toList();

    log.error("Method parameter validation failed: {}", validationErrors, e);

    ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
    problemDetail.setTitle("Your request is not valid.");
    problemDetail.setProperty("errors", validationErrors);
    return problemDetail;
}
```

### 5. Usage
```java
// Example 1: known lookup failure, surface the missing value to the user
if (!repository.existsBySerialNumber(serialNumber)) {
    throw new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber);
}

// Example 2: business-rule violation, no underlying exception to preserve
if (result == NONE) {
    throw new CustomException("Invalid recommendation issued " + NONE, ErrorCode.GENERAL);
}

// Example 3: rethrow a checked exception, its own message is enough
try {
	// some fancy code
} catch (JRException e) {
    throw new CustomException(e, ErrorCode.REPORT_GENERATION_FAIL);
}

// Example 4: rethrow a checked exception, add debug context the original lacks
try {
    return LocalDate.parse(dateString);
} catch (DateTimeParseException e) {
    throw new CustomException("Invalid date provided", e, ErrorCode.INVALID_DATE);
}
```

## Consequences
**Positives:**
- Uniform error response shape (ProblemDetail) across expected and unexpected errors, so the frontend parses one contract.
- Multi-language error messages without code changes (properties files).
- Structured, field-level validation errors instead of a generic message.
- No more exception decapitation — checked exceptions are wrapped with cause preserved.

**Negatives:**
- Adding an error case means keeping the `ErrorCode` enum and every
  `messages_*.properties` file in sync — `ErrorCodeMessagesTest` fails the
  build if a locale file is missing a key, but won't catch a key that's
  present but unused/orphaned.
- CustomException has four overloaded constructors — picking the wrong one isn't a compile error,
  so this relies on code review (Compliance) rather than the type system.
- Enum name doubles as the i18n key (ErrorCode.name()), so renaming an ErrorCode silently breaks translation
  lookup unless the test catches it.

## Compliance
- Enforcing is done via Code Review

## References
- [Victor Rentea - The Definitive Guide to Working with Exceptions in Java](https://www.youtube.com/watch?v=LRwCE7GreSM)
- [Exception Handling Guide in Java](https://victorrentea.ro/blog/exception-handling-guide-in-java/)
- [Presenting Exceptions to Users](https://victorrentea.ro/blog/presenting-exceptions-to-users/)
- [Clean Architecture Repository](https://github.com/victorrentea/clean-architecture/blob/master/clean-application/src/main/java/victor/training/clean/domain/CleanException.java#L3)