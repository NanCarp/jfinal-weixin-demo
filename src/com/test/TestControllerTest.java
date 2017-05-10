package com.test;

import org.junit.Test;

import com.jfinal.weixin.demo.TestController;

public class TestControllerTest {
	private static TestController test = new TestController();
	
	@Test
	public void testGroups() {
		test.groups();
	}

}
