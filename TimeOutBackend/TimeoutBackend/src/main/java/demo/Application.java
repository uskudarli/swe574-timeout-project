package demo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import common.DBUtility;

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
        Map<String, String> properties = DBUtility.putProperties();

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
        Map<String, String> properties = DBUtility.putProperties();

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
