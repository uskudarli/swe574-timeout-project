package hello;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({ "com.mkyong.config", "com.mkyong.seq", "com.mkyong.hosting" })
@Import({ MongoConfig.class })
public class AppConfig {

}