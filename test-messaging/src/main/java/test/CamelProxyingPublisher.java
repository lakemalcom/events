package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        listenerRegistry = proxyListeners();
    }

    @Override
    public <E extends Event> void publish(E event) {
        for (Class<?> c : listenerRegistry.keySet()) {
            if (c.isAssignableFrom(event.getClass())) {
                EventListener<Event> l = listenerRegistry.get(c);
                l.receive(event);
            }
        }
    }

    public <E extends Event> Map<Class<?>, EventListener<E>> proxyListeners() throws Exception {
        Map<Class<?>, EventListener<E>> lMap = new HashMap<Class<?>, EventListener<E>>();
        for (EventListener<? extends Event> l : getListeners()) {
            Class<?> clazz = l.getClass();
            List<Type> types = new ArrayList<Type>(Arrays.asList(clazz.getGenericInterfaces()));
            if (l instanceof AbstractRouteListener<?>) {
                types.add(clazz.getGenericSuperclass());
            }

            for (Type t : types) {
                Class<?> eventType = (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[0];
                if (!lMap.containsKey(eventType)) {
                    lMap.put(eventType, (EventListener<E>) createProxy(eventType));
                }
            }
        }
        return lMap;
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> EventListener<E> createProxy(Class<?> eventType) throws Exception {
        EventListener<E> proxy = (EventListener<E>) ProxyHelper.createProxy(camelContext.getEndpoint(getRouter()
                .createRoute(eventType)), EventListener.class);
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
