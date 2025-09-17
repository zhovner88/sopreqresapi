# ReqRes API Test Framework

API testing framework for [ReqRes.in](https://reqres.in/) REST API using Java, TestNG, and RestAssured.

## Technologies Used

- **Java 17** - Programming language
- **Gradle 8+** - Build tool and dependency management
- **TestNG** - Testing framework with parallel execution support
- **RestAssured** - REST API testing library
- **Jackson** - JSON serialization/deserialization
- **Lombok** - Code generation for DTOs
- **Allure** - Test reporting framework
- **Hamcrest** - Assertion matchers

## Project Structure

```
api-tests/
├── src/main/java/com/reqres/api/
│   ├── model/           # Data Transfer Objects (DTOs)
│   ├── services/        # API service layer
│   └── conditions/      # Custom assertion conditions
├── src/test/java/com/reqres/tests/
│   ├── LoginTests.java        # Scenario 1: Login authentication
│   ├── UserTests.java         # Scenario 2: User details retrieval
│   ├── RegisterTests.java     # Scenario 3: User registration
│   ├── PaginationTests.java   # Scenario 4: Pagination validation
│   ├── DelayTests.java        # Scenario 5: Response delay testing
│   └── ChainedRequestTests.java # Scenario 6: Chained user requests
└── build.gradle
```

## Running Tests

### Command Line (Gradle)

Run all tests:
```bash
./gradlew test
```

Run specific test class:
```bash
./gradlew test --tests "com.reqres.tests.LoginTests"
```

### IDE Integration

Import the project as a Gradle project in your IDE (IntelliJ IDEA, VSCode) and run tests directly from the IDE.

## Test Reports

### HTML Reports
- **Gradle HTML Report**: `api-tests/build/reports/tests/test/index.html`
- **Allure Report**: Generate with `./gradlew allureReport` then open `api-tests/build/reports/allure-report/index.html`

### Console Output
Test results and detailed logging are available in the console during test execution.

## Test Scenarios Coverage

| Scenario | Test File | Description |
|----------|-----------|-------------|
| 1 | `LoginTests.java` | User authentication with valid/invalid credentials and edge cases |
| 2 | `UserTests.java` | User details retrieval and data structure validation |
| 3 | `RegisterTests.java` | User registration POST requests |
| 4 | `PaginationTests.java` | Pagination handling, data integrity across pages |
| 5 | `DelayTests.java` | Response delay validation and performance testing |
| 6 | `ChainedRequestTests.java` | Chained requests for user list and specific user details |

## Configuration

### Test Configuration
- Parallel execution enabled
- Request/response logging
- Allure reporting integration
- Custom assertion conditions

## Key Features

- Template Method Pattern for request setup
- Generic user search across all pages with field selectors
- Fluent API for readable test assertions
- Parametrized tests with TestNG DataProvider
- Separation of concerns (DTOs, Services, Tests)
- Validation including timing, data integrity, and edge cases

## Authentication (Theory)

If the token returned was meant to be used for authentication, structure tests to:

- **Store and reuse tokens securely**
  - Introduce global variable for token storage in BaseApiService.java
  - Introduce SaveToken() method that extracts token from response and stores it
  - Environment variables can be used for token storage or property files

- **Add auth headers automatically**
  - Introduce UpdateAuthenticationToken() for updating token manually
  - Flow: Login without token → Get token from response and save → modify request with token
  - Modify setUpRequest() for adding Authorization header if necessary

- **Handle token expiration or reuse**
  - Introduce UpdateAuthToken() method that updates Authentication token with valid, not expired token
  - Introduce method for checking for token expiration and refreshing it automatically (if 401 - refresh)
