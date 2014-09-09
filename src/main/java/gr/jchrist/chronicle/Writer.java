package gr.jchrist.chronicle;

import java.util.List;

/**
 * @author JChrist
 *         09/09/2014
 */
public interface Writer {
	void write(Message m) throws Exception;
	void write(List<Message> msgs) throws Exception;
	void reset() throws Exception;
}
