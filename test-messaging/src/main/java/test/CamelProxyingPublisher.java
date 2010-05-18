package test;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.component.bean.ProxyHelper;

public class CamelProxyingPublisher extends SpringAwareEventPublisher implements CamelContextAware {

    private CamelContext camelContext;
    private Map<Class<?>, EventListener<Event>> listenerRegistry;

    private EventRouteStrategy router;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        listenerRegistry = new HashMap<Class<?>, EventListener<Event>>();
        listenerRegistry.put(Event.class, createProxy(Event.class));
    }

    @Override
    public <E extends Event> void publish(E event) {
        if (!listenerRegistry.containsKey(event.getClass())) {
            listenerRegistry.put(event.getClass(), createProxy(event.getClass()));
        }
        for (Class<?> c : listenerRegistry.keySet()) {
            if (c.isAssignableFrom(event.getClass())) {
                EventListener<Event> l = listenerRegistry.get(c);
                l.receive(event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> EventListener<E> createProxy(Class<?> eventType) {
        EventListener<E> proxy = null;
        try {
            proxy = (EventListener<E>) ProxyHelper.createProxy(camelContext.getEndpoint(getRouter().createRoute(
                    eventType)), EventListener.class);
        } catch (Exception e) {
            e.printStackTrace();// XXX log exception
        }
        return proxy;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public void setRouter(EventRouteStrategy router) {
        this.router = router;
    }

    public EventRouteStrategy getRouter() {
        return router;
    }
}
