package test;

public class SedaEventRouter implements EventRouteStrategy {
	private static final String COMPONENT = "seda:";

	@Override
	public String createRoute(Class<?> eventType) {
        return COMPONENT + eventType.getCanonicalName()+ "?multipleConsumers=true";
	}

    @Override
    public String createBuilderRoute(Class<?> eventType) {
        return COMPONENT + eventType.getCanonicalName()+ "-routed" + "?multipleConsumers=true";
    }

}
