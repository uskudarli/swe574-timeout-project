package hello;

/**
 * Created by Hakan on 19/02/2015.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class PersonDaoImpl implements PersonDao {

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public void save(Person person) {

        mongoOperation.save(person);

    }

}