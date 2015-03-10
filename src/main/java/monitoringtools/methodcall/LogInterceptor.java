package monitoringtools.methodcall;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

public class LogInterceptor {
	
	@AroundInvoke
	public Object interceptAllInvocations (InvocationContext ctx) throws Exception {
		
		Logger log = Logger.getLogger(ctx.getMethod().getDeclaringClass());
		log.info("LogInterceptor - "+ctx.getMethod().toString());
		System.out.println("LogInterceptor - "+ctx.getMethod().toString());
		return ctx.proceed();
	}

}
