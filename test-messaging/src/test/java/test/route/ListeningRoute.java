package test.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import test.AbstractListeningRouteBuilder;
import test.Event;

public class ListeningRoute extends AbstractListeningRouteBuilder<Event>{

    @Override
    public void configure() throws Exception {
        onEvent().process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println(ListeningRoute.this.getClass() + " received event " + exchange.getIn().getBody());
            }
        });
    }

}
