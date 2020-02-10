package com.csuf323.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Lexer {

	class Token{
		public String tokenName;
		public String lexemeName;
		
	}
	
	public enum State{
		FINISH,
		CHARACTER,
		SEPARATOR,
		KEYWORD,
		OPER,
		REAL,
		INTEGER
	}
	
	Lexer.State[][] stateTransitionTable = {
			{ State.FINISH,     State.CHARACTER, State.SEPARATOR,   State.OPER,   State.REAL,   State.INTEGER },
			{ State.CHARACTER,  State.CHARACTER, State.FINISH,      State.FINISH, State.FINISH, State.FINISH },
			{ State.SEPARATOR,  State.FINISH,    State.SEPARATOR,   State.FINISH, State.FINISH, State.FINISH },
			{ State.OPER,       State.FINISH,    State.FINISH,      State.OPER,   State.FINISH, State.FINISH },
			{ State.REAL,       State.FINISH,    State.FINISH,      State.FINISH, State.REAL,   State.REAL},
			{ State.INTEGER,    State.FINISH,    State.FINISH,      State.FINISH, State.REAL,   State.INTEGER }
	};
	
	public Lexer(){
	}
	
	private List<Token> lexicallyAnalyze(String expression){
		return null;
		
	}
	
	public String feedMe(String fileName){
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
	        stream.forEach(System.out::println);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return fileName;
	}
}
