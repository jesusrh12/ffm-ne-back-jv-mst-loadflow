package com.nttdata.fulfillment.catalogdriver;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.nttdata.interceptor.AuthRequestInterceptor;

/**
 * Utility that allows to obtain the contexts of classes
 * @author lsotelod, 02/2022
 *
 */
public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext appCxt;

	/**
	 * Method that allows initializing the applicationContext
	 * 
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appCxt = applicationContext;
	}

	/**
	 * Method that allows obtaining the connection of the class mainWebSocket
	 * 
	 * @return
	 * @throws BeansException
	 */
	public static Config getConfig() throws BeansException {
		return (Config) appCxt.getAutowireCapableBeanFactory().getBean("config");
	}
	
	public static AuthRequestInterceptor getAuth() throws BeansException {
		return (AuthRequestInterceptor) appCxt.getAutowireCapableBeanFactory().getBean(AuthRequestInterceptor.class);
	}
	
	
	
	

}