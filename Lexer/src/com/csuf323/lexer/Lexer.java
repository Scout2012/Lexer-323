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
		FINISH(0),
		CHARACTER(1),
		SEPARATOR(2),
		KEYWORD(3),
		OPER(4),
		REAL(5),
		INTEGER(6);
		
		private int id;
		
		State(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}
	
	Lexer.State[][] stateTransitionTable = {
			{ State.FINISH,     State.CHARACTER, State.SEPARATOR,   State.OPER,   State.REAL,   State.INTEGER },
			{ State.CHARACTER,  State.CHARACTER, State.FINISH,      State.FINISH, State.FINISH, State.FINISH },
			{ State.SEPARATOR,  State.FINISH,    State.SEPARATOR,   State.FINISH, State.FINISH, State.FINISH },
			{ State.OPER,       State.FINISH,    State.FINISH,      State.OPER,   State.FINISH, State.FINISH },
			{ State.REAL,       State.FINISH,    State.FINISH,      State.FINISH, State.REAL,   State.REAL},
			{ State.INTEGER,    State.FINISH,    State.FINISH,      State.FINISH, State.REAL,   State.INTEGER }
	};
	
	public Lexer() {
	}

	private List<Token> lexicallyAnalyze(String expression) {
		return null;
	}

	private State parseCharacter(char input) {
		switch(input){
			case '=': case '+': case '-': case '/': case '*': case '<': case '>': case '%':
				return State.OPER;
			case ' ': case '(': case ')': case '{': case '}': case '[': case ']': case ',': case '.': case ':': case ';':
			case '\"': case '\'':
				return State.SEPARATOR;
			case '1': case'2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': case '0':
				return State.INTEGER;
		}
		return State.CHARACTER;
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
