# aws-chat-backend

# Swagger UI

http://localhost:8080/swagger-ui/index.html

# 1. UserControler ("/v1/chat/users/")

1.1 GET "/v1/chat/users/{id}" - 하나의 user 정보

http://localhost:8080/v1/chat/users/user100

1.2 GET "/v1/chat/users" - user 리스트

http://localhost:8080/v1/chat/users

1.3 POST "/v1/chat/users" - user 생성 (name, desc)

http://localhost:8080/v1/chat/users

{

    "id": "user100",

    "firstName": "firstName",

    "lastName": "lastName",

    "gptKey": "gptKey",

    "type": "user"

}

1.4 PUT "/v1/chat/users - user 수정

http://localhost:8080/v1/chat/users

{

    "id": "user100",

    "firstName": "firstName",

    "lastName": "lastName",

    "gptKey": "gptKey - update",

    "type": "user"

}

1.5 DELETE "/v1/chat/users/{id}" - user 삭제

http://localhost:8080/v1/chat/users/user100