package test.route;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import test.AbstractRouteListener;
import test.EventListener;
import test.EventRouteStrategy;

public class EventRouteBuilder extends RouteBuilder implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private EventRouteStrategy router;

    @SuppressWarnings("unchecked")
    @Override
    public void configure() throws Exception {
        Collection<EventListener<?>> values = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
                EventListener.class).values();
        for (EventListener<?> l : values) {
            Class<?> clazz = l.getClass();
            List<Type> types = new ArrayList<Type>(Arrays.asList(clazz.getGenericInterfaces()));
            if (l instanceof AbstractRouteListener) {
                types.add(clazz.getGenericSuperclass());
            }
            for (Type t : types) {
                    Class<?> eventType = (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[0];
                    from(router.createRoute(eventType)).bean(l);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
