# API Documentation for EmployeeController
Base Path: `/employees`


---
## Get `/employees`

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
| `gender` | `String` |
| `birthDate` | `LocalDate` |
| `familyStatus` | `String` |
| `phone` | `String` |
| `position` | `String` |
| `department` | `String` |
| `address` | `String` |
| `city` | `String` |
| `country` | `String` |
| `hireDate` | `LocalDate` |
| `jobType` | `String` |
| `status` | `String` |
| `statusAttendance` | `String` |
| `updatedAt` | `LocalDate` |

---
## Post `/employees/add`

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
## Put `/employees/{personalId}`

**Method:** `PutMapping`
**Return Type:** `ResponseEntity<UpdateEmployeeDetailsResponse>`

### Request Parameters
| Name | Type |
|------|------|
| `updateEmployeeDetailsRequest` | `UpdateEmployeeDetailsRequest` |
| `personalId` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`UpdateEmployeeDetailsResponse`)
| Field | Type |
|-------|------|
| `message` | `String` |

---
## Delete `/employees/{id}`

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
## Get `/employees/test`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<?>`

### Request Parameters
None

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`?`)
No structured body (e.g., primitive, void).
