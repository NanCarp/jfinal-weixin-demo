package com.test;

import org.junit.Test;

import com.jfinal.weixin.demo.TestApiController;

public class TestControllerTest {
	private static TestApiController test = new TestApiController();
	
	@Test
	public void testGroups() {
		test.groups();
	}

}
