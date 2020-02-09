package com.csuf323.lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
	
	public static enum State{
		START,
		KEYWORD,
		SEPARATOR,
		IDENTIFIER,
		OPERATOR,
		REAL,
		INTEGER
	}
	
	public static void main(String[] args){
		String source_path = args[0];
		BufferedReader fileReader = null;
		State currentState = State.START;
		
		try{
			fileReader = new BufferedReader(new FileReader(source_path));
			String text = null;
			
			while((text = fileReader.readLine()) != null){
				for(int i = 0; i < text.length(); i++){
					step(text.charAt(i), currentState);
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				if(fileReader != null){
					fileReader.close();
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void step(Character currentInput, State state){
		switch(state){
			case START:
				break;
			default:
				break;
		}
	}
}
