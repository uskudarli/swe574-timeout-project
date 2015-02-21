
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
@Import({ MongoConfig.class })
@EnableAutoConfiguration

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
