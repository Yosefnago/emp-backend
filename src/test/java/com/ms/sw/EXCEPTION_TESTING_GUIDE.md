# Exception Handling - Complete Test Guide

## Unit Tests - Exception Classes

### Test 1: UserAlreadyExistsException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `UserAlreadyExistsException` with message "Username test already exists"
2. Assert error code equals "AUTH_001"
3. Assert HTTP status equals `HttpStatus.CONFLICT` (409)
4. Assert message equals "Username test already exists"

**Expected Response:**
- Error code: `AUTH_001`
- HTTP status: `409 CONFLICT`
- Message: "Username test already exists"

**Test Code:**
```java
@Test
void userAlreadyExistsException_shouldHaveCorrectProperties() {
    // Given
    String message = "Username test already exists";
    
    // When
    UserAlreadyExistsException exception = new UserAlreadyExistsException(message);
    
    // Then
    assertEquals("AUTH_001", exception.getErrorCode());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 2: EmailAlreadyExistsException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `EmailAlreadyExistsException` with message "Email test@example.com already exists"
2. Assert error code equals "AUTH_002"
3. Assert HTTP status equals `HttpStatus.CONFLICT` (409)
4. Assert message equals "Email test@example.com already exists"

**Expected Response:**
- Error code: `AUTH_002`
- HTTP status: `409 CONFLICT`
- Message: "Email test@example.com already exists"

**Test Code:**
```java
@Test
void emailAlreadyExistsException_shouldHaveCorrectProperties() {
    // Given
    String message = "Email test@example.com already exists";
    
    // When
    EmailAlreadyExistsException exception = new EmailAlreadyExistsException(message);
    
    // Then
    assertEquals("AUTH_002", exception.getErrorCode());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 3: UserNotFoundException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `UserNotFoundException` with message "User not found"
2. Assert error code equals "AUTH_003"
3. Assert HTTP status equals `HttpStatus.NOT_FOUND` (404)
4. Assert message equals "User not found"

**Expected Response:**
- Error code: `AUTH_003`
- HTTP status: `404 NOT_FOUND`
- Message: "User not found"

**Test Code:**
```java
@Test
void userNotFoundException_shouldHaveCorrectProperties() {
    // Given
    String message = "User not found";
    
    // When
    UserNotFoundException exception = new UserNotFoundException(message);
    
    // Then
    assertEquals("AUTH_003", exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 4: InvalidCredentialsException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `InvalidCredentialsException` with message "Invalid credentials"
2. Assert error code equals "AUTH_004"
3. Assert HTTP status equals `HttpStatus.UNAUTHORIZED` (401)
4. Assert message equals "Invalid credentials"

**Expected Response:**
- Error code: `AUTH_004`
- HTTP status: `401 UNAUTHORIZED`
- Message: "Invalid credentials"

**Test Code:**
```java
@Test
void invalidCredentialsException_shouldHaveCorrectProperties() {
    // Given
    String message = "Invalid credentials";
    
    // When
    InvalidCredentialsException exception = new InvalidCredentialsException(message);
    
    // Then
    assertEquals("AUTH_004", exception.getErrorCode());
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 5: JwtExpiredException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `JwtExpiredException` with message "Token expired"
2. Assert error code equals "JWT_001"
3. Assert HTTP status equals `HttpStatus.UNAUTHORIZED` (401)
4. Assert message equals "Token expired"

**Expected Response:**
- Error code: `JWT_001`
- HTTP status: `401 UNAUTHORIZED`
- Message: "Token expired"

**Test Code:**
```java
@Test
void jwtExpiredException_shouldHaveCorrectProperties() {
    // Given
    String message = "Token expired";
    
    // When
    JwtExpiredException exception = new JwtExpiredException(message);
    
    // Then
    assertEquals("JWT_001", exception.getErrorCode());
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 6: JwtInvalidException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `JwtInvalidException` with message "Invalid token"
2. Assert error code equals "JWT_002"
3. Assert HTTP status equals `HttpStatus.UNAUTHORIZED` (401)
4. Assert message equals "Invalid token"

**Expected Response:**
- Error code: `JWT_002`
- HTTP status: `401 UNAUTHORIZED`
- Message: "Invalid token"

**Test Code:**
```java
@Test
void jwtInvalidException_shouldHaveCorrectProperties() {
    // Given
    String message = "Invalid token";
    
    // When
    JwtInvalidException exception = new JwtInvalidException(message);
    
    // Then
    assertEquals("JWT_002", exception.getErrorCode());
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 7: EmployeeNotFoundException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `EmployeeNotFoundException` with message "Employee with ID 123 not found"
2. Assert error code equals "EMP_001"
3. Assert HTTP status equals `HttpStatus.NOT_FOUND` (404)
4. Assert message equals "Employee with ID 123 not found"

**Expected Response:**
- Error code: `EMP_001`
- HTTP status: `404 NOT_FOUND`
- Message: "Employee with ID 123 not found"

**Test Code:**
```java
@Test
void employeeNotFoundException_shouldHaveCorrectProperties() {
    // Given
    String message = "Employee with ID 123 not found";
    
    // When
    EmployeeNotFoundException exception = new EmployeeNotFoundException(message);
    
    // Then
    assertEquals("EMP_001", exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 8: AddEmployeeException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `AddEmployeeException` with message "Failed to add employee"
2. Assert error code equals "EMP_002"
3. Assert HTTP status equals `HttpStatus.BAD_REQUEST` (400)
4. Assert message equals "Failed to add employee"

**Expected Response:**
- Error code: `EMP_002`
- HTTP status: `400 BAD_REQUEST`
- Message: "Failed to add employee"

**Test Code:**
```java
@Test
void addEmployeeException_shouldHaveCorrectProperties() {
    // Given
    String message = "Failed to add employee";
    
    // When
    AddEmployeeException exception = new AddEmployeeException(message);
    
    // Then
    assertEquals("EMP_002", exception.getErrorCode());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 9: SalaryNotFoundException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `SalaryNotFoundException` with message "Salary not found"
2. Assert error code equals "SAL_001"
3. Assert HTTP status equals `HttpStatus.NOT_FOUND` (404)
4. Assert message equals "Salary not found"

**Expected Response:**
- Error code: `SAL_001`
- HTTP status: `404 NOT_FOUND`
- Message: "Salary not found"

**Test Code:**
```java
@Test
void salaryNotFoundException_shouldHaveCorrectProperties() {
    // Given
    String message = "Salary not found";
    
    // When
    SalaryNotFoundException exception = new SalaryNotFoundException(message);
    
    // Then
    assertEquals("SAL_001", exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

### Test 10: DocumentNotFoundException - Properties
**What to test:** Exception has correct error code and HTTP status

**Steps:**
1. Create new `DocumentNotFoundException` with message "Document not found"
2. Assert error code equals "DOC_001"
3. Assert HTTP status equals `HttpStatus.NOT_FOUND` (404)
4. Assert message equals "Document not found"

**Expected Response:**
- Error code: `DOC_001`
- HTTP status: `404 NOT_FOUND`
- Message: "Document not found"

**Test Code:**
```java
@Test
void documentNotFoundException_shouldHaveCorrectProperties() {
    // Given
    String message = "Document not found";
    
    // When
    DocumentNotFoundException exception = new DocumentNotFoundException(message);
    
    // Then
    assertEquals("DOC_001", exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    assertEquals(message, exception.getMessage());
}
```

---

## Integration Tests - GlobalExceptionHandler

### Test 11: UserAlreadyExistsException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for UserAlreadyExistsException

**Steps:**
1. Mock HTTP request to `/api/auth/register`
2. Throw `UserAlreadyExistsException` with message "Username test already exists"
3. Assert HTTP status is 409
4. Assert response body has errorCode "AUTH_001"
5. Assert response body has status 409
6. Assert response body has error "Conflict"
7. Assert response body has message "Username test already exists"
8. Assert response body has path "/api/auth/register"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 409,
  "error": "Conflict",
  "message": "Username test already exists",
  "errorCode": "AUTH_001",
  "path": "/api/auth/register"
}
```

**Test Code:**
```java
@Test
void handleUserAlreadyExistsException_shouldReturn409WithErrorCode() throws Exception {
    // Given
    when(authService.register(any())).thenThrow(
        new UserAlreadyExistsException("Username test already exists")
    );
    
    // When & Then
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\",\"password\":\"pass123\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.errorCode").value("AUTH_001"))
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("Username test already exists"))
        .andExpect(jsonPath("$.path").value("/api/auth/register"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 12: EmailAlreadyExistsException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for EmailAlreadyExistsException

**Steps:**
1. Mock HTTP request to `/api/auth/register`
2. Throw `EmailAlreadyExistsException` with message "Email test@example.com already exists"
3. Assert HTTP status is 409
4. Assert response body has errorCode "AUTH_002"
5. Assert response body has status 409
6. Assert response body has error "Conflict"
7. Assert response body has message "Email test@example.com already exists"
8. Assert response body has path "/api/auth/register"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 409,
  "error": "Conflict",
  "message": "Email test@example.com already exists",
  "errorCode": "AUTH_002",
  "path": "/api/auth/register"
}
```

**Test Code:**
```java
@Test
void handleEmailAlreadyExistsException_shouldReturn409WithErrorCode() throws Exception {
    // Given
    when(authService.register(any())).thenThrow(
        new EmailAlreadyExistsException("Email test@example.com already exists")
    );
    
    // When & Then
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"test@example.com\",\"password\":\"pass123\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.errorCode").value("AUTH_002"))
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("Email test@example.com already exists"))
        .andExpect(jsonPath("$.path").value("/api/auth/register"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 13: UserNotFoundException - Handler Response with Generic Message
**What to test:** GlobalExceptionHandler returns generic auth message for UserNotFoundException (security)

**Steps:**
1. Mock HTTP request to `/api/auth/login`
2. Throw `UserNotFoundException` with message "User not found"
3. Assert HTTP status is 401
4. Assert response body has generic message "Authentication failed. Invalid username or password."
5. Assert response body has status 401
6. Assert response body has error "Unauthorized"
7. Assert response body has path "/api/auth/login"
8. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication failed. Invalid username or password.",
  "errorCode": "AUTH_003",
  "path": "/api/auth/login"
}
```

**Test Code:**
```java
@Test
void handleUserNotFoundException_shouldReturn401WithGenericMessage() throws Exception {
    // Given
    when(authService.login(any())).thenThrow(
        new UserNotFoundException("User not found")
    );
    
    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\",\"password\":\"pass123\"}"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("Authentication failed. Invalid username or password."))
        .andExpect(jsonPath("$.path").value("/api/auth/login"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 14: InvalidCredentialsException - Handler Response with Generic Message
**What to test:** GlobalExceptionHandler returns generic auth message for InvalidCredentialsException (security)

**Steps:**
1. Mock HTTP request to `/api/auth/login`
2. Throw `InvalidCredentialsException` with message "Invalid password"
3. Assert HTTP status is 401
4. Assert response body has generic message "Authentication failed. Invalid username or password."
5. Assert response body has status 401
6. Assert response body has error "Unauthorized"
7. Assert response body has path "/api/auth/login"
8. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication failed. Invalid username or password.",
  "errorCode": "AUTH_004",
  "path": "/api/auth/login"
}
```

**Test Code:**
```java
@Test
void handleInvalidCredentialsException_shouldReturn401WithGenericMessage() throws Exception {
    // Given
    when(authService.login(any())).thenThrow(
        new InvalidCredentialsException("Invalid password")
    );
    
    // When & Then
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\",\"password\":\"wrongpass\"}"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("Authentication failed. Invalid username or password."))
        .andExpect(jsonPath("$.path").value("/api/auth/login"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 15: JwtExpiredException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for JwtExpiredException

**Steps:**
1. Mock HTTP request to `/api/employees` with expired JWT
2. Throw `JwtExpiredException` with message "Token has expired"
3. Assert HTTP status is 401
4. Assert response body has errorCode "JWT_001"
5. Assert response body has status 401
6. Assert response body has error "Unauthorized"
7. Assert response body has message "Token validation failed. Please login again."
8. Assert response body has path "/api/employees"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token validation failed. Please login again.",
  "errorCode": "JWT_001",
  "path": "/api/employees"
}
```

**Test Code:**
```java
@Test
void handleJwtExpiredException_shouldReturn401WithErrorCode() throws Exception {
    // Given
    doThrow(new JwtExpiredException("Token has expired"))
        .when(jwtFilter).doFilter(any(), any(), any());
    
    // When & Then
    mockMvc.perform(get("/api/employees")
            .header("Authorization", "Bearer expired-token"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errorCode").value("JWT_001"))
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("Token validation failed. Please login again."))
        .andExpect(jsonPath("$.path").value("/api/employees"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 16: JwtInvalidException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for JwtInvalidException

**Steps:**
1. Mock HTTP request to `/api/employees` with invalid JWT
2. Throw `JwtInvalidException` with message "Invalid token format"
3. Assert HTTP status is 401
4. Assert response body has errorCode "JWT_002"
5. Assert response body has status 401
6. Assert response body has error "Unauthorized"
7. Assert response body has message "Token validation failed. Please login again."
8. Assert response body has path "/api/employees"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token validation failed. Please login again.",
  "errorCode": "JWT_002",
  "path": "/api/employees"
}
```

**Test Code:**
```java
@Test
void handleJwtInvalidException_shouldReturn401WithErrorCode() throws Exception {
    // Given
    doThrow(new JwtInvalidException("Invalid token format"))
        .when(jwtFilter).doFilter(any(), any(), any());
    
    // When & Then
    mockMvc.perform(get("/api/employees")
            .header("Authorization", "Bearer invalid-token"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errorCode").value("JWT_002"))
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("Token validation failed. Please login again."))
        .andExpect(jsonPath("$.path").value("/api/employees"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 17: EmployeeNotFoundException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for EmployeeNotFoundException

**Steps:**
1. Mock HTTP request to GET `/api/employees/999`
2. Throw `EmployeeNotFoundException` with message "Employee with ID 999 not found"
3. Assert HTTP status is 404
4. Assert response body has errorCode "EMP_001"
5. Assert response body has status 404
6. Assert response body has error "Not Found"
7. Assert response body has message "Employee with ID 999 not found"
8. Assert response body has path "/api/employees/999"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 404,
  "error": "Not Found",
  "message": "Employee with ID 999 not found",
  "errorCode": "EMP_001",
  "path": "/api/employees/999"
}
```

**Test Code:**
```java
@Test
void handleEmployeeNotFoundException_shouldReturn404WithErrorCode() throws Exception {
    // Given
    when(employeeService.getEmployeeById(999L)).thenThrow(
        new EmployeeNotFoundException("Employee with ID 999 not found")
    );
    
    // When & Then
    mockMvc.perform(get("/api/employees/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("EMP_001"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Employee with ID 999 not found"))
        .andExpect(jsonPath("$.path").value("/api/employees/999"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 18: AddEmployeeException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for AddEmployeeException

**Steps:**
1. Mock HTTP request to POST `/api/employees`
2. Throw `AddEmployeeException` with message "Invalid employee data"
3. Assert HTTP status is 400
4. Assert response body has errorCode "EMP_002"
5. Assert response body has status 400
6. Assert response body has error "Bad Request"
7. Assert response body has message "Invalid employee data"
8. Assert response body has path "/api/employees"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid employee data",
  "errorCode": "EMP_002",
  "path": "/api/employees"
}
```

**Test Code:**
```java
@Test
void handleAddEmployeeException_shouldReturn400WithErrorCode() throws Exception {
    // Given
    when(employeeService.addEmployee(any())).thenThrow(
        new AddEmployeeException("Invalid employee data")
    );
    
    // When & Then
    mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("EMP_002"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Invalid employee data"))
        .andExpect(jsonPath("$.path").value("/api/employees"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 19: SalaryNotFoundException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for SalaryNotFoundException

**Steps:**
1. Mock HTTP request to GET `/api/salaries/999`
2. Throw `SalaryNotFoundException` with message "Salary record not found"
3. Assert HTTP status is 404
4. Assert response body has errorCode "SAL_001"
5. Assert response body has status 404
6. Assert response body has error "Not Found"
7. Assert response body has message "Salary record not found"
8. Assert response body has path "/api/salaries/999"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 404,
  "error": "Not Found",
  "message": "Salary record not found",
  "errorCode": "SAL_001",
  "path": "/api/salaries/999"
}
```

**Test Code:**
```java
@Test
void handleSalaryNotFoundException_shouldReturn404WithErrorCode() throws Exception {
    // Given
    when(salaryService.getSalaryById(999L)).thenThrow(
        new SalaryNotFoundException("Salary record not found")
    );
    
    // When & Then
    mockMvc.perform(get("/api/salaries/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("SAL_001"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Salary record not found"))
        .andExpect(jsonPath("$.path").value("/api/salaries/999"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 20: DocumentNotFoundException - Handler Response
**What to test:** GlobalExceptionHandler returns correct ErrorResponse for DocumentNotFoundException

**Steps:**
1. Mock HTTP request to GET `/api/documents/999`
2. Throw `DocumentNotFoundException` with message "Document not found"
3. Assert HTTP status is 404
4. Assert response body has errorCode "DOC_001"
5. Assert response body has status 404
6. Assert response body has error "Not Found"
7. Assert response body has message "Document not found"
8. Assert response body has path "/api/documents/999"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 404,
  "error": "Not Found",
  "message": "Document not found",
  "errorCode": "DOC_001",
  "path": "/api/documents/999"
}
```

**Test Code:**
```java
@Test
void handleDocumentNotFoundException_shouldReturn404WithErrorCode() throws Exception {
    // Given
    when(documentService.getDocumentById(999L)).thenThrow(
        new DocumentNotFoundException("Document not found")
    );
    
    // When & Then
    mockMvc.perform(get("/api/documents/999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value("DOC_001"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Document not found"))
        .andExpect(jsonPath("$.path").value("/api/documents/999"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 21: Validation Exception - Handler Response
**What to test:** GlobalExceptionHandler handles @Valid validation errors correctly

**Steps:**
1. Mock HTTP request to POST `/api/employees`
2. Send invalid data (empty name, invalid email)
3. Assert HTTP status is 400
4. Assert response body has errorCode "VALIDATION_ERROR"
5. Assert response body has status 400
6. Assert response body has error "Bad Request"
7. Assert response body has message "Validation failed for one or more fields."
8. Assert response body has validationErrors array with field errors
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for one or more fields.",
  "errorCode": "VALIDATION_ERROR",
  "path": "/api/employees",
  "validationErrors": [
    {
      "field": "name",
      "message": "Name is required"
    },
    {
      "field": "email",
      "message": "Email must be valid"
    }
  ]
}
```

**Test Code:**
```java
@Test
void handleValidationException_shouldReturn400WithValidationErrors() throws Exception {
    // When & Then
    mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"\",\"email\":\"invalid\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed for one or more fields."))
        .andExpect(jsonPath("$.path").value("/api/employees"))
        .andExpect(jsonPath("$.validationErrors").isArray())
        .andExpect(jsonPath("$.validationErrors[*].field").exists())
        .andExpect(jsonPath("$.validationErrors[*].message").exists())
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 22: Access Denied Exception - Handler Response
**What to test:** GlobalExceptionHandler handles Spring Security AccessDeniedException

**Steps:**
1. Mock HTTP request to `/api/admin/users`
2. Throw `AccessDeniedException` 
3. Assert HTTP status is 403
4. Assert response body has errorCode "ACCESS_DENIED"
5. Assert response body has status 403
6. Assert response body has error "Forbidden"
7. Assert response body has message "You don't have permission to access this resource."
8. Assert response body has path "/api/admin/users"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 403,
  "error": "Forbidden",
  "message": "You don't have permission to access this resource.",
  "errorCode": "ACCESS_DENIED",
  "path": "/api/admin/users"
}
```

**Test Code:**
```java
@Test
void handleAccessDeniedException_shouldReturn403WithErrorCode() throws Exception {
    // Given
    doThrow(new AccessDeniedException("Access denied"))
        .when(securityFilter).doFilter(any(), any(), any());
    
    // When & Then
    mockMvc.perform(get("/api/admin/users"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value("ACCESS_DENIED"))
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.error").value("Forbidden"))
        .andExpect(jsonPath("$.message").value("You don't have permission to access this resource."))
        .andExpect(jsonPath("$.path").value("/api/admin/users"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

### Test 23: Generic Exception - Handler Response
**What to test:** GlobalExceptionHandler handles unexpected exceptions

**Steps:**
1. Mock HTTP request to GET `/api/employees/1`
2. Throw generic `RuntimeException`
3. Assert HTTP status is 500
4. Assert response body has errorCode "INTERNAL_ERROR"
5. Assert response body has status 500
6. Assert response body has error "Internal Server Error"
7. Assert response body has generic message "An unexpected error occurred. Please try again later."
8. Assert response body has path "/api/employees/1"
9. Assert response body has timestamp

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T...",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred. Please try again later.",
  "errorCode": "INTERNAL_ERROR",
  "path": "/api/employees/1"
}
```

**Test Code:**
```java
@Test
void handleGenericException_shouldReturn500WithErrorCode() throws Exception {
    // Given
    when(employeeService.getEmployeeById(1L)).thenThrow(
        new RuntimeException("Unexpected database error")
    );
    
    // When & Then
    mockMvc.perform(get("/api/employees/1"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.errorCode").value("INTERNAL_ERROR"))
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later."))
        .andExpect(jsonPath("$.path").value("/api/employees/1"))
        .andExpect(jsonPath("$.timestamp").exists());
}
```

---

## Manual Testing with Postman

### Test 24: User Registration - Username Already Exists
**What to test:** Registering with existing username returns 409 with AUTH_001

**Steps:**
1. Open Postman
2. Create POST request to `http://localhost:8080/api/auth/register`
3. Set Content-Type header to `application/json`
4. Set body to:
```json
{
  "username": "existinguser",
  "email": "new@example.com",
  "password": "pass123"
}
```
5. Send request
6. Verify response status is 409
7. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 409,
  "error": "Conflict",
  "message": "Username existinguser is already taken",
  "errorCode": "AUTH_001",
  "path": "/api/auth/register"
}
```

---

### Test 25: User Registration - Email Already Exists
**What to test:** Registering with existing email returns 409 with AUTH_002

**Steps:**
1. Open Postman
2. Create POST request to `http://localhost:8080/api/auth/register`
3. Set Content-Type header to `application/json`
4. Set body to:
```json
{
  "username": "newuser",
  "email": "existing@example.com",
  "password": "pass123"
}
```
5. Send request
6. Verify response status is 409
7. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 409,
  "error": "Conflict",
  "message": "Email existing@example.com is already registered",
  "errorCode": "AUTH_002",
  "path": "/api/auth/register"
}
```

---

### Test 26: User Login - Invalid Credentials
**What to test:** Login with wrong password returns 401 with generic message

**Steps:**
1. Open Postman
2. Create POST request to `http://localhost:8080/api/auth/login`
3. Set Content-Type header to `application/json`
4. Set body to:
```json
{
  "username": "testuser",
  "password": "wrongpassword"
}
```
5. Send request
6. Verify response status is 401
7. Verify response has generic message (no info about which field was wrong)

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication failed. Invalid username or password.",
  "errorCode": "AUTH_004",
  "path": "/api/auth/login"
}
```

---

### Test 27: Get Employee - Not Found
**What to test:** Getting non-existent employee returns 404 with EMP_001

**Steps:**
1. Open Postman
2. Create GET request to `http://localhost:8080/api/employees/999`
3. Add Authorization header with valid JWT token
4. Send request
5. Verify response status is 404
6. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 404,
  "error": "Not Found",
  "message": "Employee with ID 999 not found",
  "errorCode": "EMP_001",
  "path": "/api/employees/999"
}
```

---

### Test 28: Add Employee - Invalid Data
**What to test:** Adding employee with invalid data returns 400 with validation errors

**Steps:**
1. Open Postman
2. Create POST request to `http://localhost:8080/api/employees`
3. Set Content-Type header to `application/json`
4. Add Authorization header with valid JWT token
5. Set body to:
```json
{
  "name": "",
  "email": "invalid-email",
  "salary": -1000
}
```
6. Send request
7. Verify response status is 400
8. Verify response has validation errors array

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for one or more fields.",
  "errorCode": "VALIDATION_ERROR",
  "path": "/api/employees",
  "validationErrors": [
    {
      "field": "name",
      "message": "Name is required"
    },
    {
      "field": "email",
      "message": "Email must be valid"
    },
    {
      "field": "salary",
      "message": "Salary must be positive"
    }
  ]
}
```

---

### Test 29: Get Salary - Not Found
**What to test:** Getting non-existent salary returns 404 with SAL_001

**Steps:**
1. Open Postman
2. Create GET request to `http://localhost:8080/api/salaries/999`
3. Add Authorization header with valid JWT token
4. Send request
5. Verify response status is 404
6. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 404,
  "error": "Not Found",
  "message": "Salary record for employee 999 not found",
  "errorCode": "SAL_001",
  "path": "/api/salaries/999"
}
```

---

### Test 30: Get Document - Not Found
**What to test:** Getting non-existent document returns 404 with DOC_001

**Steps:**
1. Open Postman
2. Create GET request to `http://localhost:8080/api/documents/999`
3. Add Authorization header with valid JWT token
4. Send request
5. Verify response status is 404
6. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 404,
  "error": "Not Found",
  "message": "Document with ID 999 not found",
  "errorCode": "DOC_001",
  "path": "/api/documents/999"
}
```

---

### Test 31: Request with Expired JWT Token
**What to test:** Request with expired token returns 401 with JWT_001

**Steps:**
1. Open Postman
2. Create GET request to `http://localhost:8080/api/employees`
3. Add Authorization header with expired JWT token: `Bearer <expired-token>`
4. Send request
5. Verify response status is 401
6. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token validation failed. Please login again.",
  "errorCode": "JWT_001",
  "path": "/api/employees"
}
```

---

### Test 32: Request with Invalid JWT Token
**What to test:** Request with malformed token returns 401 with JWT_002

**Steps:**
1. Open Postman
2. Create GET request to `http://localhost:8080/api/employees`
3. Add Authorization header with invalid JWT token: `Bearer invalid.token.here`
4. Send request
5. Verify response status is 401
6. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token validation failed. Please login again.",
  "errorCode": "JWT_002",
  "path": "/api/employees"
}
```

---

### Test 33: Access Admin Endpoint Without Permission
**What to test:** Accessing admin endpoint with user role returns 403

**Steps:**
1. Open Postman
2. Create GET request to `http://localhost:8080/api/admin/users`
3. Add Authorization header with valid JWT token (user role, not admin)
4. Send request
5. Verify response status is 403
6. Verify response body

