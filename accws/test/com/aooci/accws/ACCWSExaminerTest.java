package com.aooci.accws;

import org.junit.Test;

public class ACCWSExaminerTest {

	@Test
	public void testExamine() {
		//ACCWSExaminer.examine("\\s\\s", "input/pku_test_gold.txt", "input/pku_test_gold.txt");
		ACCWSExaminer.examine("input/pku_test_gold.txt", "output/result.txt", "\\s", true);
	}

}
