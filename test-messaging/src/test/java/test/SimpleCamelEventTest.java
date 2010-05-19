package test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.EventTest.TestEvent1;
import test.EventTest.TestEvent2;

public class SimpleCamelEventTest {
    
    private EventPublisher publisher;

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("camel-simple-spring-test.xml");
        publisher = (EventPublisher) ctx.getBean("publisher");
    }

    @Test
    public void testListening() throws InterruptedException {
        publisher.publish(new TestEvent1());
        Thread.sleep(1000);
        publisher.publish(new TestEvent2());
        Thread.sleep(1000);
        publisher.publish(new TestEvent2());
        Thread.sleep(1000);
    }
    
}
