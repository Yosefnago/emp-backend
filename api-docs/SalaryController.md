# API Documentation for SalaryController
Base Path: `/salary`


---
## Get `/salary/salaries`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<BigDecimal>`

### Request Parameters
None

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`BigDecimal`)
No structured body (e.g., primitive, void).

---
## Get `/salary/{personalId}`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<BigDecimal>`

### Request Parameters
| Name | Type |
|------|------|
| `personalId` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`BigDecimal`)
No structured body (e.g., primitive, void).

---
## Get `/salary/emp/{personalId}`

**Method:** `GetMapping`
**Return Type:** `ResponseEntity<SalaryDetailsPerEmployee>`

### Request Parameters
| Name | Type |
|------|------|
| `personalId` | `String` |

### Request Body (`User`)
No structured request body (e.g., path variables only).

### Response Body (`SalaryDetailsPerEmployee`)
| Field | Type |
|-------|------|
| `salaryPerHour` | `double` |
| `monthlySalary` | `double` |
| `bonus` | `String` |
| `bankName` | `String` |
| `bankAccount` | `String` |
| `pensionFund` | `String` |
| `totalHoursMonth` | `double` |
| `overtimeHours` | `double` |
| `vacationDays` | `double` |
