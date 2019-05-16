package com.xzy;

import com.MyConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MyConfig.class})
public class TestCellPhone {

	@Autowired
	CellPhone cp;
	@Autowired
	Student stu;
	@Test
	public void testRun()
	{
		String re=cp.run();
		System.out.println(stu.getName()+"--"+stu.getAge());
		assertEquals("PhoneXX",re);
	}
}
