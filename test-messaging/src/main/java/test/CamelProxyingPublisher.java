package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.component.bean.ProxyHelper;

public class CamelProxyingPublisher extends AbstractSpringAwareEventPublisher
		implements CamelContextAware {

	private CamelContext camelContext;
	private Map<Class<?>, ? extends EventListener<? extends Event>> listenerMap;

	private EventRouter router;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		listenerMap = proxyListeners();
	}
	
	@Override
	public <E extends Event> void publish(E event) {
		EventListener<E> eventListener = (EventListener<E>) listenerMap.get(event.getClass());
		eventListener.receive(event);
	}

	@SuppressWarnings("unchecked")
	public <E extends Event> Map<Class<?>, ? extends EventListener<E>> proxyListeners()
			throws Exception {
		Map<Class<?>, EventListener<E>> lMap = new HashMap<Class<?>, EventListener<E>>();
		for (EventListener<? extends Event> l : getListeners()) {
			Class clazz = l.getClass();
			Type[] types = clazz.getGenericInterfaces();
			for (Type t : types) {
				Class<?> eventType = (Class<?>) ((ParameterizedType) t)
						.getActualTypeArguments()[0];
				EventListener<E> proxy = (EventListener<E>) ProxyHelper
						.createProxy(camelContext.getEndpoint(getRouter()
								.createRoute(eventType)), EventListener.class);
				lMap.put(eventType, proxy);
			}
		}
		return lMap;
	}

	@Override
	Collection<? extends EventListener<? extends Event>> getListeners() {
		// TODO Auto-generated method stub
		return super.getListeners();
	}

	@Override
	public CamelContext getCamelContext() {
		return camelContext;
	}

	@Override
	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	public void setRouter(EventRouter router) {
		this.router = router;
	}

	public EventRouter getRouter() {
		return router;
	}
}
