# API Documentation for EmployeeController
Base Path: `/employees`


---
## Get `/employees/loadAll`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<List<EmployeeListResponse>>`

### Request Parameters
None

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`List<EmployeeListResponse>`)
No structured body (e.g., primitive, void).

---
## Get `/employees/{personalId}`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<EmployeeDetailsResponse>`

### Request Parameters
| Name | Type |
|------|------|
| `personalId` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`EmployeeDetailsResponse`)
| Field | Type |
|-------|------|
| `firstName` | `String` |
| `lastName` | `String` |
| `personalId` | `String` |
| `email` | `String` |
| `phone` | `String` |
| `position` | `String` |
| `department` | `String` |
| `address` | `String` |
| `hireDate` | `Date` |
| `status` | `String` |
| `createdAt` | `Timestamp` |
| `updatedAt` | `Timestamp` |

---
## Post `/employees/addEmployee`

**Method:** `PostMapping`
**Return Type:** `ResponseEntity<AddEmployeeResponse>`

### Request Parameters
| Name | Type |
|------|------|
| `addEmployeeRequest` | `AddEmployeeRequest` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`AddEmployeeResponse`)
| Field | Type |
|-------|------|
| `message` | `String` |

---
## Put `/employees/updateEmployeeDetails`

**Method:** `PutMapping`
**Return Type:** `ResponseEntity<UpdateEmployeeDetailsResponse>`

### Request Parameters
| Name | Type |
|------|------|
| `updateEmployeeDetailsRequest` | `UpdateEmployeeDetailsRequest` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`UpdateEmployeeDetailsResponse`)
| Field | Type |
|-------|------|
| `message` | `String` |

---
## Delete `/employees/delete/{id}`

**Method:** `DeleteMapping`
**Return Type:** `ResponseEntity<Void>`

### Request Parameters
| Name | Type |
|------|------|
| `id` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`Void`)
No structured body (e.g., primitive, void).

---
## Get `/employees/loadNumberOfEmployees`

**Method:** `GetMapping`
**Return Type:** `int`

### Request Parameters
None

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`int`)
No structured body (e.g., primitive, void).

---
## Get `/employees/test`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<?>`

### Request Parameters
None

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`?`)
No structured body (e.g., primitive, void).
