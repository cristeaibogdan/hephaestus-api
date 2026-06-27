## Standardize Endpoint, Request and Response naming convention

## Status
Accepted: Decision approved and in effect. Please don't hesitate to challenge it.

## Context
We are creating multiple endpoints and we have no established naming convention for endpoints, requests, or responses.

Pain points:
- Shared Requests / Responses across endpoints make API contracts harder to evolve independently.
- Time wasted in code reviews debating naming approaches.
- DTO names without a clear indication of whether they are requests or responses.

We need a simple, predictable naming convention that can scale as the project grows.

## Decision
Each endpoint defines its own Request and/or Response classes.

### How to apply
1. **Identify the Entity** from the URL's resource name (User, Item)
   - For nested resources: combine parent and child in order (UserItem, UserOrder)
   - For aggregated data: use the concept name (UserDashboard, UserOrderSummary, UserOrderHistory)
2. **Identify the Action** from the URL's last segment
   - **Verb** (activate, suspend): use it as the action
   - **Noun** (profile, address): use the HTTP method and append noun to entity
3. **Create the classes** as `{Action}{Entity}Request` and/or `{Action}{Entity}Response`

### Rules
1. The action expresses **user intent** (Create, Update, Search, Activate, ...).
2. Entity name is **always singular** (User not Users), even when returning collections.
3. Nested classes use the concept name only — no Request/Response suffix, no entity prefix; nesting plus the module already provide the scope.

## General principles for naming REST endpoints

### Start with REST conventions
Use standard HTTP methods and resource nouns whenever they clearly convey the endpoint's purpose:

| Method |  Endpoint path  | Action | Entity |         Request and Response name          |
|:------:|:---------------:|:------:|:------:|:------------------------------------------:|
|  POST  |     /users      | Create |  User  | CreateUserRequest <br/> CreateUserResponse |
|  GET   | /users/{userId} |  Get   |  User  |    GetUserRequest <br/> GetUserResponse    |
|  PUT   | /users/{userId} | Update |  User  | UpdateUserRequest <br/> UpdateUserResponse |

### Enhance with sub-resources and actions
When REST conventions become limiting, use sub-resources and action verbs to express specific business operations:

| Method |      Endpoint path       |  Action  |   Entity    |                Request and Response name                 |
|:------:|:------------------------:|:--------:|:-----------:|:--------------------------------------------------------:|
|  PUT   | /users/{userId}/profile  |  Update  | UserProfile | UpdateUserProfileRequest <br/> UpdateUserProfileResponse |
|  PUT   |  /users/{userId}/detail  |  Update  | UserDetail  |  UpdateUserDetailRequest <br/> UpdateUserDetailResponse  |
|  PUT   | /users/{userId}/address  |  Update  | UserAddress | UpdateUserAddressRequest <br/> UpdateUserAddressResponse |
|  POST  | /users/{userId}/activate | Activate |    User     |      ActivateUserRequest <br/> ActivateUserResponse      |

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
public record SearchUserResponse(
		List<User> users,
		boolean includeDeleted,
		long totalCount
) {

	public record User(
			Long id,
			String name,
			String email
	) {}
}
```

| Method |     Endpoint path     | Action |  Entity  |           Request and Response name            |
|:------:|:---------------------:|:------:|:--------:|:----------------------------------------------:|
|  POST  |     /users/search     | Search |   User   |   SearchUserRequest <br/> SearchUserResponse   |
|  GET   | /users/{userId}/items |  Get   | UserItem |  GetUserItemRequest <br/> GetUserItemResponse  |
|  POST  |    /tickets/search    | Search |  Ticket  | SearchTicketRequest <br/> SearchTicketResponse |
|  GET   |  /tickets/{ticketId}  |  Get   |  Ticket  |    GetTicketRequest <br/> GetTicketResponse    |

## Example implementations
1) Request and Response, POST endpoint `/users/create`
```java
public record CreateUserRequest(
		String firstName,
		String lastName,
		String address
) {}

public record CreateUserResponse(
		int id,
		String createdAt	
) {}
```
2) Request only with nested class, PUT endpoint `/users/{userId}`
```java
public record UpdateUserRequest(
		String firstName,
		String lastName,
		int age,
		Company company
) { 
	public record Company(
			String name,
			String country,
			int numberOfEmployees
    ) {}
}
```
3) Request and paginated Response, POST endpoint `/users/search`
```java
import org.springframework.data.domain.Page;

public Page<SearchUserResponse> search(@RequestBody SearchUserRequest request) { }

public record SearchUserRequest(
		int pageIndex,
		int pageSize,
		String name,
		String country
) {}

public record SearchUserResponse(
		String name,
		String country,
		String address
) {}
```

## Consequences
**Positives:**
- Consistent naming - every request/response follows the same convention and is self-explanatory
- Clarity - the action (Get, Create, Search, Update, Delete, BatchCreate, ...) is immediately clear from the response/request name
- Independent evolution - each endpoint's request/response can evolve independently

**Negatives:**
- Reduced naming flexibility may frustrate developers who prefer different conventions
- Requires discipline during code reviews to enforce
- Existing endpoints will need gradual migration to match convention // TODO: Remove if used on a greenfield project.

## Compliance
- Enforcing is done via Code Review
- Possible to enforce it via ArchUnit in the java backend 
