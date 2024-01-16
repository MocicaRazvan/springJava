# Wellness Specification

Wellness API Documentation

## Version: 1.0

### Terms of service

Terms of service

**Contact information:**  
Mocica Razvan  
razvanmocica@gmail.com

**License:** [MIT License](https://choosealicense.com/licenses/mit/)

### /users/{id}

#### GET

##### Summary:

Get user by id

##### Description:

All authenticated users can access

##### Parameters

| Name | Located in | Description        | Required | Schema |
|------|------------|--------------------|----------|--------|
| id   | path       | The id of the user | Yes      | long   |

##### Responses

| Code | Description                                    |
|------|------------------------------------------------|
| 200  | The response will contain an user response dto |
| 400  | Entities related problems                      |
| 404  | The user credentials are not valid             |

#### PUT

##### Summary:

Update the user

##### Description:

All authenticated users can access
For the update to be successfully the user that makes the update should be the same as the one updated

##### Parameters

| Name | Located in | Description        | Required | Schema |
|------|------------|--------------------|----------|--------|
| id   | path       | The id of the user | Yes      | long   |

##### Responses

| Code | Description                                   |
|------|-----------------------------------------------|
| 200  | The response will contain a user response dto |
| 400  | Entities related problems                     |
| 404  | The user credentials are not valid            |

### /trainings/update/{id}

#### PUT

##### Summary:

Update model

##### Description:

    Only admin and trainer can access.
    For the update to be successful the user that updates the model
    needs to be the user that created the model

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                              |
|------|--------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated fields |
| 400  | Entities related problems                                                |
| 404  | The user credentials are not valid                                       |

### /posts/update/{id}

#### PUT

##### Summary:

Update model

##### Description:

    Only admin and trainer can access.
    For the update to be successful the user that updates the model
    needs to be the user that created the model

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                              |
|------|--------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated fields |
| 400  | Entities related problems                                                |
| 404  | The user credentials are not valid                                       |

### /orders/update/{id}

#### PUT

##### Summary:

Update model

##### Description:

    Only admin and trainer can access.
    For the update to be successful the user that updates the model
    needs to be the user that created the model

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                              |
|------|--------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated fields |
| 400  | Entities related problems                                                |
| 404  | The user credentials are not valid                                       |

### /exercises/update/{id}

#### PUT

##### Summary:

Update model

##### Description:

    Only admin and trainer can access.
    For the update to be successful the user that updates the model
    needs to be the user that created the model

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                              |
|------|--------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated fields |
| 400  | Entities related problems                                                |
| 404  | The user credentials are not valid                                       |

### /comments/update/{id}

#### PUT

##### Summary:

Update comment

##### Description:

    For the update to be successful the user that updates the model
    needs to be the user that created the model

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                              |
|------|--------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated fields |
| 400  | Entities related problems                                                |
| 404  | The user credentials are not valid                                       |

### /trainings/create

#### POST

##### Summary:

Create a model

##### Description:

Only admin and trainer can access.

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 201  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /posts/create

#### POST

##### Summary:

Create a model

##### Description:

Only admin and trainer can access.

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 201  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /orders

#### POST

##### Summary:

Create an order

##### Description:

All authenticated users can access.

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 201  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /exercises/create

#### POST

##### Summary:

Create a model

##### Description:

Only admin and trainer can access.

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 201  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /comments/create/{postId}

#### POST

##### Summary:

Create comment for a post

##### Description:

All authenticated users can access.
For the comment to be created the post should exist.

##### Parameters

| Name   | Located in | Description                                 | Required | Schema |
|--------|------------|---------------------------------------------|----------|--------|
| postId | path       | The id of the post on which comments are on | Yes      | long   |

##### Responses

| Code | Description                                        |
|------|----------------------------------------------------|
| 201  | The response will contain the comment response dto |
| 400  | Entities related problems                          |
| 404  | The user credentials are not valid                 |

### /auth/register

#### POST

##### Summary:

Register a new user

##### Description:

Unprotected.
For the register to be successfully the email should not be already taken
It will also be returned a JWT

##### Responses

| Code | Description                                                        |
|------|--------------------------------------------------------------------|
| 200  | The user will be registered to the database and will be sent a JWT |
| 400  | Entities related problems                                          |
| 404  | The user credentials are not valid                                 |

### /auth/login

#### POST

##### Summary:

Login an existing user

##### Description:

Unprotected.
For the login to be successfully the email and password should be correct
It will also be returned a JWT

##### Responses

| Code | Description                                                        |
|------|--------------------------------------------------------------------|
| 200  | The user will be registered to the database and will be sent a JWT |
| 400  | Entities related problems                                          |
| 404  | The user credentials are not valid                                 |

### /users

#### PATCH

##### Summary:

Get all the users

##### Description:

All authenticated users can access

##### Parameters

| Name  | Located in | Description                   | Required | Schema     |
|-------|------------|-------------------------------|----------|------------|
| roles | query      | The roles to filter the users | No       | [ string ] |

##### Responses

| Code | Description                                                                                   |
|------|-----------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of user response dto |
| 400  | Entities related problems                                                                     |
| 404  | The user credentials are not valid                                                            |

### /users/admin/{id}

#### PATCH

##### Summary:

Make a user trainer

##### Description:

Only admin can access. If the user is already trainer or admin the update will fail.

##### Parameters

| Name | Located in | Description        | Required | Schema |
|------|------------|--------------------|----------|--------|
| id   | path       | The id of the user | Yes      | long   |

##### Responses

| Code | Description                                   |
|------|-----------------------------------------------|
| 200  | The response will contain a user response dto |
| 400  | Entities related problems                     |
| 404  | The user credentials are not valid            |

### /trainings/trainer/{trainerId}

#### PATCH

##### Summary:

Get all the models of a trainer

##### Description:

Only admin and trainer can access.
For the "get" to be successful the user either needs to be an admin
or the be the owner of the models.

##### Parameters

| Name      | Located in | Description                                  | Required | Schema |
|-----------|------------|----------------------------------------------|----------|--------|
| trainerId | path       | The id of the trainer that made the entities | Yes      | long   |

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /trainings/like/{id}

#### PATCH

##### Summary:

Like a model

##### Description:

All authenticated users can access.
If the user didn't dislike the model previously, he will just be
added to the likes set. If he disliked the model previously he will also
be removed from the dislikes set. But, if he liked the model previously
he will just be removed from the liked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /trainings/dislike/{id}

#### PATCH

##### Summary:

Dislike a model

##### Description:

All authenticated users can access.
If the user didn't like the model previously, he will just be
added to the dislikes set. If he liked the model previously he will also
be removed from the likes set. But, if he disliked the model previously
he will just be removed from the disliked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /trainings/approved

#### PATCH

##### Summary:

Get all approved models

##### Description:

All authenticated users can access

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /trainings/admin

#### PATCH

##### Summary:

Get all the models

##### Description:

Only admin can access

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /trainings/admin/approve/{id}

#### PATCH

##### Summary:

Approve a model

##### Description:

Only admin can access.
For the approval to be successfully the model needs to be unapproved.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                      |
|------|----------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the update approved status |
| 400  | Entities related problems                                                        |
| 404  | The user credentials are not valid                                               |

### /posts/trainer/{trainerId}

#### PATCH

##### Summary:

Get all the models of a trainer

##### Description:

Only admin and trainer can access.
For the "get" to be successful the user either needs to be an admin
or the be the owner of the models.

##### Parameters

| Name      | Located in | Description                                  | Required | Schema |
|-----------|------------|----------------------------------------------|----------|--------|
| trainerId | path       | The id of the trainer that made the entities | Yes      | long   |

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /posts/like/{id}

#### PATCH

##### Summary:

Like a model

##### Description:

All authenticated users can access.
If the user didn't dislike the model previously, he will just be
added to the likes set. If he disliked the model previously he will also
be removed from the dislikes set. But, if he liked the model previously
he will just be removed from the liked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /posts/dislike/{id}

#### PATCH

##### Summary:

Dislike a model

##### Description:

All authenticated users can access.
If the user didn't like the model previously, he will just be
added to the dislikes set. If he liked the model previously he will also
be removed from the likes set. But, if he disliked the model previously
he will just be removed from the disliked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /posts/approved

#### PATCH

##### Summary:

Get all approved models

##### Description:

All authenticated users can access

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /posts/admin

#### PATCH

##### Summary:

Get all the models

##### Description:

Only admin can access

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /posts/admin/approve/{id}

#### PATCH

##### Summary:

Approve a model

##### Description:

Only admin can access.
For the approval to be successfully the model needs to be unapproved.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                      |
|------|----------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the update approved status |
| 400  | Entities related problems                                                        |
| 404  | The user credentials are not valid                                               |

### /orders/user/{userId}

#### PATCH

##### Summary:

Get the orders for a user

##### Description:

All authenticated users can access.
For the retrieve to be successfully the authenticated user
needs to be the users that made the order or the be an admin.

##### Parameters

| Name      | Located in | Description                             | Required | Schema |
|-----------|------------|-----------------------------------------|----------|--------|
| userId    | path       | The id of the user that made the orders | Yes      | long   |
| orderType | query      | The type of orders to retrieve          | No       | string |

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /orders/pay/{id}

#### PATCH

##### Summary:

Pay an order

##### Description:

All authenticated users can access.
For the update to be successfully the authenticated user
needs to be the users that made the order and also the order
should not be already payed.

##### Parameters

| Name | Located in | Description         | Required | Schema |
|------|------------|---------------------|----------|--------|
| id   | path       | The id of the order | Yes      | long   |

##### Responses

| Code | Description                                                                       |
|------|-----------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the payed field set to true |
| 400  | Entities related problems                                                         |
| 404  | The user credentials are not valid                                                |

### /orders/admin

#### PATCH

##### Summary:

Get all the orders by type

##### Description:

Only admin can access

##### Parameters

| Name      | Located in | Description                    | Required | Schema |
|-----------|------------|--------------------------------|----------|--------|
| orderType | query      | The type of orders to retrieve | No       | string |

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /exercises/trainer/{trainerId}

#### PATCH

##### Summary:

Get all the models of a trainer

##### Description:

Only admin and trainer can access.
For the "get" to be successful the user either needs to be an admin
or the be the owner of the models.

##### Parameters

| Name      | Located in | Description                                  | Required | Schema |
|-----------|------------|----------------------------------------------|----------|--------|
| trainerId | path       | The id of the trainer that made the entities | Yes      | long   |

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /exercises/like/{id}

#### PATCH

##### Summary:

Like a model

##### Description:

All authenticated users can access.
If the user didn't dislike the model previously, he will just be
added to the likes set. If he disliked the model previously he will also
be removed from the dislikes set. But, if he liked the model previously
he will just be removed from the liked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /exercises/dislike/{id}

#### PATCH

##### Summary:

Dislike a model

##### Description:

All authenticated users can access.
If the user didn't like the model previously, he will just be
added to the dislikes set. If he liked the model previously he will also
be removed from the likes set. But, if he disliked the model previously
he will just be removed from the disliked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /exercises/approved

#### PATCH

##### Summary:

Get all approved models

##### Description:

All authenticated users can access

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /exercises/admin

#### PATCH

##### Summary:

Get all the models

##### Description:

Only admin can access

##### Responses

| Code | Description                                                                                     |
|------|-------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of entity response dto |
| 400  | Entities related problems                                                                       |
| 404  | The user credentials are not valid                                                              |

### /exercises/admin/approve/{id}

#### PATCH

##### Summary:

Approve a model

##### Description:

Only admin can access.
For the approval to be successfully the model needs to be unapproved.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                      |
|------|----------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the update approved status |
| 400  | Entities related problems                                                        |
| 404  | The user credentials are not valid                                               |

### /comments/{postId}

#### PATCH

##### Summary:

Get all the comments for a post

##### Description:

All authenticated users can access. For the operation to be successful, the post should exist.

##### Parameters

| Name   | Located in | Description                                 | Required | Schema |
|--------|------------|---------------------------------------------|----------|--------|
| postId | path       | The id of the post on which comments are on | Yes      | long   |

##### Responses

| Code | Description                                                                                      |
|------|--------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of comment response dto |
| 400  | Entities related problems                                                                        |
| 404  | The user credentials are not valid                                                               |

### /comments/user/{userId}

#### PATCH

##### Summary:

Get all the comments for a user

##### Description:

All authenticated users can access.
For the operation to be successfully the users should exist.

##### Parameters

| Name   | Located in | Description                               | Required | Schema |
|--------|------------|-------------------------------------------|----------|--------|
| userId | path       | The id of the user that made the comments | Yes      | long   |

##### Responses

| Code | Description                                                                                      |
|------|--------------------------------------------------------------------------------------------------|
| 200  | The response will contain a pageable response with the payload as a list of comment response dto |
| 400  | Entities related problems                                                                        |
| 404  | The user credentials are not valid                                                               |

### /comments/like/{id}

#### PATCH

##### Summary:

Like a model

##### Description:

All authenticated users can access.
If the user didn't dislike the model previously, he will just be
added to the likes set. If he disliked the model previously he will also
be removed from the dislikes set. But, if he liked the model previously
he will just be removed from the liked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /comments/dislike/{id}

#### PATCH

##### Summary:

Dislike a model

##### Description:

All authenticated users can access.
If the user didn't like the model previously, he will just be
added to the dislikes set. If he liked the model previously he will also
be removed from the likes set. But, if he disliked the model previously
he will just be removed from the disliked set.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                                          |
|------|--------------------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto with the updated likes and dislikes |
| 400  | Entities related problems                                                            |
| 404  | The user credentials are not valid                                                   |

### /users/roles

#### GET

##### Summary:

Get the the possible roles a user can have

##### Description:

All authenticated users can access

##### Responses

| Code | Description                                                |
|------|------------------------------------------------------------|
| 200  | The response will contain a set of all possible user roles |
| 400  | Entities related problems                                  |
| 404  | The user credentials are not valid                         |

### /trainings/{id}

#### GET

##### Summary:

Get model by id

##### Description:

All authenticated users can access. For the retrieve to be successful the model should be approved or the user should be
the owner or admin

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /posts/{id}

#### GET

##### Summary:

Get model by id

##### Description:

All authenticated users can access. For the retrieve to be successful the model should be approved or the user should be
the owner or admin

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /orders/{id}

#### GET

##### Summary:

Get order by id

##### Description:

All authenticated users can access. For the retrieve to be successfully the user should be the one that made the order
or admin.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /orders/types

#### GET

##### Summary:

Get all the order types

##### Description:

All authenticated users can access.

##### Responses

| Code | Description                                                   |
|------|---------------------------------------------------------------|
| 200  | The response will contain a set with all possible order types |
| 400  | Entities related problems                                     |
| 404  | The user credentials are not valid                            |

### /exercises/{id}

#### GET

##### Summary:

Get model by id

##### Description:

All authenticated users can access. For the retrieve to be successful the model should be approved or the user should be
the owner or admin

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /comments/{id}

#### GET

##### Summary:

Get model by id

##### Description:

All authenticated users can access. For the retrieve to be successful the model should be approved or the user should be
the owner or admin

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                      |
|------|--------------------------------------------------|
| 200  | The response will contain an entity response dto |
| 400  | Entities related problems                        |
| 404  | The user credentials are not valid               |

### /trainings/delete/{id}

#### DELETE

##### Summary:

Delete model

##### Description:

Only admin and trainer can access.
For the delete to be successful the user either needs to be an admin
or the be the owner of the model being deleted.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                            |
|------|------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto od the deleted object |
| 400  | Entities related problems                                              |
| 404  | The user credentials are not valid                                     |

### /posts/delete/{id}

#### DELETE

##### Summary:

Delete model

##### Description:

Only admin and trainer can access.
For the delete to be successful the user either needs to be an admin
or the be the owner of the model being deleted.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                            |
|------|------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto od the deleted object |
| 400  | Entities related problems                                              |
| 404  | The user credentials are not valid                                     |

### /orders/delete/{id}

#### DELETE

##### Summary:

Delete model

##### Description:

Only admin and trainer can access.
For the delete to be successful the user either needs to be an admin
or the be the owner of the model being deleted.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                            |
|------|------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto od the deleted object |
| 400  | Entities related problems                                              |
| 404  | The user credentials are not valid                                     |

### /exercises/delete/{id}

#### DELETE

##### Summary:

Delete model

##### Description:

Only admin and trainer can access.
For the delete to be successful the user either needs to be an admin
or the be the owner of the model being deleted.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                            |
|------|------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto od the deleted object |
| 400  | Entities related problems                                              |
| 404  | The user credentials are not valid                                     |

### /comments/delete/{id}

#### DELETE

##### Summary:

Delete model

##### Description:

Only admin and trainer can access.
For the delete to be successful the user either needs to be an admin
or the be the owner of the model being deleted.

##### Parameters

| Name | Located in | Description          | Required | Schema |
|------|------------|----------------------|----------|--------|
| id   | path       | The id of the entity | Yes      | long   |

##### Responses

| Code | Description                                                            |
|------|------------------------------------------------------------------------|
| 200  | The response will contain an entity response dto od the deleted object |
| 400  | Entities related problems                                              |
| 404  | The user credentials are not valid                                     |
