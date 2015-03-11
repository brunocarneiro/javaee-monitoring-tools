package monitoringtools.methodcall;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import monitoringtools.methodcall.cdi.Monitored;
import monitoringtools.methodcall.model.MonitoredMethodCall;

@Monitored
@Interceptor
public class CDIMonitorInterceptor implements Serializable {
	
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
				MonitoredMethodCall monitoredMethodCall = new MonitoredMethodCall();
				monitoredMethodCall.setMethodName(ctx.getMethod().getDeclaringClass().getName()+"."+ ctx.getMethod().getName()+" ("+getParametersName(ctx)+")");
				monitoredMethodCall.setStackTrace(Thread.currentThread().getStackTrace());
				monitoredMethodCall.setTimeExecuted(methodMeasuredTime);
				monitoredMethodCall.setWhenExecuted(whenExecuted);
				methodMonitor.addMethod(monitoredMethodCall);
			}
		}
	}

	protected String getParametersName(InvocationContext ctx) {
		StringBuffer buf = new StringBuffer();
		for(Class<?> clazz : ctx.getMethod().getParameterTypes()){
			if(buf.length()!=0){
				buf.append(", ");
			}
			buf.append(clazz.toString());
		}
		return buf.toString();
	}

}
