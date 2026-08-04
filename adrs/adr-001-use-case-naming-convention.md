# Use Case Naming Convention

## Status
Accepted: Decision approved and in effect. Please don't hesitate to challenge it.

## Context
We are adopting a vertical-slicing / use-case architecture, where each use case *is* an endpoint — one class, 
one route, no separate controller layer. Each use case owns its own package-private Request/Response DTOs. 
We have no established naming convention for use case classes under this model.

Pain points:
- Shared Requests / Responses across endpoints make API contracts harder to evolve independently.
- Time spent in code reviews debating naming conventions.
- DTO names without a clear indication of whether they are requests or responses.

We need a simple, predictable naming convention that can scale as the project grows.

## Decision
Each use case class defines its own Request and/or Response classes.

### How to apply
1. **Identify the Entity** from the URL's resource name (User, Item)
   - For nested resources: combine parent and child in order (UserItem, UserOrder)
   - For aggregated data: use the concept name (UserDashboard, UserOrderSummary)
   - Entity name is **always singular** (User not Users), even when returning collections.

> **Why singular even for Batch/Search?**
>
> The Entity name describes the kind of object (a DTO row, or a response container),
> not its cardinality — even when that object holds a list internally (e.g. `SearchUserResponse.users`).
> Cardinality is carried by the wrapper type (`List<T>`, `Page<T>`, `Map<K,V>`)
> or an internal field, not the class name. Pluralizing the Entity would just
> duplicate that signal and risk drifting out of sync with the real shape.

2. **Identify the Action** from the URL's last segment
   - **Verb** (activate, suspend): use it as the action
   - **Noun** (profile, address): use the HTTP method and append noun to entity
   - The action expresses **user intent** (Create, Update, Search, Activate, ...)
   - Compound actions (BatchGet, BatchCreate, ...) keep the modifier on the action, never the entity.
3. **Create the use case class** as `{Action}{Entity}` with a `handle()` method
4. **Create the DTOs** by appending `Request` and/or `Response` to the use case class' name
   - Nested DTO classes use the concept name only — no Request/Response suffix, no entity prefix.
5. **Place use case and its related classes** in a package with the same name, under `/usecase` in the application layer
   - All classes and methods inside a use case are package-private.

### Simple CRUD endpoints
Use standard HTTP methods and resource nouns whenever they clearly convey the endpoint's purpose:

| Method |    Endpoint     | Action | Entity |  Use Case  |                    DTOs                    |
|:------:|:---------------:|:------:|:------:|:----------:|:------------------------------------------:|
|  POST  |     /users      | Create |  User  | CreateUser | CreateUserRequest <br/> CreateUserResponse |
|  GET   | /users/{userId} |  Get   |  User  |  GetUser   |    GetUserRequest <br/> GetUserResponse    |
|  PUT   | /users/{userId} | Update |  User  | UpdateUser | UpdateUserRequest <br/> UpdateUserResponse |

Example with nested class:
```java
class UpdateUser {
	@PutMapping("/users/{userId}")
	void handle(@PathVariable String userId, @RequestBody UpdateUserRequest request) { }
}

record UpdateUserRequest(String firstName, String lastName, int age, Company company) {
	record Company(String name, String country, int numberOfEmployees) {}
}
```

### Sub-resource and action endpoints
When REST conventions become limiting, use sub-resources and action verbs to express specific business operations:

| Method |         Endpoint         |  Action  |   Entity    |     Use Case      |                           DTOs                           |
|:------:|:------------------------:|:--------:|:-----------:|:-----------------:|:--------------------------------------------------------:|
|  PUT   | /users/{userId}/profile  |  Update  | UserProfile | UpdateUserProfile | UpdateUserProfileRequest <br/> UpdateUserProfileResponse |
|  PUT   |  /users/{userId}/detail  |  Update  | UserDetail  | UpdateUserDetail  |  UpdateUserDetailRequest <br/> UpdateUserDetailResponse  |
|  PUT   | /users/{userId}/address  |  Update  | UserAddress | UpdateUserAddress | UpdateUserAddressRequest <br/> UpdateUserAddressResponse |
|  POST  | /users/{userId}/activate | Activate |    User     |   ActivateUser    |      ActivateUserRequest <br/> ActivateUserResponse      |

