# API Documentation for UserController
Base Path: `auth`


---
## Post `auth/login`

**Method:** `PostMapping`
**Return Type:** `ResponseEntity<UserLoginResponse>`

### Request Parameters
None

### Request Body (`UserLoginRequest`)
| Field | Type |
|-------|------|
| `username` | `String` |
| `password` | `String` |

### Response Body (`UserLoginResponse`)
| Field | Type |
|-------|------|
| `message` | `String` |
| `token` | `String` |

---
## Post `auth/register`

**Method:** `PostMapping`
**Return Type:** `ResponseEntity<UserRegisterResponse>`

### Request Parameters
None

### Request Body (`UserRegisterRequest`)
| Field | Type |
|-------|------|
| `username` | `String` |
| `password` | `String` |
| `email` | `String` |

### Response Body (`UserRegisterResponse`)
| Field | Type |
|-------|------|
| `message` | `String` |
