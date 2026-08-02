TODO: 
- Simplify this ADR - too many rules, and exceptions and decision taking.
- Refers to use-case and endpoint, we should pick one and stick to it.


# Use Case Naming Convention

## Status
Accepted: Decision approved and in effect. Please don't hesitate to challenge it.

## Context
We are adopting a vertical-slicing / use-case architecture, where each use case *is* an endpoint — one class, 
one route, no separate controller layer. Each use case owns its own package-private Request/Response DTOs. 
We have no established naming convention for use case classes under this model.

Pain points:
- Shared Requests / Responses across endpoints make API contracts harder to evolve independently.
- Time wasted in code reviews debating naming approaches.
- DTO names without a clear indication of whether they are requests or responses.

We need a simple, predictable naming convention that can scale as the project grows.

## Decision
Each use case class defines its own Request and/or Response classes.

### How to apply
1. **Identify the Entity** from the URL's resource name (User, Item)
   - For nested resources: combine parent and child in order (UserItem, UserOrder)
   - For aggregated data: use the concept name (UserDashboard, UserOrderSummary)
2. **Identify the Action** from the URL's last segment
   - **Verb** (activate, suspend): use it as the action
   - **Noun** (profile, address): use the HTTP method and append noun to entity
3. **Create the use case class** as `{Action}{Entity}` with a `handle()` method
4. **Create the DTOs** by appending `Request` and/or `Response` to the use case class' name
5. **Place use case and all its related classes** in a package with the same name, under `/usecase` in the application layer

### Rules
1. The action expresses **user intent** (Create, Update, Search, Activate, ...), even if it differs from
   the literal URL verb (validate → Check, when checking existence rather than format).
    - Compound actions (BatchGet, BatchCreate, ...) keep the modifier on the action, never the entity.
2. Entity name is **always singular** (User not Users), even when returning collections.
3. Nested DTO classes use the concept name only — no Request/Response suffix, no entity prefix; nesting plus the module already provide the scope.
4. All classes and methods inside a use case are package-private.

## General principles for naming REST endpoints

### Start with REST conventions
Use standard HTTP methods and resource nouns whenever they clearly convey the endpoint's purpose:

| Method |  Endpoint path  | Action | Entity | Use Case name |                 DTOs name                  |
|:------:|:---------------:|:------:|:------:|:-------------:|:------------------------------------------:|
|  POST  |     /users      | Create |  User  |  CreateUser   | CreateUserRequest <br/> CreateUserResponse |
|  GET   | /users/{userId} |  Get   |  User  |    GetUser    |    GetUserRequest <br/> GetUserResponse    |
|  PUT   | /users/{userId} | Update |  User  |  UpdateUser   | UpdateUserRequest <br/> UpdateUserResponse |

### Enhance with sub-resources and actions
When REST conventions become limiting, use sub-resources and action verbs to express specific business operations:

| Method |      Endpoint path       |  Action  |   Entity    |   Use Case name   |                        DTOs name                         |
|:------:|:------------------------:|:--------:|:-----------:|:-----------------:|:--------------------------------------------------------:|
|  PUT   | /users/{userId}/profile  |  Update  | UserProfile | UpdateUserProfile | UpdateUserProfileRequest <br/> UpdateUserProfileResponse |
|  PUT   |  /users/{userId}/detail  |  Update  | UserDetail  | UpdateUserDetail  |  UpdateUserDetailRequest <br/> UpdateUserDetailResponse  |
|  PUT   | /users/{userId}/address  |  Update  | UserAddress | UpdateUserAddress | UpdateUserAddressRequest <br/> UpdateUserAddressResponse |
|  POST  | /users/{userId}/activate | Activate |    User     |   ActivateUser    |      ActivateUserRequest <br/> ActivateUserResponse      |

### Handle collection endpoints
- Use GET for small, stable, unfiltered lists. The response will be returned as `List<T>`.

- Use POST with a `search` action for filtered/paginated collections. The response will be wrapped in Spring's `Page<T>`.
  Example: `Page<SearchUserResponse>`
  > **Why `search` instead of `get`?** 
  > 
  > It avoids naming collisions with single-item GET endpoints. 
  > 
  > Example: `GET /tickets/{ticketId}` uses `GetTicket...`, while `POST /tickets/search` uses `SearchTicket...`
  
- If additional metadata is required that cannot be represented by `List<T>` or `Page<T>`, create a dedicated response
  object following the same naming convention.
  Example:
```java
record SearchUserResponse(
		List<User> users,
		boolean includeDeleted,
		long totalCount
) {
	record User(
			Long id,
			String name,
			String email
	) {}
}
```

| Method |     Endpoint path     | Action |  Entity  | Use Case name |                   DTOs name                    |
|:------:|:---------------------:|:------:|:--------:|:-------------:|:----------------------------------------------:|
|  POST  |     /users/search     | Search |   User   |  SearchUser   |   SearchUserRequest <br/> SearchUserResponse   |
|  GET   | /users/{userId}/items |  Get   | UserItem |  GetUserItem  |  GetUserItemRequest <br/> GetUserItemResponse  |
|  POST  |    /tickets/search    | Search |  Ticket  | SearchTicket  | SearchTicketRequest <br/> SearchTicketResponse |
|  GET   |  /tickets/{ticketId}  |  Get   |  Ticket  |   GetTicket   |    GetTicketRequest <br/> GetTicketResponse    |

### Examples
1) Request and Response, POST endpoint `/users`
```java
class CreateUser {
	CreateUserResponse handle(@RequestBody CreateUserRequest request) { }
}

record CreateUserRequest(
		String firstName,
		String lastName,
		String address
) {}

record CreateUserResponse(
		int id,
		String createdAt	
) {}
```
2) Request only with nested class, PUT endpoint `/users/{userId}`
```java
class UpdateUser {
	void handle(@RequestBody UpdateUserRequest request) { }
}

record UpdateUserRequest(
		String firstName,
		String lastName,
		int age,
		Company company
) { 
	record Company(
			String name,
			String country,
			int numberOfEmployees
    ) {}
}
```
3) Request and paginated Response, POST endpoint `/users/search`
```java
import org.springframework.data.domain.Page;

class SearchUser {	
    Page<SearchUserResponse> handle(@RequestBody SearchUserRequest request) { }
}

record SearchUserRequest(
		int pageIndex,
		int pageSize,
		String name,
		String country
) {}

record SearchUserResponse(
		String name,
		String country,
		String address
) {}
```

### Handle existence/uniqueness endpoints
Name the use case `Check{Entity}{Property}Exists` or `Check{Entity}{Property}Availability`.

Because these names stack multiple concepts (Check + Entity + Property + Exists/Availability),
they grow long quickly. If the entity is unambiguous from the module it lives in, you may drop it:
`Check{Property}Exists` or `Check{Property}Availability`.

### Example
```java
class CheckSerialNumberExists {
	@GetMapping("/washing-machines/{serialNumber}/exists")
	boolean handle(@PathVariable String serialNumber) {
		return repository.existsBySerialNumber(serialNumber);
	}
}
```

## Consequences
**Positives:**
- Consistent naming - every request/response follows the same convention and is self-explanatory
- Clarity - the action (Get, Create, Search, Update, Delete, BatchCreate, ...) is immediately clear from the response/request name
- Independent evolution - each endpoint's request/response can evolve independently

**Negatives:**
- Reduced naming flexibility may frustrate developers who prefer different conventions
- Requires discipline during code reviews to enforce

## Compliance
- Enforcing is done via Code Review
- Possible to enforce it via ArchUnit
