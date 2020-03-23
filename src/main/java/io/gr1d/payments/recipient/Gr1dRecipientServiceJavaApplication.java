package io.gr1d.payments.recipient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ComponentScan("io.gr1d")
public class Gr1dRecipientServiceJavaApplication {

	public static void main(final String[] args) {
		SpringApplication.run(Gr1dRecipientServiceJavaApplication.class, args);
	}

}
