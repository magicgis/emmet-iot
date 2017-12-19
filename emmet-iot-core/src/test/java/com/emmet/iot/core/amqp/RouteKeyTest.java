package com.emmet.iot.core.amqp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class RouteKeyTest {

	@Test
	public void test() {
	     assertThat(new DeviceRoutingkey("foo").cannel("bar").status(), equalTo("device.foo.bar.status"));
	     assertThat(new DeviceRoutingkey("foo").cannel("bar").update(), equalTo("device.foo.bar.update"));
		
	     assertThat(new SubjectRoutingkey("foo").topic("bar").status(), equalTo("view.foo.bar.status"));
	     assertThat(new SubjectRoutingkey("foo").topic("bar").update(), equalTo("view.foo.bar.update"));
	
	}

}
