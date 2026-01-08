# API Documentation for FilesController
Base Path: `/files`


---
## Get `/files/{personalId}`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<List<FileDto>>`

### Request Parameters
| Name | Type |
|------|------|
| `personalId` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`List<FileDto>`)
No structured body (e.g., primitive, void).

---
## Post `/files/upload/{personalId}`

**Method:** `PostMapping`
**Return Type:** `ResponseEntity<?>`

### Request Parameters
| Name | Type |
|------|------|
| `files` | `List<MultipartFile>` |
| `personalId` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`?`)
No structured body (e.g., primitive, void).

---
## Delete `/files/delete/{documentId}`

**Method:** `DeleteMapping`
**Return Type:** `ResponseEntity<Void>`

### Request Parameters
| Name | Type |
|------|------|
| `documentId` | `Long` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`Void`)
No structured body (e.g., primitive, void).

---
## Get `/files/show/{id}`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<byte[]>`

### Request Parameters
| Name | Type |
|------|------|
| `id` | `Long` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`byte[]`)
No structured body (e.g., primitive, void).

---
## Get `/files/download/{id}`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<byte[]>`

### Request Parameters
| Name | Type |
|------|------|
| `id` | `Long` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`byte[]`)
No structured body (e.g., primitive, void).
