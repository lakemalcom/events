package test;

import org.apache.camel.InOnly;

public interface EventListener<E extends Event> {
    
    @InOnly
	void receive(E event);
}
