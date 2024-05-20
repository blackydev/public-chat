# Public Chat

College project. Project is a simple chat application written without any frameworks or libraries other than postgresql
JDBC. The server was developed in `java 21`.

## Tech Stack

**Backend:** `Java` `PostgreSQL` `Docker`

**Frontend** `HTML` `CSS` `Javascript`

## API Endpoints

| Method   | Path                           | Description                       | Permissions |
|:---------|:-------------------------------|-----------------------------------|-------------|
| `POST`   | `/api/auth`                    | Authenticates user                | none        |
| `POST`   | `/api/users`                   | Creates user                      | none        |
| `PUT`    | `/api/users/me`                | Update own user                   | user        |
| `POST`   | `/api/users/permissions/admin` | Adds admin permissions to user    | admin       |
| `DELETE` | `/api/users/permissions/admin` | Removes admin permissions to user | admin       |
| `GET`    | `/api/messages/last/id`        | Gets last message id              | user        |
| `GET`    | `/api/messages`                | Gets list of message              | user        |
| `POST`   | `/api/messages`                | Creates message                   | user        |
| `DELETE` | `/api/messages/{id}`           | Deletes message                   | admin       |

## Authentication

Application uses basic authentication. To authenticate it's required to send `Authorization` header
in `Basic username:password` format (where username:password is Base64 encoded).

## How to run

