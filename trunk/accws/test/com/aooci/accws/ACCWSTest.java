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
            DataInputStream in = new DataInputStream(new FileInputStream("input/pku_28288_test.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            Writer out = new OutputStreamWriter(new FileOutputStream("output/result.txt"), "UTF-8");
            String line;
            while ((line = br.readLine()) != null) {
                out.write(accws.processReverseMax(line, " ") + System.getProperty("line.separator"));
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
		String inputLine = "该系统便会自动运转，";
		System.out.println("●|" + inputLine);
		System.out.println("○|" + accws.processReverseMax(inputLine, "  "));
		
	}

}
