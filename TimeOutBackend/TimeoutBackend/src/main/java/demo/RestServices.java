package demo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import common.DBUtility;

@RestController
public class RestServices {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/register")
      @ResponseBody
      public boolean registerUser(@RequestParam(value="userName") String userName,
                                  @RequestParam(value="password") String password) {
        Map<String, String> properties = DBUtility.putProperties();

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
        Map<String, String> properties = DBUtility.putProperties();

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
