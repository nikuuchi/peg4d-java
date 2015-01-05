package org.peg4d;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ErrorInfo {

	private Set<String> expected;
	private String input;
	private long line;
	private long column;
	public long reportedPos;

	ErrorInfo() {
		expected = new HashSet<String>();
		setInput("");
		setLine(0);
		setColumn(0);
	}

	ErrorInfo(String input, long line, long column, Set<String> expected) {
		this.setInput(input);
		this.setLine(line);
		this.setColumn(column);
		this.expected = expected;
	}

	@Override
	public String toString() {
		Iterator<String> it = expected.iterator();
		StringBuilder sb = new StringBuilder();
		if(it.hasNext()) {
			sb.append(it.next());
		}
		while(it.hasNext()) {
			sb.append(" / ");
			sb.append(it.next());
		}
		return sb.toString();
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public long getLine() {
		return line;
	}

	public void setLine(long line) {
		this.line = line;
	}

	public long getColumn() {
		return column;
	}

	public void setColumn(long column) {
		this.column = column;
	}

	public void addExpected(String s) {
		this.expected.add(s);
	}

	public void clearExpected() {
		this.expected.clear();
	}

	public Set<String> getExpected() {
		return this.expected;
	}
}
