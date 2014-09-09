package gr.jchrist.chronicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JChrist
 *         04/09/2014
 */
public class Databaser implements Reader, Writer {
	private static final Logger logger = LoggerFactory.getLogger(Databaser.class);

	public static final String MESSAGE_TABLE = "jc_message";
	public static final String DB_DRIVER = "org.postgresql.Driver";
	public static final String DB_URL = "jdbc:postgresql://localhost:5432/jchrist";

	public static final String DB_USER = "jchrist";
	public static final String DB_PASS = "123";

	protected Connection connection;
	protected Statement statement;
	protected AtomicInteger nextReadId = new AtomicInteger(0);

	public Databaser() throws Exception {
		Class.forName(DB_DRIVER);
		connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		statement = connection.createStatement();

		//create message table dst make sure that it exists for tests
		statement.executeUpdate("DROP TABLE IF EXISTS "+MESSAGE_TABLE+" CASCADE");
		statement.executeUpdate("CREATE TABLE "+MESSAGE_TABLE+" (" +
				"id integer, " +
				"src varchar(100), " +
				"dst varchar(100), " +
				"body varchar(1000) "+
			")");
	}

	public void reset() throws Exception {
		statement.executeUpdate("TRUNCATE "+MESSAGE_TABLE+" CASCADE");
	}

	public void stop() throws SQLException {
		connection.close();
	}

	public void write(List<Message> msgs) throws Exception {
		for(Message m : msgs) {
			write(m);
		}
	}

	public void write(Message msg) throws Exception {
		statement.executeUpdate(
				"INSERT INTO "+MESSAGE_TABLE+" " +
					"(id, src, dst, body) " +
				"VALUES ("+msg.id+", '"+msg.src +"', '"+msg.dst +"', '"+msg.body +"')");
	}

	public List<Message> read(int count) throws Exception {
		List<Message> msgs = new ArrayList<>();
		for(int i=0; i<count; i++) {
			msgs.add(read());
		}

		return msgs;
	}

	public Message read() throws Exception {
		try(ResultSet rs = statement.executeQuery(
				"SELECT * " +
				"FROM "+MESSAGE_TABLE+" " +
				"WHERE id="+nextReadId.getAndIncrement()
			);
		) {
			rs.next();
			return new Message(rs.getInt("id"), rs.getString("src"), rs.getString("dst"), rs.getString("body"));
		}
	}

	public String toString() {
		return "Databaser";
	}
}
