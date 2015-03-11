package monitoringtools.methodcall.cdi;

import java.lang.annotation.Annotation;


public interface CDIMonitorInterceptorConfiguration {
	
	Class<? extends Annotation>[] annotatedClassesToBeIntercept();
	
	Class<?>[] classesToBeIntercepted();
}
