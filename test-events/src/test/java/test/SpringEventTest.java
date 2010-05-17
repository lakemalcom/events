package test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.EventTest.TestEvent1;
import test.EventTest.TestEvent2;

public class SpringEventTest {
	EventPublisher publisher;
	@Before
	public void setUp() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-test.xml");
		publisher = (EventPublisher) ctx.getBean("publisher");
	}
	
	@Test
	public void testListening() {
		publisher.publish(new TestEvent1());
		publisher.publish(new TestEvent2());
		publisher.publish(new TestEvent2());
	}
}
