package com.aooci.accws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class ACCWS {
	private Set<String> sIndex;
	
	public ACCWS() {
		loadInternalDictionary();
	}
	
	public String processPositiveMax(String text){
		return null;
	}
	
	private String cleanText(String text){
		text = text.replaceAll("([\\w\\.%％℃]+)", " $1 ");
		text = text.replaceAll("(?<=[\\pP‘’“”])", " ");
		text = text.replaceAll("\\s{2,}", " ");
		return text;
	}

	public String processReverseMax(String text, String delimiter) {
		text = this.cleanText(text);
		StringBuilder segmentedText = new StringBuilder();
		for (String sentence : text.split("\\s")) {
			if (sentence.matches("[a-zA-Z0-9_\\pP‘’“”]*")) {
				segmentedText.append(sentence + delimiter);
			} else {
				StringBuilder segmentedSentence = new StringBuilder();
				int begin = 0;
				int end = sentence.length();
				while (end > 0) {
					String word = "";
					while (begin < end) {
						if (sIndex.contains(sentence.substring(begin, end))) {
							word = sentence.substring(begin, end);
							end = begin;

							break;
						}
						begin++;
					}
					if (word.isEmpty()) {
						word = sentence.substring(end - 1, end);
						end -= 1;
					}
					begin = 0;
					segmentedSentence.insert(0, word + delimiter);
				}
				segmentedText.append(segmentedSentence);
			}
		}
		return segmentedText.toString();
	}

	public void setExtendedDictionary(String dictFile){
		int internalDictionarySize = this.sIndex.size();
        try {
			this.chargeIndex(new FileInputStream(dictFile));
			System.out.println("Extended dictionary : " + dictFile);
			System.out.println("Extended dictionary size : " + (this.sIndex.size() - internalDictionarySize));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadInternalDictionary(){
        this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/cedict_ts.d"));
        this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/area_cn.d"));
        System.out.println("Internal dictionary size : " + this.sIndex.size());
	}
	
	private void chargeIndex(InputStream in){
		if(this.sIndex == null){
			this.sIndex = new HashSet<String>();
		}
		CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(in, Charset.forName("UTF-8")), '\t');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
            	this.sIndex.add( nextLine[0]);
    		}            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}
}
