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

	//@Test
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
                out.write(accws.getSeparatorString(accws.processRegularMax(line), " ") + System.getProperty("line.separator"));
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
		String inputLine = "1999年以来，福建省各级检察机关要求公安机关说明不立案理由480件，换回信任，赢得民心。 检察长袁成武上任后决心从一点一滴抓起，";
		//String inputLine = "结婚的和尚未结婚的, 中外科学名著, 北京大学生前来应聘, 为人民办公益, 他说的确实在理";
		System.out.println(" ●|" + inputLine);
		System.out.println("=<|" + accws.processReverseMax(inputLine));
		System.out.println("=>|" + accws.processRegularMax(inputLine));
		System.out.println("->|" + accws.processRegularMin(inputLine));
	}

}
