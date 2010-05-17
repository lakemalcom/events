package test;

public class JmsEventRouter implements EventRouter {
	private static final String COMPONENT = "jms:";

	@Override
	public String createRoute(Class<?> eventType) {
		return COMPONENT + eventType.getCanonicalName();
	}

}
