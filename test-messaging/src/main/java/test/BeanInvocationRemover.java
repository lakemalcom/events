package test;

import org.apache.camel.InOut;
import org.apache.camel.component.bean.BeanInvocation;

public class BeanInvocationRemover {
    @SuppressWarnings("unchecked")
    @InOut
    public <E extends Event> E identity(BeanInvocation b) {
        return (E) b.getArgs()[0];
    }
}
