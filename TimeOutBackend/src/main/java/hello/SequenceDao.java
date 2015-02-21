package hello;

public interface SequenceDao {

	long getNextSequenceId(String key) throws SequenceException;

}