package monitoringtools.methodcall.ejb;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import monitoringtools.methodcall.MethodMonitor;
import monitoringtools.methodcall.model.MonitoredMethodCall;

public class EJBMonitorInterceptor {
	
	@Inject
	private MethodMonitor methodMonitor;
	
	@AroundInvoke
	public Object interceptAllRemoteInvocations (InvocationContext ctx) throws Exception {
		
		long whenExecuted, methodMeasuredTime;
		whenExecuted = methodMeasuredTime = System.currentTimeMillis();
		try{
			Object result =  ctx.proceed();
			return result;
		} finally{
			methodMeasuredTime=System.currentTimeMillis()-methodMeasuredTime;
			if(methodMonitor.isLongestEnougth(methodMeasuredTime)){
				MonitoredMethodCall monitoredMethodCall = new MonitoredMethodCall(ctx.getMethod(), methodMeasuredTime, whenExecuted);
				methodMonitor.addMethod(monitoredMethodCall);
			}
		}
	}

}
