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
            DataInputStream in = new DataInputStream(new FileInputStream("input/test_1m.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            Writer out = new OutputStreamWriter(new FileOutputStream("output/test_1m_result.txt"), "UTF-8");
            String line;
            while ((line = br.readLine()) != null) {

                out.write(accws.processStringMaxMatch(line) + System.getProperty("line.separator"));
            }
            
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Done : " + (System.currentTimeMillis() - start));

		//fail("Not yet implemented");
	}

}
