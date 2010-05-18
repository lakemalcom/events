package test;

import static org.junit.Assert.*;

public class BaseListener implements EventListener<Event>{

    @Override
    public void receive(Event event) {
        assertNotNull(event);
        System.out.println(this.getClass() + " received event: " + event.getClass());
    }

}
