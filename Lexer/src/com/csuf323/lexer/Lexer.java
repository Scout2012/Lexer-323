package com.csuf323.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
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
		IN_COMPARATOR(7),
		COMPARATOR(8),
		SEPARATOR(9),
		END_STATEMENT(10),
		DOT_TRANSITION(11),
		KEYWORD(12);
		private int id;

		State(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	Lexer.State[][] stateTransitionTable = {
			{ State.START,          State.IDENTIFIER,  State.NUMBER,     State.REAL,         State.IN_STRING,  State.IN_COMMENT, State.SPACE,       State.IN_COMPARATOR, State.COMPARATOR, State.SEPARATOR, State.END_STATEMENT, State.DOT_TRANSITION},
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
		List<Token>tokenList = new ArrayList<>();
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
				if(currChar == '!' && this.isInComment){
					this.isInComment = false;
					this.prevState = State.IN_COMMENT;
					this.currState = State.START;
				} else if(currChar == '!') {
					this.isInComment = true;
					this.prevState = this.currState;
					this.currState = State.IN_COMMENT;
				}
				if(!this.isInComment && currChar != '!'){
					this.currState = parseState(this.currState, getColumn(currChar));
				}
				if(this.currState == State.START || i == nextLineRead.length()-1){
					if(this.currState != State.IN_COMMENT) {
						if(this.prevState != State.SPACE){
							if(i == nextLineRead.length()-1) {
								State read = parseState(this.currState, getColumn(currChar));
								if(read == this.prevState) {
									currToken = currToken + currChar;
									token.tokenName = this.currState;
									token.lexemeName = currToken;
									tokenList.add(token);
									token = new Token();
								} else {
									token.tokenName = this.prevState;
									token.lexemeName = currToken;
									tokenList.add(token);
									token = new Token();
									currToken = Character.toString(currChar);
									token.tokenName = read;
									token.lexemeName = currToken;
									tokenList.add(token);
									token = new Token();
								}
								++i;
							} else {
								token.tokenName = this.prevState;
								token.lexemeName = currToken;
								tokenList.add(token);
								token = new Token();
							}
						}
					} else {
						++i;
					}
					currToken = "";
				} else if(this.currState== State.IN_COMMENT || (currChar != ' ' && currChar != '\n' && currChar != '\t')){
					currToken += currChar;
					++i;
				} else {
					++i;
				}
				this.prevState = this.currState;
			}
		}
		tokenList = cleanTokenList(tokenList);
		return tokenList;
	}


	public void feedMe(String fileName) {
		List<Token>tokenList;
		tokenList = createTokenList(fileName);
		for(Token printToken:tokenList){
			int numSpaces = 15-printToken.tokenName.toString().length();
			String spaces = "";
			for(int i= 0; i < numSpaces; i++) {
				spaces += " ";
			}
			System.out.print(printToken.tokenName);
			System.out.println(spaces + printToken.lexemeName);
		}
	}

	public List<Token> cleanTokenList(List<Token> list){
		String keyWordList [] = {"int", "float", "bool", "true", "false", "if", "else", "then", "endif", "while", "whileend", "do", "doend", "for", "forend", "input", "output", "and", "or", "not"};
		Map<String,State> keyWordMap = new HashMap<>();
		for(String word:keyWordList){
			keyWordMap.put(word, State.KEYWORD);
		}
		for(Token token:list){
			if(keyWordMap.containsKey(token.lexemeName)){
				token.tokenName=keyWordMap.get(token.lexemeName);
			}
			if(token.lexemeName.startsWith("!")){
				token.tokenName = State.IN_COMMENT;
			}
		}

		return list;
	}

}