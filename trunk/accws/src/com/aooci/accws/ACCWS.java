package com.aooci.accws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class ACCWS {
	private Set<String> sIndex;
	private Set<String> lastNameIndex;
	private Set<String> suffixIndex;
	
	private final static String REGEX_COMPACT_WORD = "[a-zA-Z0-9_\\pP‘’“”１２３４５６７８９０零一二三四五六七八九十点百千万亿兆年月日时分秒·．]*";
	
	public ACCWS() {
		loadInternalDictionary();
	}
	
	public String getSeparatorString(List<String> words, String separator){
		StringBuilder separatorString = new StringBuilder();
    	for(String word : words)
    		separatorString.append(word + separator);
    	return separatorString.toString();
	}
	
	private String cleanText(String text){
		text = text.replaceAll("([\\w\\.%％℃１２３４５６７８９０零一二三四五六七八九十点百千万亿兆年月日时分秒·．]{2,})", " $1 ");
		//text = text.replaceAll("([\\w\\.%％℃１２３４５６７８９０·．]{2,})", " $1 ");
		text = text.replaceAll("(?<=[\\pP‘’“”])", " ");
		text = text.replaceAll("(?=[\\pP‘’“”])", " ");
		text = text.replaceAll("\\s{2,}", " ");
		text = text.replaceAll("(?<=[\\w\\.%％℃１２３４５６７８９０零一二三四五六七八九十点百千万亿兆年月日时分秒·．—])\\s(?=[\\w\\.%％℃１２３４５６７８９０零一二三四五六七八九十点百千万亿兆年月日·．—])", "");
		text = text.replaceAll("(?<=[\\w１２３４５６７８９０零一二三四五六七八九十]+[年月日])", " ");
		text = text.replaceAll("\\s{2,}", " ");
		
		return text;
	}
	
	private void identifyPersonName(List<String> words){
		for(int index = 0 ; index < words.size()-2 ; index++){
			if((words.get(index).length() < 2) && 
					(words.get(index+1).length() < 2) && 
					(words.get(index+2).length() < 2) && 
					words.get(index+1).matches("[^a-zA-Z0-9_\\pP‘’“”１２３４５６７８９０零一二三四五六七八九十·．]") && 
					words.get(index+2).matches("[^a-zA-Z0-9_\\pP‘’“”１２３４５６７８９０零一二三四五六七八九十·．]") && 
					this.lastNameIndex.contains(words.get(index))){
				String name = words.get(index+1) + words.get(index+2);
				words.remove(index+1);
				words.remove(index+1);
				words.add(index+1, name);
			}
		}
	}
	
	private void suffixCombination(List<String> words){
		for(int index = 1 ; index < words.size() ; index++){
			if((words.get(index).length() < 2) && 
					//this.suffixIndex.contains(words.get(index)) &&
					//(words.get(index+1).length() < 2) && 
					//(words.get(index+2).length() < 2) && 
					//words.get(index+1).matches("[^a-zA-Z0-9_\\pP‘’“”１２３４５６７８９０零一二三四五六七八九十·．]") && 
					//words.get(index+2).matches("[^a-zA-Z0-9_\\pP‘’“”１２３４５６７８９０零一二三四五六七八九十·．]") && 
					this.suffixIndex.contains(words.get(index))){
				String newWord = words.get(index-1) + words.get(index);
				words.remove(index-1);
				words.remove(index-1);
				words.add(index-1, newWord);
			}
		}
	}
	
	public List<String> processReverseMax(String text){
		text = this.cleanText(text);
		List<String> segmentedText = new ArrayList<String>();
		for (String sentence : text.split("\\s")) {
			if (sentence.matches(REGEX_COMPACT_WORD) || (sentence.matches("[—]+"))) {
				segmentedText.add(sentence);
			} else {
				List<String> segmentedSentence = new ArrayList<String>();
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
						end--;
					}
					begin = 0;
					segmentedSentence.add(0, word);
				}
				segmentedText.addAll(segmentedSentence);
			}
		}
		try{
			this.identifyPersonName(segmentedText);
			this.suffixCombination(segmentedText);
		}catch(Exception e){
			e.printStackTrace();
		}
		return segmentedText;
	}
	/*
	public List<String> processRegularMin(String text){
		text = this.cleanText(text);
		List<String> segmentedText = new ArrayList<String>();
		for (String sentence : text.split("\\s")) {
			if (sentence.matches(REGEX_COMPACT_WORD)) {
				segmentedText.add(sentence);
			} else {
				List<String> segmentedSentence = new ArrayList<String>();
				int begin = 0;
				int end = begin + 1;
				while (begin < sentence.length()) {
					String word = "";
					while (end < sentence.length()) {
						if (sIndex.contains(sentence.substring(begin, end))) {
							word = sentence.substring(begin, end);
							begin = end;
							break;
						}
						end++;
					}
					if (word.isEmpty()) {
						word = sentence.substring(begin, begin + 1);
						begin++;
					}
					end = begin + 1;
					segmentedSentence.add(word);
				}
				segmentedText.addAll(segmentedSentence);
			}
		}
		return segmentedText;
	}
*/
	public List<String> processRegularMax(String text){
		text = this.cleanText(text);
		List<String> segmentedText = new ArrayList<String>();
		for (String sentence : text.split("\\s")) {
			if (sentence.matches(REGEX_COMPACT_WORD)) {
				segmentedText.add(sentence);
			} else {
				List<String> segmentedSentence = new ArrayList<String>();
				int begin = 0;
				int end = sentence.length();
				while (begin < sentence.length()) {
					String word = "";
					while (begin < end) {
						if (sIndex.contains(sentence.substring(begin, end))) {
							word = sentence.substring(begin, end);
							begin = end;
							break;
						}
						end--;
					}
					if (word.isEmpty()) {
						word = sentence.substring(begin, begin + 1);
						begin++;
					}
					end = sentence.length();
					segmentedSentence.add(word);
				}
				segmentedText.addAll(segmentedSentence);
			}
		}
		return segmentedText;
	}

	public void setExtendedDictionary(String dictFile){
		int internalDictionarySize = this.sIndex.size();
        try {
			this.chargeIndex(new FileInputStream(dictFile), this.sIndex);
			System.out.println("Extended dictionary : " + dictFile);
			System.out.println("Extended dictionary size : " + (this.sIndex.size() - internalDictionarySize));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadInternalDictionary(){
		this.sIndex = new HashSet<String>();
		this.lastNameIndex = new HashSet<String>();
        this.suffixIndex = new HashSet<String>();
		
		this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/cedict_ts.d"), this.sIndex);
        this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/area_cn.d"), this.sIndex);
        this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/idiom.d"), this.sIndex);
        System.out.println("Internal dictionary size : " + this.sIndex.size());

        this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/last_name_cn.d"), this.lastNameIndex);
        System.out.println("Chinese last name dictionary size : " + this.lastNameIndex.size());  
        
        this.chargeIndex(ACCWS.class.getResourceAsStream("/com/aooci/accws/dict/suffix.d"), this.suffixIndex);
        System.out.println("Suffix dictionary size : " + this.suffixIndex.size());  
	}
	
	private void chargeIndex(InputStream in, Set<String> index){
		CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(in, Charset.forName("UTF-8")), '\t');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
            	index.add( nextLine[0]);
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
