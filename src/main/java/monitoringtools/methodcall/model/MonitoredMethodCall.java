package monitoringtools.methodcall.model;

public class MonitoredMethodCall implements Comparable<MonitoredMethodCall>{

	private long timeExecuted;
	private String methodName;
	private StackTraceElement[] stackTrace;
	private long whenExecuted;
	
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

	public void setTimeExecuted(long timeExecuted) {
		this.timeExecuted = timeExecuted;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace=stackTrace;
	}
	
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public long getWhenExecuted() {
		return whenExecuted;
	}

	public void setWhenExecuted(long whenExecuted) {
		this.whenExecuted = whenExecuted;
	}
}

