# Bank Management REST API

## Description
This project is a RESTful API for managing a bank's operations. It includes functionalities for managing customer accounts, handling transactions, and maintaining account balances. It is built using Java and Spring Boot.

## Features
  * Create, Read, Update, and Delete (CRUD) operations for accounts.
  * Transfer money between accounts.
  * Check account balance.
  * Retrieve transaction history.
  * Validation for transactions (e.g., sufficient balance).
  * Exception handling for invalid operations.
  * Using JWT Authentication for security.
  * Email notifications using Java Mail:
    * Debit/Credit: Sends an email to the user with transaction details.
    * Login / New Account: Notifies the user of login / new account activity.
    * Transfer Between Accounts: Confirms transfer details via email to both parties.
    * Insufficient Balance: Alerts the user when a transaction fails due to insufficient funds.
    
## Technologies Used
  * Backend Framework: *Spring Boot*
  * Database: *MySQL / PostgreSQL (or specify the database you used)*
  * ORM Tool: *Hibernate*
  * Build Tool: *Maven*
  * Language: *Java 17 (or the version you used)*

## Setup Instructions
This section describes how to set up and run the Bank Management API.

### Prerequisites

*   **Java Development Kit (JDK):** [Download JDK](https://www.oracle.com/java/technologies/downloads/)
*   **Maven or Gradle:** [Download Maven](https://maven.apache.org/download.cgi)
*   **MySQL/PostgreSQL:** Install and have a running instance of your chosen database.

### Steps

1.  **Clone the repository:**

    ```bash
    git clone [https://github.com/yourusername/bank-management-api.git](https://github.com/sagarboyal/Banking-Application.git)
    ```

2.  **Navigate to the project directory:**

    ```bash
    cd bank-management-api
    ```

3.  **Configure the database connection:**

    Open the `application.properties` file (located in `src/main/resources`) and update the following properties:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/bank_db  # Update if using PostgreSQL or different settings
    spring.datasource.username=root                     # Your database username
    spring.datasource.password=yourpassword              # Your database password
    spring.jpa.hibernate.ddl-auto=update             # Automatically update database schema (optional but recommended)
    ```
4. **Java Mail Configuration:**

    This section describes how to configure Java Mail for sending email notifications. This functionality is optional and requires additional setup.
    To enable email notifications, you need to add the following properties to your `application.properties` file:

    ```properties
    spring.mail.host=smtp.example.com  # Replace with your SMTP server address
    spring.mail.port=587                 # Replace with your SMTP server port (common: 587)
    spring.mail.username=your-email@example.com  # Replace with your email address
    spring.mail.password=your-email-password  # Replace with your email password
    spring.mail.properties.mail.smtp.auth=true  # Enable SMTP authentication
    spring.mail.properties.mail.smtp.starttls.enable=true  # Enable STARTTLS for secure communication
    ```

5. ** JWT Configuration (Security Sensitive):**

  This section describes how to configure JSON Web Tokens (JWT) for this project. JWT is used for user authentication and authorization. **Handling the JWT secret is crucial for security.**
 
  **Configuration File:**
  JWT settings are configured in the `application.properties` (or `application.yml` if you're using YAML) file.

  **Properties:**

*   **Secret Key (`app.jwt.secret`):** This is the **most critical** piece of your JWT setup. It's used to digitally sign the tokens. **Never commit this value directly to your version control (Git).**
    *   **Example (Do NOT use this in production!):**
        ```properties
        app.jwt.secret=THIS_IS_A_PLACEHOLDER_REPLACE_WITH_A_STRONG_SECRET
        ```
    *   **Generating a Strong Secret:** You can generate a strong secret using various methods. Here's an example using a Java command (you can adapt this to other languages):
        ```bash
        java -jar /path/to/your/preferred/uuid/generator.jar # Replace with the actual path
        #Or using openssl:
        openssl rand -base64 32
        ```
*   **Token Expiration Time (`app.jwt.expiration`):** This determines how long a JWT is valid (in milliseconds). The default is often 1 hour (3600000 ms), but you can adjust it based on your security needs.
    *   **Example:**
        ```properties
        app.jwt.expiration=3600000 # 1 hour
        ```

  **Critical Security Practices:**

  *   **Never Hardcode the Secret in Source Code:** Absolutely avoid putting the JWT secret directly into your `application.properties` or any other configuration file that is committed to your repository.
  *   **Use Environment Variables:** In production and ideally in development as well, store the secret as an environment variable on your server or development machine. Your application can then read it from           the environment.
  *   **Secure Configuration Management (Recommended for Production):** For production environments, consider using a dedicated secrets management tool like:
      *   **HashiCorp Vault:** A popular open-source option.
      *   **AWS Secrets Manager:** For applications hosted on AWS.
      *   **Azure Key Vault:** For Azure-based applications.
      *   **Google Cloud Secret Manager:** For Google Cloud Platform.
  *   **Secret Rotation:** Regularly rotate your JWT secret to further enhance security. This limits the impact of a potential compromise.

  **Example of Accessing the Secret from an Environment Variable (in Spring Boot):**

  In your Spring Boot application, you can access the secret like this:

  ```java
  @Value("${app.jwt.secret}")
  private String jwtSecret;
  ```

7.  **Build the project:**

    ```bash
    mvn clean install
    ```

8.  **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

9.  **Access the API:**

    The API will be accessible at `http://localhost:8080/api/user/login`.


## Security
  This API is secured with JWT (JSON Web Token) authentication. Only authenticated users can access most endpoints. Unauthorized requests will result in a 403 Forbidden response.
  
## Authentication Flow

This section describes the authentication flow for your API, which uses JWT (JSON Web Tokens) for secure access to protected resources.

### Sign Up (User Registration)

To create a new user account:

*   **Endpoint:** `POST [register](http://localhost:8080/api/user/register)`
*   **Request Body (JSON):**

    ```json
    {
      "firstName": "Sagar",
      "lastName": "Boyal",
      "otherName": "",
      "gender": "Male",
      "address": "Haldia",
      "stateOfOrigin": "West Bengal",
      "accountNumber": "5678901234",
      "email": "xyz@gmail.com",
      "password":"12345",
      "role":"ADMIN",
      "phoneNumber": "+91-8877665544",
      "alternativePhoneNumber": "+91-8877665544"
    }
    ```

*   **Response (JSON - Success):**

    ```json
    {
        "responseCode": "002",
        "responseMessage": "Account has been successfully created!",
        "accountInfo": {
            "accountName": "Sagar Boyal ",
            "accountBalance": 0,
            "accountNumber": "2025796229"
        }
    }
    ```

*   **Response (JSON - Error - Example: Username already exists):**

    ```json
    {
        "responseCode": "001",
        "responseMessage": "This user already has an account!",
        "accountInfo": null
    }
    ```

### Login (Token Generation)

To obtain a JWT token:

*   **Endpoint:** `POST [login](http://localhost:8080/api/user/login)`
*   **Request Body (JSON):**

    ```json
    {
      "username": "user123",
      "password": "securepassword" // In a real application, passwords should be hashed on the client-side before sending.
    }
    ```

*   **Response (JSON - Success):**

    ```json
        {
        "responseCode": "201",
        "responseMessage": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZXJ2ZXIubm90aWZpY2F0aW9ucy5zeXN0ZW1AZ21haWwuY29tIiwiaWF0IjoxNzM2Nzc5ODk1LCJleHAiOjE3MzY3ODM0OTV9.AAKc9hNG8dGPwPaxXip0j3nSnU_VjyiM6JuIhIfjQfk",
        "accountInfo": null
    }
    ```

### Using the Token (Authorization)

Once you have obtained a JWT token from the login endpoint, you must include it in the `Authorization` header of your requests to access protected resources.

*   **Header:** `Authorization`
*   **Value:** `Bearer <your-token>`


# API Endpoints 

**Authentication:** All endpoints in this API require JWT (JSON Web Token) authentication. You must include a valid JWT in the `Authorization` header of your requests using the `Bearer <your_jwt_token>` format.

### User Authentication

*   `POST /api/user/login`: User login (to obtain a JWT).
*   `POST /api/user/register`: User registration.

### User Account Information

*   `GET /api/user/balance-enquiry`: Check account balance.
*   `GET /api/user/name-enquiry`: Check account holder's name.

### User Transactions

*   `POST /api/user/debit`: Debit from account.
*   `POST /api/user/credit`: Credit to account.
*   `POST /api/user/transfer`: Transfer funds to another account.

### Transactions

*   `GET /api/transaction/bank-statement`: Retrieve bank statement.

### Example Usage

```bash
curl -H "Authorization: Bearer <your_jwt_token>" http://localhost:8080/api/user/balance-enquiry
