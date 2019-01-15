package com.sp01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"/ApplicationContext.xml" })
public class TestCellPhone {

	@Autowired
	CellPhone cp;

	@Test
	public void testRun()
	{	System.out.println(cp);
		String re=cp.run();
		assertEquals("PhoneXX",re);
	}
}
