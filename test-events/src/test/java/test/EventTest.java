package test;


import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventTest {
	private EventPublisher publisher;
	private Collection<? extends EventListener<? extends Event>> listeners;
	
	
	@Before
	public void setUp() {
		publisher = new AbstractEventPublisher() {
			@Override
			void setListeners(
					Collection<? extends EventListener<? extends Event>> listeners) {
				EventTest.this.listeners = listeners;
			}
			
			@Override
			Collection<? extends EventListener<? extends Event>> getListeners() {
				return listeners;
			}
		};
	}
	
	@After
	public void tearDown() {
		listeners = null;
	}
	
	public static final class TestEvent1 implements Event {}
	public static final class TestEvent2 implements Event {}
	
	@Test
	public void testSingleTypedListener() {
		listeners = Arrays.asList(new TestListener1());
		
		publisher.publish(new TestEvent1());
	}
	
	@Test
	public void testTwoSeparateListeners() {
		listeners = Arrays.asList(new TestListener1(), new TestListener2(1));
		
		publisher.publish(new TestEvent1());
		publisher.publish(new TestEvent2());
	}
	
	@Test
	public void testMultipleEvents() {
		listeners = Arrays.asList(new TestListener1(), new TestListener2(2));
		
		publisher.publish(new TestEvent1());
		publisher.publish(new TestEvent2());
		publisher.publish(new TestEvent2());
	}
	
	
}
