package demo;

import java.util.Date;
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

        // Insert a few rows.
        EntityManager em = DBUtility.startTranscation();
        em.persist(new User(userName, password));
        DBUtility.commitTransaction(em);

        return true;
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public boolean login(@RequestParam(value="userName") String userName,
                                @RequestParam(value="password") String password) {
        EntityManager em = DBUtility.startTranscation();
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

        DBUtility.commitTransaction(em);

        return false;

    }

    @RequestMapping("/test")
    public boolean test() {
        Map<String, String> properties = DBUtility.putProperties();

        EntityManager em = DBUtility.startTranscation();

        em.persist(new Greeting("user", new Date(), "Hello!"));
        em.persist(new Greeting("user", new Date(), "Hi!"));

        DBUtility.commitTransaction(em);

        return true;
    }
}
