package gr.jchrist.chronicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JChrist
 *         04/09/2014
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	public static final int MESSAGES_NO = 100_000;

	public static List<Message> generateMessages() {
		return generateMessages(MESSAGES_NO);
	}

	public static List<Message> generateMessages(final int no) {
		Instant start = Instant.now();
		List<Message> msgs = new ArrayList<>();
		for(int i=0; i<no; i++) {
			Message m = new Message(i, "jchrist"+i, "jchrist"+i, "sample test message id:"+i );
			msgs.add(m);
		}
		Instant end = Instant.now();

		logger.info("Generated {} msgs in {} ms.", no, Duration.between(start, end).toMillis());
		return msgs;
	}

	public static void writeMessages(Writer w, List<Message> msgs) throws Exception {
		logger.info("Resetting {}", w);
		w.reset();

		logger.info("Done resetting {}, starting writing {} msgs", w, msgs.size());

		Instant start = Instant.now();
		w.write(msgs);
		Instant end = Instant.now();

		logger.info("Writer:{} took {} ms dst write {} messages",w, Duration.between(start, end).toMillis(), msgs.size());
	}

	public static void main(String[] args) throws Exception {
		List<Message> msgs = generateMessages();

		logger.info("creating databaser");
		Databaser db = new Databaser();

		logger.info("Warming up Databaser");
		writeMessages(db, msgs);

		logger.info("TESTING Databaser");
		writeMessages(db, msgs);

		logger.info("stopping Databaser");
		db.stop();

		logger.info("creating Chronicler");
		Chronicler chronicler = new Chronicler();

		logger.info("Warming up Chronicler");
		writeMessages(chronicler, msgs);

		logger.info("TESTING chronicler");
		writeMessages(chronicler, msgs);
	}
}
