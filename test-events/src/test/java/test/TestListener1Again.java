package test;

import test.EventTest.TestEvent1;
import static org.junit.Assert.*;

public class TestListener1Again implements EventListener<TestEvent1>{
	private int counter = 0;
    @Override
    public void receive(TestEvent1 event) {
		assertNotNull(event);
		System.out.println(this.getClass()+ " received event " + event.getClass());
		assertEquals(0, counter++);
    }

}
