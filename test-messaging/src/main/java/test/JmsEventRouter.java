package test;

public class JmsEventRouter implements EventRouteStrategy {
	private static final String COMPONENT = "activemq:";
    private static final String EXCHANGE = "";

	@Override
	public String createRoute(Class<?> eventType) {
        return COMPONENT + "topic:" + eventType.getCanonicalName() + EXCHANGE;
	}

    @Override
    public String createBuilderRoute(Class<?> eventType) {
        return COMPONENT + eventType.getCanonicalName() + "-routed" + EXCHANGE;
    }

}
