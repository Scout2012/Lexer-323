package com.csuf323.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lexer {

	class Token{
		public State tokenName;
		public String lexemeName;
	}

	public enum State {
		REJECT(-1),
		START(0),
		IDENTIFIER(1),
		NUMBER(2),
		REAL(3),
		IN_STRING(4),
		IN_COMMENT(5),
		SPACE(6),
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
			{ State.START,          State.IDENTIFIER,  State.NUMBER,     State.REAL,         State.IN_STRING,  State.IN_COMMENT, State.SPACE, State.IN_COMPARATOR, State.COMPARATOR, State.SEPARATOR, State.END_STATEMENT, State.DOT_TRANSITION},
			{ State.IDENTIFIER,     State.IDENTIFIER,  State.IDENTIFIER, State.START,        State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.NUMBER,         State.START,       State.NUMBER,     State.REAL,         State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.REAL},
			{ State.REAL,           State.START,       State.REAL,       State.REAL,         State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.DOT_TRANSITION},
			{ State.IN_STRING,      State.START,       State.START,      State.START,        State.IN_STRING,  State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.IN_COMMENT,     State.IN_COMMENT,  State.IN_COMMENT, State.IN_COMMENT,   State.IN_COMMENT, State.IN_COMMENT, State.START,       State.IN_COMMENT,    State.IN_COMMENT, State.IN_COMMENT,State.IN_COMMENT,    State.IN_COMMENT},
			{ State.SPACE, 	        State.START,       State.START,      State.START,        State.START,      State.START,      State.SPACE,       State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.IN_COMPARATOR,  State.START,       State.START,      State.START,        State.START,      State.START,      State.START,       State.START,         State.COMPARATOR, State.SEPARATOR, State.END_STATEMENT, State.DOT_TRANSITION},
			{ State.COMPARATOR,     State.START,       State.START,      State.START,        State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.SEPARATOR,      State.START,       State.START,      State.START,        State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.END_STATEMENT,  State.START,       State.START,      State.START,        State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.START},
			{ State.DOT_TRANSITION, State.START,       State.REAL,       State.START,        State.START,      State.START,      State.START,       State.START,         State.START,      State.START,     State.START,         State.START},
	};

	private State prevState = State.START;
	private State currState = State.START;
	private boolean isInComment = false;

	public Lexer() {
	}

	private List<Token> lexicallyAnalyze(String expression) {
		return null;
	}

	private State getColumn(char input) {
		switch(input){
			case '0': case '1':case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
				return State.NUMBER;	
			case '(': case ')': case '{': case '}': case ',':
				return State.SEPARATOR;
			 case ' ':
				 return State.SPACE;
			case '>': case '<': case '=': 
				if(this.currState != State.IN_COMPARATOR){
					System.out.println("Rhee " + this.currState);
					return State.IN_COMPARATOR;
				} else {
					return State.COMPARATOR;
				}
			case '!':
				if(this.currState != State.IN_COMMENT){
					return State.IN_COMMENT;
				} else {
					return State.START;
				}
			case '.':
				if(this.currState == State.NUMBER){
					return State.REAL;
				} else {
					return State.DOT_TRANSITION;
				}
			case '"':
				if(this.currState != State.IN_STRING){
					return State.IN_STRING;
				} else {
					return State.START;
				}
			case ';':
				return State.END_STATEMENT;
			default:
				if(this.currState == State.IN_COMMENT){
					return State.IN_COMMENT;
				} else {
					return State.IDENTIFIER;
				}
		}
	}

	private State parseState(State current, State input) {
		//State input, while State current would be the previous result
		return stateTransitionTable[current.getId()][input.getId()];
	}

	public List<Token> createTokenList(String fileName){
		File file = new File(fileName);
		Scanner input = null;
		List<Token>tokenList = new ArrayList<Token>();
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (input.hasNextLine()) {
			String nextLineRead = input.nextLine();
			String currToken = "";
			Token token = new Token();
			for(int i = 0; i < nextLineRead.length();){
				char currChar = nextLineRead.charAt(i);
				if(currChar == '!'){
					this.isInComment = !this.isInComment;
				}
				if(!this.isInComment){
					this.currState = parseState(this.currState, getColumn(currChar));
				} else {
					this.currState = State.IN_COMMENT;
				}
				if(this.currState == State.START && this.prevState != State.IN_COMMENT){
					if(currChar != '\n' && currChar != '\t'){
						token.tokenName=this.prevState;//System.out.print(this.prevState);
						token.lexemeName=currToken;//System.out.println("           "  + currToken);
						tokenList.add(token);
						token = new Token();
					}
					currToken = "";
				} else {
					currToken += currChar;
					++i;
				}
				this.prevState = this.currState;
			}
		}
		return tokenList;
	}


	public void feedMe(String fileName) {
		List<Token>tokenList = new ArrayList<Token>();
		tokenList = createTokenList(fileName);
		for(Token printToken:tokenList){
			System.out.print(printToken.tokenName);
			System.out.println("           "  + printToken.lexemeName);
		}
	}
}