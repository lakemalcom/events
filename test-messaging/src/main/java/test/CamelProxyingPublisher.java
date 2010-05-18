package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.InOnly;
import org.apache.camel.component.bean.ProxyHelper;

public class CamelProxyingPublisher extends SpringAwareEventPublisher implements CamelContextAware {

    private CamelContext camelContext;
    private Map<Class<?>, Collection<EventListener<Event>>> listenerRegistry;

    private EventRouteStrategy router;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        listenerRegistry = proxyListeners();
    }

    @Override
    @InOnly
    public <E extends Event> void publish(E event) {
        for (Class<?> c : listenerRegistry.keySet()) {
            if (c.isAssignableFrom(event.getClass())) {
                for (EventListener<Event> l : listenerRegistry.get(c)) {
                    l.receive(event);
                }
            }
        }
    }

    public <E extends Event> Map<Class<?>, Collection<EventListener<E>>> proxyListeners() throws Exception {
        Map<Class<?>, Collection<EventListener<E>>> lMap = new HashMap<Class<?>, Collection<EventListener<E>>>();
        for (EventListener<? extends Event> l : getListeners()) {
            Class<?> clazz = l.getClass();
            if (l instanceof AbstractRouteListener<?>) {
                Class<?> eventType = (Class<?>) ((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
                putOrAdd(lMap, eventType, createProxy(eventType));
            }
            
            List<Type> types = new ArrayList<Type>(Arrays.asList(clazz.getGenericInterfaces()));
            for (Type t : types) {
                Class<?> eventType = (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[0];
                putOrAdd(lMap, eventType, createProxy(eventType));
            }
        }
        return lMap;
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> EventListener<E> createProxy(Class<?> eventType) throws Exception {
        EventListener<E> proxy = (EventListener<E>) ProxyHelper.createProxy(camelContext
                .getEndpoint(getRouter().createRoute(eventType)), EventListener.class);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> EventListener<E> createRouteProxy(Class<?> eventType) throws Exception {
        EventListener<E> proxy = (EventListener<E>) ProxyHelper.createProxy(camelContext
                .getEndpoint(getRouter().createBuilderRoute(eventType)), EventListener.class);
        return proxy;
    }
    @SuppressWarnings("unchecked")
    private <E extends Event> void putOrAdd(Map<Class<?>, Collection<EventListener<E>>> lMap, Class<?> eventType,
            EventListener<Event> eventListener) {
        if (lMap.containsKey(eventType)) {
            Collection<EventListener<E>> collection = lMap.get(eventType);
            collection.add((EventListener<E>) eventListener);
            lMap.put(eventType, collection);
        } else {
            ArrayList<EventListener<E>> list = new ArrayList<EventListener<E>>();
            list.add((EventListener<E>) eventListener);
            lMap.put(eventType, list);
        }
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
