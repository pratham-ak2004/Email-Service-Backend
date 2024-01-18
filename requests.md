# Login Requests

## get requests:

- login : http://localhost:8080/login
```json
// class : Email
{
    "email": "",
    "password": ""
}
```
- signup : http://localhost:8080/signup
```json
// class : Email
{
    "email": "",
    "password": ""
}
```

## post requests:

NULL

# Inbox Requests

## get requests:

- All messages : http://localhost:8080/user/getAll
```json
// class : Email
{
    "email": ""
}
```
- Get by search : http://localhost:8080/user/getSearch
```json
// class : MultiRequest
{
  "targetText": "",
  "email": {
    "email": ""
  }
}
```
- Sent mails : http://localhost:8080/user/getSent
```json
// class : Email
{
    "email": ""
}
```
- Received mails : http://localhost:8080/user/getReceived
```json
// class : Email
{
    "email": ""
}
```

## post requests:

- Send mail : http://localhost:8080/user/addMail
```json
// class : Mail
{
  "sender": "",
  "to": [
    {"email": "", "value": true},
    {"email": "", "value": false}
  ],
  "cc": [
    {"email": "", "value": true},
    {"email": "", "value": false}
  ],
  "bcc": [
    {"email": "", "value": true},
    {"email": "", "value": false}
  ],
  "subject": "",
  "message": ""
}
```