/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import test.EventTest.TestEvent1;

public final class TestListener1 implements EventListener<TestEvent1> {
	private int counter = 0;
	@Override
	public void receive(TestEvent1 event) {
		assertNotNull(event);
		System.out.println(this.getClass()+ " received event " + event.getClass());
		assertEquals(0, counter++);
	}
}