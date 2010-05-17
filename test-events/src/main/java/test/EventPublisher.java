package test;

public interface EventPublisher {
	<E extends Event> void publish(E event);
}
