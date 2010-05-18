package test;

import java.lang.reflect.ParameterizedType;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractListeningRouteBuilder<E extends Event> extends RouteBuilder {
    
    @Autowired
    EventRouteStrategy router;

    public ProcessorDefinition<?> onEvent() {
        Class<?> t = (Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return from(router.createRoute(t)).bean(BeanInvocationRemover.class);
    }

}
