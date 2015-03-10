package monitoringtools.methodcall;

import java.util.Iterator;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;

import monitoringtools.methodcall.model.MonitoredMethodCall;

@ApplicationScoped
public class MethodMonitor implements Iterable<MonitoredMethodCall>{

	private TreeSet<MonitoredMethodCall> monitoredMethods = new TreeSet<MonitoredMethodCall>();
	
	private static final int MAX_SIZE = 30;
	
	public synchronized void addMethod(MonitoredMethodCall m){
		if(monitoredMethods.size()==MAX_SIZE && monitoredMethods.last().compareTo(m)>0){
			monitoredMethods.remove(monitoredMethods.last());
		}
		monitoredMethods.add(m);
	}

	@Override
	public Iterator<MonitoredMethodCall> iterator() {
		TreeSet<MonitoredMethodCall> copy = new TreeSet<MonitoredMethodCall>(monitoredMethods);
		return new MethodMonitorIterator(copy.iterator());
	}
	
	public boolean isLongestEnougth(long time){
		return monitoredMethods.isEmpty() || monitoredMethods.last().getTimeExecuted()<time;
	} 
	
	public synchronized void clear(){
		monitoredMethods.clear();
	}

}

class MethodMonitorIterator implements Iterator<MonitoredMethodCall> {
	
	private Iterator<MonitoredMethodCall> it;
	
	public MethodMonitorIterator(Iterator<MonitoredMethodCall> it){
		this.it=it;
	}
	
	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public MonitoredMethodCall next() {
		return it.next();
	}

	@Override
	public void remove() {
		throw new RuntimeException("Not allowed");
	}
}