**Expected Response:**
```json
{
  "timestamp": "2025-01-31T10:30:45.123",
  "status": 403,
  "error": "Forbidden",
  "message": "You don't have permission to access this resource.",
  "errorCode": "ACCESS_DENIED",
  "path": "/api/admin/users"
}
```

---

## Test Coverage Summary

### Total Tests: 33

**Unit Tests (Exception Classes):** 10
- All custom exceptions tested for correct properties
- Error codes verified
- HTTP status codes verified

**Integration Tests (GlobalExceptionHandler):** 13
- All exception types tested through handler
- ErrorResponse format verified
- HTTP status codes verified
- Error codes verified
- Generic security messages verified

**Manual Tests (Postman):** 10
- Real API endpoint testing
- Complete request/response flow
- All error codes covered
- Security scenarios tested

### Coverage by Error Code:
- ✅ AUTH_001 (UserAlreadyExistsException)
- ✅ AUTH_002 (EmailAlreadyExistsException)
- ✅ AUTH_003 (UserNotFoundException)
- ✅ AUTH_004 (InvalidCredentialsException)
- ✅ JWT_001 (JwtExpiredException)
- ✅ JWT_002 (JwtInvalidException)
- ✅ EMP_001 (EmployeeNotFoundException)
- ✅ EMP_002 (AddEmployeeException)
- ✅ SAL_001 (SalaryNotFoundException)
- ✅ DOC_001 (DocumentNotFoundException)
- ✅ VALIDATION_ERROR (MethodArgumentNotValidException)
- ✅ ACCESS_DENIED (AccessDeniedException)
- ✅ INTERNAL_ERROR (Generic Exception)

### Coverage by HTTP Status:
- ✅ 400 Bad Request (AddEmployeeException, Validation)
- ✅ 401 Unauthorized (Auth failures, JWT errors)
- ✅ 403 Forbidden (AccessDenied)
- ✅ 404 Not Found (Employee, Salary, Document, User)
- ✅ 409 Conflict (User/Email already exists)
- ✅ 500 Internal Server Error (Generic exception)
