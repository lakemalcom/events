package test;

public class SedaEventRouter implements EventRouter {
	private static final String COMPONENT = "seda:";

	@Override
	public String createRoute(Class<?> eventType) {
		return COMPONENT + eventType.getCanonicalName();
	}

}
