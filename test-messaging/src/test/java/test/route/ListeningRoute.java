package test.route;

import test.AbstractListeningRouteBuilder;
import test.Event;

public class ListeningRoute extends AbstractListeningRouteBuilder<Event>{

    @Override
    public void configure() throws Exception {
        onEvent().inOnly().to("stream:out");
    }

}
