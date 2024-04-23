<h1>Cinema API</h1>

<par> I created an application similar to IMDb that allows users to view movies and their actors, post reviews and ratings, among other features. The primary goal of this project was to enhance my knowledge.

<h3>Class and Use-case Diagrams</h3>

![class-usecase drawio](https://github.com/vladradu21/2024-Nagarro-AF/assets/117584846/2ed1514c-a811-45c0-a087-d3897689d501)


<h3>Conceptual and Deployment Diagrams</h3>

<par> It was built using Spring Boot, employing Data JPA for persistence, Flyway for migration management, and Spring Security 6 with JWT for security measures.

<par> For deployment, Docker was used, involving two images configured with specific containers and port forwarding.

![conceptual-deployment](https://github.com/vladradu21/2024-Nagarro-AF/assets/117584846/787a06fc-3580-4cf7-a665-f4370d1eabd6)

<h3>Spring Security 6 with JWT</h3>

<par> This diagram illustrates the security workflow.

![spring-security](https://github.com/vladradu21/2024-Nagarro-AF/assets/117584846/ed5d1d81-caec-4e2d-9283-d0f547c9ecac)

<h3>Miscellaneous</h3>

A set of public and private keys were implemented for security; within the microservices architecture, only the user management microservice holds the private key, while the other microservices obtain the public key to validate tokens.

Global exception handling is included, which prevents the application from encountering errors due to incorrect requests.

Flyway is used for managing database migrations and for inserting data into nomenclature tables.

Mapstruct is utilized to map the DTOs to the models in the service layer.

To ensure reliability, I performed unit testing and utilized Testcontainers for integration testing, along with implementing logging mechanisms and various other tools.

![image](https://github.com/vladradu21/2024-Nagarro-AF/assets/117584846/995e9030-29fb-4060-a2fe-b039e930b3bd)
