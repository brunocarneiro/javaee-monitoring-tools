package monitoringtools.methodcall.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitoringtools.methodcall.MethodMonitor;
import monitoringtools.methodcall.model.MonitoredMethodCall;

@WebServlet(urlPatterns="/methodstacktracereport")
public class MethodStackTraceReport extends HttpServlet {
	
	@Inject
	protected MethodMonitor monitor;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String methodName = req.getParameter("methodName");
		
		for (MonitoredMethodCall monitoredMethod : monitor) {
			if(monitoredMethod.getMethodName().equals(methodName)){
				resp.getWriter().println(methodName);
				for(StackTraceElement stackElem : monitoredMethod.getStackTrace()){
					resp.getWriter().println(stackElem);
				}
			}
		}
	}

}
