package test;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import test.Event;
import test.EventListener;


public abstract class AbstractRouteListener<E extends Event> implements EventListener<E>, CamelContextAware {

    private CamelContext camelContext;
    
    @Autowired
    private EventRouteStrategy router;

    @Override
    public void receive(E event) {
        Endpoint endpoint = camelContext.getEndpoint(router.createBuilderRoute(event.getClass()));
        ProducerTemplate p = new DefaultProducerTemplate(camelContext, endpoint);
        p.sendBody(event);
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

}
