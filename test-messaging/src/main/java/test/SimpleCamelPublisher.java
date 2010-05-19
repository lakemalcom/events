package test;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class SimpleCamelPublisher implements InitializingBean, EventPublisher, CamelContextAware {

    private CamelContext camelContext;
    
    //TODO: make this a tree
    private Map<Class<?>, ProducerTemplate> listenerRegistry;

    private EventRouteStrategy router;

    @Override
    public void afterPropertiesSet() throws Exception {
        listenerRegistry = new HashMap<Class<?>, ProducerTemplate>();
        listenerRegistry.put(Event.class, createProducer(Event.class));
    }

    private ProducerTemplate createProducer(Class<?> eventType) {
        Endpoint e = camelContext.getEndpoint(router.createRoute(eventType));
        return  new DefaultProducerTemplate(camelContext, e);
    }

    @Override
    public <E extends Event> void publish(E event) {
        register(event.getClass());
        for (Class<?> c : listenerRegistry.keySet()) {
            if (c.isAssignableFrom(event.getClass())) {
                ProducerTemplate p = listenerRegistry.get(c);
                p.sendBody(event);
            }
        }
    }

    private void register(Class<?> eventClass) {
        Class<?> currentClass = eventClass;
        while(Event.class.isAssignableFrom(currentClass)) {
            if (!listenerRegistry.containsKey(currentClass)) {
                listenerRegistry.put(currentClass, createProducer(currentClass));
            }
            currentClass = currentClass.getSuperclass();
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

    @Required
    public void setRouter(EventRouteStrategy router) {
        this.router = router;
    }
}
