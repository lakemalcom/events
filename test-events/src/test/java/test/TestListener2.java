/**
 * 
 */
package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import test.EventTest.TestEvent2;

public final class TestListener2 implements EventListener<TestEvent2> {
	private final int expectedEvents;
	private int counter = 0;
	public TestListener2(int expectedEvents)
	{
		this.expectedEvents = expectedEvents;
	}
	@Override
	public void receive(TestEvent2 event) {
		assertNotNull(event);
		System.out.println(this.getClass()+ " received event " + event.getClass());
		assertTrue(expectedEvents >= ++counter);
	}
}