package gr.jchrist.chronicle;

import java.io.Serializable;

public class Message implements Comparable, Serializable, Cloneable {
	public int id;
	public String src;
	public String dst;
	public String body;

	public Message() {
		this.id = 0;
		this.src = "";
		this.dst = "";
		this.body = "";
	}

	public Message(int id, String src, String dst, String body) {
		this.id = id;
		this.src = src;
		this.dst = dst;
		this.body = body;
	}

	public int compareTo(Object o) throws ClassCastException {
		return compareTo((Message) o);
	}

	public int compareTo(Message m) {
		return this.id - m.id;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object o) {
		return o != null && o instanceof Message && equals((Message) o);

	}

	public boolean equals(Message msg) {
		return this.id == msg.id;
	}

	public int hashCode() {
		return id;
	}
}
