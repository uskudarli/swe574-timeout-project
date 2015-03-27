package demo;

import com.google.appengine.api.utils.SystemProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
@RestController
@SpringBootApplication
public class Application {
	
	@Value("${info.version}")
	private String version;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @RequestMapping("/")
    public void home() {
        Map<String, String> properties = new HashMap();
        if (SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production) {
            properties.put("javax.persistence.jdbc.driver",
                    "com.mysql.jdbc.GoogleDriver");
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:google:mysql://vernal-day-88222:instance2/demo?user=root");
        } else {
            properties.put("javax.persistence.jdbc.driver",
                    "com.mysql.jdbc.Driver");
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:mysql://localhost:3306/demo?user=root?password=password");
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                "Demo", properties);

        // Insert a few rows.
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(new Greeting("user", new Date(), "Hello!"));
        em.persist(new Greeting("user", new Date(), "Hi!"));

        em.getTransaction().commit();
        em.close();
    }

    @RequestMapping("/version")
    public String getVersion() {
        Map<String, String> properties = new HashMap();

        if (SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production) {
            properties.put("javax.persistence.jdbc.driver",
                    "com.mysql.jdbc.GoogleDriver");
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:google:mysql://vernal-day-88222:instance2/demo?user=root");
        } else {
            properties.put("javax.persistence.jdbc.driver",
                    "com.mysql.jdbc.Driver");
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:mysql://localhost:3306/demo?user=root?password=password");
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                "Demo", properties);

        // Insert a few rows.
        EntityManager em = emf.createEntityManager();
        em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Greeting> result = em
                .createQuery("FROM Greeting")
                .getResultList();
        for (Greeting g : result) {
            version = g.getAuthor();
        }
        em.getTransaction().commit();
        em.close();
        return version;
    }
    

    @RequestMapping("/test")
    public boolean test() {

        return true;
    }

    @RequestMapping(value="/asd1/{name}", method=RequestMethod.GET)
    public String showForm(@PathVariable String name) {
        return "form";
    }

}
