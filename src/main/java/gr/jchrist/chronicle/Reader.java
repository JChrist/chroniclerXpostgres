package gr.jchrist.chronicle;

import java.util.List;

/**
 * @author JChrist
 *         09/09/2014
 */
public interface Reader {
	List<Message> read(int count) throws Exception;
	Message read() throws Exception;
}
