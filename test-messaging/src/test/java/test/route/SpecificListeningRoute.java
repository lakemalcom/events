package test.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import test.AbstractListeningRouteBuilder;
import test.EventTest.TestEvent1;

public class SpecificListeningRoute extends AbstractListeningRouteBuilder<TestEvent1> {

    @Override
    public void configure() throws Exception {
        onEvent().process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println(SpecificListeningRoute.this.getClass() + " received event "
                        + exchange.getIn().getBody());
            }
        });
    }
}
