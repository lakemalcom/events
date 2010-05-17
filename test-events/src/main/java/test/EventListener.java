package test;

public interface EventListener<E extends Event> {
	void receive(E event);
}
