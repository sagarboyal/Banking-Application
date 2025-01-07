package com.main.application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The SBI Clone Banking API",
				description = "Backend Rest API for banking",
				version = "v1.0",
				contact = @Contact(
						name = "Sagar Boyal",
						email = "sagarboyal.024@gmail.com",
						url = "https://www.linkedin.com/in/sagarboyal/"
				),
				license = @License(
						name = "Github Link",
						url = "https://github.com/sagarboyal/Banking-Application"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "This project is a Bank Management System API developed using Java and Spring Boot. It allows users to perform various banking operations such as account creation, money transfers, balance checks, and transaction history. The system provides a set of RESTful APIs to manage banking transactions securely and efficiently, with features like email notifications for debits and credits. The backend is built using Spring Boot, while the data is stored and accessed through a simple, relational database.",
				url = "https://github.com/sagarboyal/Banking-Application"
		)
)
public class BankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}
}
