package com.aooci.accws;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ACCWSExaminer {

	public static void examine(String goldFile, String examineeFile, String splitRegex, boolean printMistakes){
        long start = System.currentTimeMillis();
        int total = 0;
        int correct = 0;
        try {
            DataInputStream goldIn = new DataInputStream(new FileInputStream(goldFile));
            DataInputStream examineeIn = new DataInputStream(new FileInputStream(examineeFile));
            
            BufferedReader goldBr = new BufferedReader(new InputStreamReader(goldIn, Charset.forName("UTF-8")));
            BufferedReader examineeBr = new BufferedReader(new InputStreamReader(examineeIn, Charset.forName("UTF-8")));

            String goldLine;
            String examineeLine;
            int lineNumber = 0;
            
            while ((goldLine = goldBr.readLine()) != null) {
            	examineeLine = examineeBr.readLine();
            	lineNumber++;
            	
            	List<String> goldWords = new ArrayList<String>(Arrays.asList(goldLine.split(splitRegex)));
            	List<String> examineeWords = Arrays.asList(examineeLine.split(splitRegex));
            	List<String> mistakes = new ArrayList<String>();
            	
            	total += goldWords.size();  
            	int goldWordsSize = goldWords.size();
            	
            	for(String word : examineeWords){
            		if(goldWords.contains(word)){
            			goldWords.remove(word);
            		}else{
            			mistakes.add(word);
            		}
            	}
            	correct += (goldWordsSize - goldWords.size());
            	
            	if(printMistakes && (goldWords.size() > 0)){
            		System.out.println("________________________________________________________________ line: " + lineNumber);
            		System.out.println("●|" + goldLine);
            		System.out.println("○|" + examineeLine);
            	}
            }
            
            goldIn.close();
            examineeBr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done : " + (System.currentTimeMillis() - start));
        System.out.println("Words total: " + total);
        System.out.println("Words correct: " + correct);
        System.out.println("Final score: " + ((float)correct/(float)total * 100) + " %");
	}

}
