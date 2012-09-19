package com.aooci.accws;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.junit.Test;

public class ACCWSTest {

	@Test
	public void testACCWS() {

        long start = System.currentTimeMillis();
        try {
        	ACCWS accws = new ACCWS();
        	accws.setExtendedDictionary("input/dict.d");
            DataInputStream in = new DataInputStream(new FileInputStream("input/pku_test.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            Writer out = new OutputStreamWriter(new FileOutputStream("output/result.txt"), "UTF-8");
            String line;
            while ((line = br.readLine()) != null) {
                out.write(accws.getSeparatorString(accws.processReverseMax(line), " ") + System.getProperty("line.separator"));
            }
            
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Done : " + (System.currentTimeMillis() - start));

		//fail("Not yet implemented");
	}
	
	@Test
	public void testLine(){
		
		ACCWS accws = new ACCWS();
		accws.setExtendedDictionary("input/dict.d");

		//String inputLine = "结婚的和尚未结婚的, 中外科学名著, 北京大学生前来应聘, 为人民办公益, 他说的确实在理,“提高产品质量”、“鞭炮声响彻夜空”、“努力学习语法规则”";
		String inputLine = "我征求爷爷的意见。爷爷说：“早先，日本鬼子的飞机，中国吉林雾凇冰雪节的正式表演项目, 2001年1月1日零时，涉及科局级干部13人。其中有一位开发区管委会主任，是该院一名女干警的家属。这位主任负责基金会工作，因滥用职权损失贷款80多万元。院领导做通了这名女干警的思想工作，这名女干警积极配合办案人员，顶住各方压力查清了案件。";
		//String inputLine = "（新华社记者李昌元摄）,1999年以来，检察长袁成武上任后决心从一点一滴抓起，福清市发生了陈维华等10人持枪杀人案";

		System.out.println(" ●|" + inputLine);
		System.out.println("=<|" + accws.processReverseMax(inputLine));
		System.out.println("=>|" + accws.processRegularMax(inputLine));
		//System.out.println("->|" + accws.processRegularMin(inputLine));
	}

}
