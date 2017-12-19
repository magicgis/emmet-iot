package com.emmet.iot.core.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class JsonHelperTest {
	
	private static final String JSON_STRING = "{\"bar\":\"test\"}";

	@Test
	public void test() {
		Foo foo = JsonHelper.JsonStringToObject(JSON_STRING, Foo.class);
		assertThat(foo.getBar(), equalTo("test"));
		
		String jsonSting = JsonHelper.ObjectToJsonString(foo);
		assertThat(jsonSting, equalTo(JSON_STRING));
	}

}
