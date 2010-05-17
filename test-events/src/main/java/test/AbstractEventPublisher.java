package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public abstract class AbstractEventPublisher implements EventPublisher {

	@SuppressWarnings("unchecked")
	@Override
	public <E extends Event> void publish(E event) {
		for (EventListener<? extends Event> l : getListeners()) {
			Class clazz = l.getClass();
			Type[] types = clazz.getGenericInterfaces();
			for (Type t : types) {
				Class<?> eventType = (Class<?>) ((ParameterizedType) t)
						.getActualTypeArguments()[0];
				if (eventType.isAssignableFrom(event.getClass())) {
					EventListener<E> el = (EventListener<E>) l;
					el.receive(event);
				}
			}
		}
	}

	abstract void setListeners(
			Collection<? extends EventListener<? extends Event>> listeners);

	abstract Collection<? extends EventListener<? extends Event>> getListeners();

}
