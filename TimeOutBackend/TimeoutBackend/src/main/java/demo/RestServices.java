package demo;

import com.google.appengine.api.utils.SystemProperty;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RestServices {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/register")
      @ResponseBody
      public boolean registerUser(@RequestParam(value="userName") String userName,
                                  @RequestParam(value="password") String password) {
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
        em.persist(new User(userName, password));
        em.getTransaction().commit();
        em.close();
        return true;
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public boolean login(@RequestParam(value="userName") String userName,
                                @RequestParam(value="password") String password) {
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
        List<User> result = em
                .createQuery("FROM User")
                .getResultList();
        for (User g : result) {
            if(g.getUserName().equals(userName)){
                if(g.getPassword().equals(password)){
                    return true;
                }
            }
        }

        em.close();

        return false;

    }
}
