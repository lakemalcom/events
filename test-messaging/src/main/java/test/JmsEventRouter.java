package test;

public class JmsEventRouter implements EventRouteStrategy {
	private static final String COMPONENT = "activemq:";

	@Override
	public String createRoute(Class<?> eventType) {
        return COMPONENT + "topic:" + eventType.getCanonicalName();
	}

    @Override
    public String createBuilderRoute(Class<?> eventType) {
        return COMPONENT + "topic:" + eventType.getCanonicalName() + "-routed";
    }

}
