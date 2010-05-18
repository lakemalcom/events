package test;
import java.lang.reflect.ParameterizedType;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.InOut;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractRouteListener<E extends Event> implements EventListener<E>, CamelContextAware {

    private CamelContext camelContext;
    
    @Autowired
    private EventRouteStrategy router;

    @Override
    @InOut
    public void receive(E event) {
        Class<?> c = (Class<?>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Endpoint endpoint = camelContext.getEndpoint(router.createBuilderRoute(c));
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
