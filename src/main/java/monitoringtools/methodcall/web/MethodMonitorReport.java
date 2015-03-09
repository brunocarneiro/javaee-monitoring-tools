package monitoringtools.methodcall.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitoringtools.methodcall.MethodMonitor;
import monitoringtools.methodcall.model.MonitoredMethodCall;

@WebServlet(urlPatterns="/monitorreport")
public class MethodMonitorReport  extends HttpServlet {

	@Inject
	protected MethodMonitor monitor;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(req.getParameter("clear")!=null){
			monitor.clear();
		}
		
		PrintWriter respWriter = resp.getWriter();
		
		respWriter.println("<html><body><title>Relatorio de tempo de execução</title>");
		respWriter.println("<a href=\"monitorreport?clear=true\" >Clear Stats</a><br/>");
		for (MonitoredMethodCall monitoredMethod : monitor) {
			respWriter.println("<a href=\"methodstacktracereport?methodName="+monitoredMethod.getMethodName()+"\" >"+monitoredMethod.getMethodName()+"</a> - "+monitoredMethod.getTimeExecuted()+"ms <br/>");
		}
		respWriter.println("</body></html>");
	}
	
}
