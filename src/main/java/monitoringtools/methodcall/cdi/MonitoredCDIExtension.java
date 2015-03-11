package monitoringtools.methodcall.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ServiceLoader;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

/**
 * CDI Plugin that enables monitoring interceptor dynamically 
 * (without annotating a class).
 * 
 * @author bruno.carneiro
 *
 */
public class MonitoredCDIExtension implements Extension {
	
	private ServiceLoader<CDIMonitorInterceptorConfiguration> configurationLoader =ServiceLoader.load(CDIMonitorInterceptorConfiguration.class);

	public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> processAnnotatedType) {
		
		
		AnnotatedType<T> annotatedType = processAnnotatedType.getAnnotatedType();
		
		Class<T> javaClass = annotatedType.getJavaClass();
		if(shouldIntercept(javaClass)){
			Annotation monitoredAnnotation = new AnnotationLiteral<Monitored>() {};
			AnnotatedTypeWrapper<T> wrapper = new AnnotatedTypeWrapper<T>(annotatedType, annotatedType.getAnnotations());
			wrapper.addAnnotation(monitoredAnnotation);
			processAnnotatedType.setAnnotatedType(wrapper);
		}
	}

	protected <T> boolean shouldIntercept(Class<T> javaClass) {
		return isNotFinal(javaClass) && 
				isNotEJB(javaClass) && 
				hasNotStaticOrFinalMethod(javaClass) &&
				notMonitoringToolsClass(javaClass) &&
				(containsAnyAnnotationConfigured(javaClass) || 
						isInstanceOfAnyConfiguredClass(javaClass));
	}

	protected <T> boolean isNotFinal(Class<T> javaClass) {
		return !Modifier.isFinal(javaClass.getModifiers());
	}
	
	protected boolean isNotEJB(Class<?> clazz){
		return !(clazz.getAnnotation(Stateless.class)!=null || clazz.getAnnotation(Stateful.class) !=null);
	}
	
	protected boolean hasNotStaticOrFinalMethod(Class<?> clazz){
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if(Modifier.isFinal(method.getModifiers()) || Modifier.isStatic(method.getModifiers())){
				return false;
			}
		}
		return true;
	}
	
	protected boolean notMonitoringToolsClass(Class<?> clazz){
		return !clazz.getName().startsWith("monitoringtools");
	}
	
	protected boolean containsAnyAnnotationConfigured(Class<?> javaClass){
		for(CDIMonitorInterceptorConfiguration configuration :configurationLoader){
			if(configuration.annotatedClassesToBeIntercept()!=null){
				for(Class<? extends Annotation> annotationClass : configuration.annotatedClassesToBeIntercept()){
					if(javaClass.getAnnotation(annotationClass)!=null){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	protected boolean isInstanceOfAnyConfiguredClass(Class<?> javaClass){
		for(CDIMonitorInterceptorConfiguration configuration :configurationLoader){
			if(configuration.classesToBeIntercepted()!=null){
				for(Class<?> clazz : configuration.classesToBeIntercepted()){
					if(clazz.isAssignableFrom(javaClass)){
						return true;
					}
				}
			}
		}
		return false;
	}
}
