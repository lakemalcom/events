/**
 * 
 */
package test;

public interface EventRouteStrategy {
	String createRoute(Class<?> eventType);
	String createBuilderRoute(Class<?> eventType);
}