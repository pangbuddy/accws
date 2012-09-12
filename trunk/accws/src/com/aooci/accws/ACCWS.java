package com.aooci.accws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class ACCWS {
	private Set<String> sIndex;
	
	public ACCWS() {
		init();
	}
	
	   public String processStringMaxMatch(String text){
	        text = text.replaceAll("([\\w\\.]+)", " $1 ");
	        //text = text.replaceAll("(?<=[\\pP‘’“”])", " ");
	        text = text.replaceAll("(?<=[，。’‘”“])", " ");
	        text = text.replaceAll("\\s{2,}", " ");
	        StringBuilder segmentedText = new StringBuilder();
	        for(String sentence : text.split("\\s")){
	            if(sentence.matches("[a-zA-Z0-9_\\pP‘’“”]*")){
	                segmentedText.append("|"+sentence);
	            }else{
	                StringBuilder segmentedSentence = new StringBuilder();
	                int begin = 0;
	                int end = sentence.length();
	                while(end > 0){
	                    String word = "";
	                    while(begin < end){
	                        if(sIndex.contains(sentence.substring(begin, end))){
	                            word = sentence.substring(begin, end);
	                            end = begin;
	                            
	                            break;
	                        }
	                        begin++;
	                    }
	                    if(word.isEmpty()){
	                        word = sentence.substring(end-1, end);
	                        end -= 1;
	                    }
	                    begin = 0;
	                    segmentedSentence.insert(0, "|"+word);
	                }
	                segmentedText.append(segmentedSentence);
	            }
	        }
	            return segmentedText.toString();
	    }
	   
	
	public String processStringMinMatch(String text){
	    text = text.replaceAll("([\\w\\.]+)", " $1 ");
		//text = text.replaceAll("(?<=[\\pP‘’“”])", " ");
		text = text.replaceAll("\\s{2,}", " ");
		StringBuilder segmentedText = new StringBuilder();
		for(String sentence : text.split("\\s")){
			if(sentence.matches("[a-zA-Z0-9_\\pP‘’“”]*")){
				segmentedText.append("|"+sentence);
			}else{
	            StringBuilder segmentedSentence = new StringBuilder();
	            int begin = sentence.length() - 2;
	            int end = sentence.length();
	            while(end > 0){
	                String word = "";
	                while(begin > -1){
	                    if(sIndex.contains(sentence.substring(begin, end))){
	                        word = sentence.substring(begin, end);
	                        end = begin;
	                        begin -= 2;
	                        if(begin < 0){
	                            begin = 0;
	                        }
	                        break;
	                    }
	                    begin--;
	                }
	                if(word.isEmpty()){
	                    word = sentence.substring(end-1, end);
	                    end -= 1;
	                    begin = end - 1;
	                }
	                segmentedSentence.insert(0, "|"+word);
	            }
	            segmentedText.append(segmentedSentence);
			}
		}
            return segmentedText.toString();
	}

	private void init(){
        this.sIndex = new HashSet<String>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/cedict_ts.d"), Charset.forName("UTF-8")), '\t');
            String [] nextLine;
            
            while ((nextLine = reader.readNext()) != null) {
            	this.sIndex.add( nextLine[0]);
    		}            
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded index size : " + this.sIndex.size());
	}

}
