package com.csuf323.lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Lexer {

	class Token{
		public State tokenName;
		public String lexemeName;
	}
	
	public enum State {
		START(0),
		IDENTIFIER(1),
		NUMBER(2),
		REAL(3),
		IN_STRING(4),
		IN_COMMENT(5),
		IN_COMPARATOR(6),
		COMPARATOR(7),
		SEPARATOR(8),
		END_STATEMENT(9),
		DOT_TRANSITION(10);
		
		private int id;
		
		State(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}
	
	Lexer.State[][] stateTransitionTable = {
			{ State.START,          State.IDENTIFIER,  State.NUMBER,     State.REAL,    State.IN_STRING,  State.IN_COMMENT, State.IN_COMPARATOR, State.COMPARATOR, State.SEPARATOR, State.END_STATEMENT, State.DOT_TRANSITION},
			{ State.IDENTIFIER,     State.IDENTIFIER,  State.IDENTIFIER, State.START,   State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.NUMBER,         State.START,       State.NUMBER,     State.REAL,    State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.REAL},
			{ State.REAL,           State.START,       State.REAL,       State.REAL,    State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.DOT_TRANSITION},
			{ State.IN_STRING,      State.START,       State.START,      State.START,   State.IN_STRING,  State.START,      State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.IN_COMMENT,     State.START,       State.START,      State.START,   State.IN_COMMENT, State.IN_COMMENT, State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.IN_COMPARATOR,  State.START,       State.START,      State.START,   State.START,      State.START,      State.START,         State.COMPARATOR, State.SEPARATOR, State.END_STATEMENT, State.DOT_TRANSITION},
			{ State.COMPARATOR,     State.START,       State.START,      State.START,   State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.SEPARATOR,      State.START,       State.START,      State.START,   State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.END_STATEMENT,  State.START,       State.START,      State.START,   State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.DOT_TRANSITION, State.START,       State.REAL,       State.START,   State.START,      State.START,      State.START,         State.START,      State.START,     State.START,         State.START},
	};
	
	public Lexer() {
	}

	private List<Token> lexicallyAnalyze(String expression) {
		return null;
	}

	private State parseCharacter(char input) {
		return null;
	}

	private State parseState(State current, State input) {
		//State input, while State current would be the previous result
		return stateTransitionTable[current.getId()][input.getId()];
	}

	public String feedMe(String fileName) {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
	        stream.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fileName;
	}
}
