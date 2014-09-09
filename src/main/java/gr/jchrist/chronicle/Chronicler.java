package gr.jchrist.chronicle;

import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;
import net.openhft.chronicle.IndexedChronicle;
import net.openhft.chronicle.tools.ChronicleTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JChrist
 *         04/09/2014
 */
public class Chronicler implements Reader, Writer {
	public static final String BasePath = new File("").getAbsolutePath() + "/Chronicle";

	protected IndexedChronicle chronicle;

	public Chronicler() throws IOException {
		ChronicleTools.deleteOnExit(BasePath);
		chronicle = new IndexedChronicle(BasePath);
	}

	public void write(List<Message> msgs) throws Exception {
		for(Message m : msgs) {
			write(m);
		}
	}

	public void write(Message msg) throws Exception {
		ExcerptAppender appender = chronicle.createAppender();
		appender.startExcerpt(1_000_000);
		appender.writeObject(msg);
		appender.finish();
	}

	public List<Message> read(int count) throws Exception {
		List<Message> res = new ArrayList<>();
		for(int i=0; i<count; i++) {
			res.add(read());
		}

		return res;
	}

	public Message read() throws Exception {
		ExcerptTailer reader = chronicle.createTailer();
		Object o = reader.readObject();
		reader.finish();
		return (Message) o;
	}

	public void reset() throws Exception {
	}

	public String toString() {
		return "Chronicler";
	}
}
