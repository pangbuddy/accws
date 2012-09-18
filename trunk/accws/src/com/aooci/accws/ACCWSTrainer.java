package com.aooci.accws;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ACCWSTrainer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,Integer> wordScore = new HashMap<String, Integer>();
		//Map<String,Integer> wordLeftScore = new HashMap<String, Integer>();
		//Map<String,Integer> wordRightScore = new HashMap<String, Integer>();
		
        long start = System.currentTimeMillis();
        try {
            DataInputStream in = new DataInputStream(new FileInputStream("input/pku_test.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            Writer out = new OutputStreamWriter(new FileOutputStream("output/index.txt"), "UTF-8");
            String line;
            while ((line = br.readLine()) != null) {
            	//line = "如过去美国FDA（药品与食品管理局）对中药的要求十分苛刻";
            	line = line.replaceAll("([\\w\\.%％℃１２３４５６７８９０零一二三四五六七八九十点百千万亿兆年月日·．]{2,})", " $1 ");
            	line = line.replaceAll("(?<=[\\pP‘’“”])", " ");
            	line = line.replaceAll("\\s{2,}", " ");
            	//line = line.replaceAll("\\s", "");
            	
        		for(String sentence : line.split("[\\pP‘’“”\\s]")){
        			sentence = sentence.replaceAll("\\s", "");
        			//System.out.println("Sentence :" + sentence);
        			//char[] letters = sentence.toCharArray();
        			if (sentence.matches("[a-zA-Z0-9_\\pP‘’“”１２３４５６７８９０零一二三四五六七八九十点百千万亿兆年月日·．]*")) {
        				continue;
        			}
        			String[] letters = sentence.split("(?<=.)");
        			
        			for(int begin = 0 ; begin < letters.length ; begin++){
        				StringBuilder word = new StringBuilder();
        				for(int index = begin ; index < letters.length; index++){
        					word.append(letters[index]);
        					if(wordScore.containsKey(word.toString())){
        						wordScore.put(word.toString(), wordScore.get(word.toString()) + 1);
        					}else{
        						wordScore.put(word.toString(), 1);
        					}
        					//System.out.print(letters[index]);
        				}
        				//System.out.println();
        			}
        		}
            }
            System.out.println("Done : " + (System.currentTimeMillis() - start));
            for(Entry<String, Integer> entry : wordScore.entrySet()){
            	out.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
    			//System.out.println(entry.getKey() + "\t" + entry.getValue());
    		}
            
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Done : " + (System.currentTimeMillis() - start));
		/*
		String inputLine = "结婚的和尚未结婚的, 中外科学名著, 北京大学生前来应聘, 为人民办公益, 他说的确实在理";

		inputLine = inputLine.replaceAll("\\s", "");
		for(String sentence : inputLine.split("[\\pP‘’“”]")){
			System.out.println("Sentence :" + sentence);
			//char[] letters = sentence.toCharArray();
			String[] letters = sentence.split("(?<=.)");
			
			for(int begin = 0 ; begin < letters.length ; begin++){
				StringBuilder word = new StringBuilder();
				for(int index = begin ; index < letters.length; index++){
					word.append(letters[index]);
					if(wordScore.containsKey(word.toString())){
						wordScore.put(word.toString(), wordScore.get(word.toString()) + 1);
					}else{
						wordScore.put(word.toString(), 1);
					}
					System.out.print(letters[index]);
				}
				System.out.println();
			}
		}
		*/
		
	}

}
