package monitoringtools.methodcall.model;

import java.lang.reflect.Method;

public class MonitoredMethodCall implements Comparable<MonitoredMethodCall>{

	private long timeExecuted;
	private String methodName;
	private StackTraceElement[] stackTrace;
	private long whenExecuted;
	private long threadId = Thread.currentThread().getId();
	
	public MonitoredMethodCall(){}
	
	public MonitoredMethodCall(Method method, long timeExecuted, long whenExecuted){
		this.methodName=method.getDeclaringClass().getName()+"."+ method.getName()+" ("+getParametersName(method)+")";
		this.stackTrace=Thread.currentThread().getStackTrace();
		this.timeExecuted=timeExecuted;
		this.whenExecuted=whenExecuted;
	}
	
	protected String getParametersName(Method method) {
		StringBuffer buf = new StringBuffer();
		for(Class<?> clazz : method.getParameterTypes()){
			if(buf.length()!=0){
				buf.append(", ");
			}
			buf.append(clazz.toString());
		}
		return buf.toString();
	}
	
	@Override
	public int compareTo(MonitoredMethodCall o) {
		if(o!=null){
			if(o.timeExecuted>timeExecuted)
				return 1;
			else if (o.timeExecuted<timeExecuted)
				return -1;
			else
				return 0;
		}
		return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MonitoredMethodCall){
			MonitoredMethodCall other = (MonitoredMethodCall)obj;
			return other.getMethodName().equals(this.getMethodName()) && (other.getWhenExecuted()==this.getWhenExecuted());
		} else
			return false;
	}
	
	@Override
	public int hashCode() {
		return (this.getMethodName()+this.getWhenExecuted()).hashCode();
	}

	public long getTimeExecuted() {
		return timeExecuted;
	}

	public String getMethodName() {
		return methodName;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public long getWhenExecuted() {
		return whenExecuted;
	}

	public long getThreadId() {
		return threadId;
	}
}

