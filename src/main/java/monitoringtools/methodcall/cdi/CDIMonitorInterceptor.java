package monitoringtools.methodcall.cdi;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import monitoringtools.methodcall.MethodMonitor;
import monitoringtools.methodcall.model.MonitoredMethodCall;

@Monitored
@Interceptor
public class CDIMonitorInterceptor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
