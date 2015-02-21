package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonBoImpl implements PersonBo {

	private static final String HOSTING_SEQ_KEY = "hosting";

	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private PersonDao hostingDao;

	@Override
	public void save(String name) throws SequenceException {

		Person hosting = new Person();

		hosting.setId(String.valueOf(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY)));
		hosting.setFirstName(name);
		hostingDao.save(hosting);

		System.out.println(hosting);

	}

}