# Mini-Aspire Loan Management System

## Features
1. **Loan Application:**
    - Authenticated users can submit loan requests specifying the amount and term.
    - Scheduled repayments are generated automatically.

2. **Admin Loan Approval:**
    - Admins can review and approve or reject loan requests.
    - Loan status updates automatically based on approval or rejection.

3. **Customer Loan Views:**
    - Authenticated customers can view their approved loans.
    - They can also view their repayment details, including future and past repayments.

4. **Loan Repayments:**
    - Customers can make repayments on their approved loans.
    - Repayment status updates automatically.

## Assumptions
Assumptions:
- Username of user is unique, it is not system generated.
- Customer doesn't need to strictly pay all the terms. He could pay more in less terms.
- Customer can pay any amount within the constraints: Amount Paid in each installment >= Weekly Installment decided initially and if at end we have remaining amount < Weekly Installment decided initially then he can pay only the remaining amount.
- We don't remove the remaining repayments which are pending but just close out the loan by marking as `PAID`

## Versions used
Please make sure to have the following version on your machine for the project to run smoothly
- Java: 17
- Amazon Jdk: 17
- Gradle: 8.5
- Additionally, if required, I have used Ubuntu 20.04.6 LTS as my OS on an Intel i5 processor

## Steps to Run
1. Clone the repository: `git clone <repository-url>`
2. Navigate to the project directory: `cd mini`
3. Build the project: `gradle build`
4. Run the Spring Boot application: `gradle bootJar`

## How to use the program
Base URL of program is: `http://localhost:8080`
1. There are 2 users hard coded in the internal volatile database. You can use them for your testing. One is: `{"username": "aspire", "password": "123", "email": "admin@aspire.com"}`
2. First login using the username and password on the endpoint: `http://localhost:8080/mini-aspire/api/v1/auth/login`. You'll get an accessToken, use this for all steps ahead as Bearer Token.
3. You can create a loan by using the endpoint: `http://localhost:8080/mini-aspire/api/v1/loan`. Sample request:
```json
{
   "loanAmount": 6000,
   "loanTerm": 3
}
```
4. One of the many ways to see loan and other details is through this endpoint: `http://localhost:8080/mini-aspire/api/v1/loan/1`. This will give you all the details of repayments, loans and your own user. Notice the userId as path parameter.
```json
[
   {
      "loadId": 1,
      "loanStatus": "APPROVED",
      "loanRepaymentResponseDTOs": [
         {
            "repaymentDate": "11-03-2024",
            "repaymentAmount": 4000.0,
            "status": "PAID"
         },
         {
            "repaymentDate": "18-03-2024",
            "repaymentAmount": 4000.0,
            "status": "PENDING"
         }
      ]
   },
   {
      "loadId": 2,
      "loanStatus": "REJECTED",
      "loanRepaymentResponseDTOs": [
         {
            "repaymentDate": "11-03-2024",
            "repaymentAmount": 3000.0,
            "status": "PENDING"
         },
         {
            "repaymentDate": "18-03-2024",
            "repaymentAmount": 3000.0,
            "status": "PENDING"
         },
         {
            "repaymentDate": "25-03-2024",
            "repaymentAmount": 3000.0,
            "status": "PENDING"
         }
      ]
   }
]
```
5. You can approve the loan yourself as of now using API: `http://localhost:8080/mini-aspire/api/v1/admin`. Sample request is:
```json
{
   "loanId": 1,
   "approved": true
}
```
6. You can pay for a loan repayment using this: `http://localhost:8080/mini-aspire/api/v1/repayment`. Request looks like this:
```json
{
    "loanId": 1,
    "amount": 2000.0
}
```
6. Doing this 3 times in total (payments) will mark the loan as **PAID**.
7. Please follow the steps as given with values to avoid any confusion in request/response.

## Design Considerations and Rationale

### Modular Architecture
I structured the application with a modular architecture to promote separation of concerns and maintainability.  
Each component, such as services, controllers, and repositories, is responsible for a specific functionality.
This approach allows for easier testing, debugging, and scalability.  
The packaging structure is child of my experience with Microservices. The packages are very loosely coupled with each other thus making it easier to test independently. If required the packages can themselves be directly moved to separate repos.

### Spring Boot and Spring Framework
Leveraged the Spring Boot framework to streamline application setup, configuration, and development. Spring's dependency injection and inversion of control help manage component dependencies and ensure loose coupling between different parts of the application.

### SOLID Principles
Adhered to the SOLID principles of object-oriented design to enhance the code's maintainability and extensibility:
- **Single Responsibility Principle (SRP):** Each class has a single responsibility, making it easier to manage and modify.
- **Open/Closed Principle (OCP):** The code is open for extension but closed for modification, achieved through interfaces and inheritance.
- **Liskov Substitution Principle (LSP):** Subtypes can be substituted for their base types, ensuring proper inheritance relationships.
- **Interface Segregation Principle (ISP):** Interfaces are kept focused and small to ensure that implementing classes only have relevant methods.
- **Dependency Inversion Principle (DIP):** High-level modules are not dependent on low-level modules, both depend on abstractions, fostering flexibility.

### Implementation Patterns
- Repository Pattern: Encapsulates data access, simplifying database interactions.
- DTO Pattern: Uses Data Transfer Objects to separate the external data transfer interface from internal entity representations.
- Exception Handling: Custom exceptions provide meaningful error information, aiding in debugging and user feedback.

### Technology Choices

- Spring Framework: Facilitates dependency management and offers comprehensive infrastructure support.
- Java Enums: Enhance readability and reduce errors by representing constant values.
- Unit Testing: Ensures reliability through extensive test coverage.

### Security 
JWT-based authentication secures user sessions, with future enhancements planned for comprehensive authorization controls.

## Enhancements

The current implementation lays the groundwork for a robust loan management system. Future developments could include improved edge case handling, enhanced repayment scheduling, comprehensive API documentation, and additional features like pagination and detailed logging for improved user experience and system management.

