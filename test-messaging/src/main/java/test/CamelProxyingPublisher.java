package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.component.bean.ProxyHelper;

public class CamelProxyingPublisher extends AbstractSpringAwareEventPublisher
		implements CamelContextAware {

	private CamelContext camelContext;

	private EventRouter router;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		setListeners(proxyListeners());
	}

	@SuppressWarnings("unchecked")
	public <E extends Event> Collection<? extends EventListener<E>> proxyListeners()
			throws Exception {
		Collection<EventListener<E>> list = new ArrayList<EventListener<E>>();
		for (EventListener<? extends Event> l : getListeners()) {
			Class clazz = l.getClass();
			Type[] types = clazz.getGenericInterfaces();
			for (Type t : types) {
				Class<?> eventType = (Class<?>) ((ParameterizedType) t)
						.getActualTypeArguments()[0];
				EventListener<E> proxy = (EventListener<E>) ProxyHelper
						.createProxy(camelContext.getEndpoint(getRouter()
								.createRoute(eventType)), EventListener.class);
				list.add((EventListener<E>) proxy);
			}
		}
		return list;
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