### Collection endpoints
- Use GET for small, stable, unfiltered lists. The response will be returned as `List<T>`.
- Use POST with a `search` action for filtered/paginated collections. The response will be wrapped in Spring's `Page<T>`.
  Example: `Page<SearchUserResponse>`
  > **Why `search` instead of `get`?** 
  > 
  > It avoids naming collisions with single-item GET endpoints. 
  > 
  > Example: `GET /tickets/{ticketId}` uses `GetTicket...`, while `POST /tickets/search` uses `SearchTicket...`  
- If additional metadata is required that cannot be represented by `List<T>` or `Page<T>`, create a dedicated response
  object following the same naming convention:
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

| Method |       Endpoint        | Action |  Entity  |   Use Case   |                      DTOs                      |
|:------:|:---------------------:|:------:|:--------:|:------------:|:----------------------------------------------:|
|  POST  |     /users/search     | Search |   User   |  SearchUser  |   SearchUserRequest <br/> SearchUserResponse   |
|  GET   | /users/{userId}/items |  Get   | UserItem | GetUserItem  |  GetUserItemRequest <br/> GetUserItemResponse  |
|  POST  |    /tickets/search    | Search |  Ticket  | SearchTicket | SearchTicketRequest <br/> SearchTicketResponse |
|  GET   |  /tickets/{ticketId}  |  Get   |  Ticket  |  GetTicket   |    GetTicketRequest <br/> GetTicketResponse    |

Example with paginated Response:
```java
import org.springframework.data.domain.Page;

class SearchUser {
	@PostMapping("/users/search")
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
### Batch endpoints
Use batch endpoints when the client performs the same operation on multiple resources.

| Method |       Endpoint        |   Action    | Entity |     Use Case      |                           DTOs                           |
|:------:|:---------------------:|:-----------:|:------:|:-----------------:|:--------------------------------------------------------:|
|  POST  |   /users/batch/get    |  BatchGet   |  User  |   BatchGetUser    |      BatchGetUserRequest <br/> BatchGetUserResponse      |
|  POST  | /tickets/batch/create | BatchCreate | Ticket | BatchCreateTicket | BatchCreateTicketRequest <br/> BatchCreateTicketResponse |
|  POST  | /tickets/batch/delete | BatchDelete | Ticket | BatchDeleteTicket | BatchDeleteTicketRequest <br/> BatchDeleteTicketResponse |

Example:
```java
class BatchGetTicket {
	@PostMapping("/tickets/batch/get")
	Map<String, BatchGetTicketResponse> handle(@RequestBody Set<String> tickets) {}
}
```

### Existence endpoints
Name the use case `ExistsBy{Property}`. Add the Entity only if the property is
ambiguous within the module: `Exists{Entity}By{Property}`.

> Diverges from the `{Action}{Entity}` — these are single-property
> predicates, not entity operations, so there's no Entity to attach.

| Method |                Endpoint                 |       Use Case       |
|:------:|:---------------------------------------:|:--------------------:|
|  GET   |          /users/{email}/exists          |    ExistsByEmail     |
|  GET   | /washing-machines/{serialNumber}/exists | ExistsBySerialNumber |

Example:
```java
class ExistsBySerialNumber {
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
- Can lead to a large number of use case classes over time

## Compliance
- Enforced via code review; ArchUnit enforcement possible in future.

## References
- https://alistair.cockburn.us/hexagonal-architecture
- https://www.youtube.com/watch?v=bKxkIjfTAnQ&list=PL1msPBH9ZGkhpANkreFA_teOnloVdLuCx
- https://www.youtube.com/watch?v=H7HWOlANX78
