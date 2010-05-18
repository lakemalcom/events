package test;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringAwareEventPublisher extends AbstractEventPublisher
		implements InitializingBean, ApplicationContextAware {

	private Collection<? extends EventListener<? extends Event>> listeners;
	protected ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		setListeners(new ArrayList<EventListener<? extends Event>>(
				BeanFactoryUtils.beansOfTypeIncludingAncestors(
						applicationContext, EventListener.class).values()));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	Collection<? extends EventListener<? extends Event>> getListeners() {
		return listeners;
	}

	@Override
	void setListeners(
			Collection<? extends EventListener<? extends Event>> listeners) {
		this.listeners = listeners;
	}
}
