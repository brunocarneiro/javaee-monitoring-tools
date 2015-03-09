package monitoringtools.methodcall;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import monitoringtools.methodcall.model.MonitoredMethodCall;

public class MonitorInterceptor {
	
	@Inject
	private MethodMonitor methodMonitor;
	
	@AroundInvoke
	public Object interceptAllRemoteInvocations (InvocationContext ctx) throws Exception {
		
		long time = System.currentTimeMillis();
		try{
			Object result =  ctx.proceed();
			return result;
		} finally{
			time=System.currentTimeMillis()-time;
			if(methodMonitor.isLongestEnougth(time)){
				MonitoredMethodCall monitoredMethodCall = new MonitoredMethodCall();
				monitoredMethodCall.setMethodName(ctx.getMethod().getDeclaringClass().getName()+"."+ ctx.getMethod().getName()+" ("+getParametersName(ctx)+")");
				monitoredMethodCall.setStackTrace(Thread.currentThread().getStackTrace());
				monitoredMethodCall.setTimeExecuted(time);
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
