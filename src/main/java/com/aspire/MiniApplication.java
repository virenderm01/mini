package com.aspire;

import com.aspire.auth.constants.Role;
import com.aspire.auth.model.UserEntity;
import com.aspire.auth.repository.UserRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = {"com.aspire.auth", "com.aspire.loan", "com.aspire.common"})
@EntityScan(basePackages = {"com.aspire.auth.model", "com.aspire.loan.model"})
@EnableJpaRepositories(basePackages = {"com.aspire.auth.repository", "com.aspire.loan.repository"})
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot REST API Documentation",
				description = "Spring Boot REST API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Virender Moudgil",
						email = "virenderm01@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"

				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Aspire Loan Management Documentation"
		)
)
public class MiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniApplication.class, args);
	}
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * This initialises our Database
	 * Used for testing our system
	 */
	@PostConstruct
	public void initUsers() {
		List<UserEntity> users = Stream.of(
				new UserEntity(null, "test@aspire.com", "test", passwordEncoder.encode( "123"), Role.USER, true),
				new UserEntity(null, "aspire@aspire.com", "aspire", passwordEncoder.encode("123"), Role.ADMIN, true)

		).collect(Collectors.toList());
		userRepository.saveAll(users);
	}


}
