package monitoringtools.methodcall.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import monitoringtools.methodcall.MethodMonitor;
import monitoringtools.methodcall.model.MonitoredMethodCall;

@WebServlet(urlPatterns="compareresultsreport")
public class CompareResultsReport extends HttpServlet {
	
	@Inject
	protected MethodMonitor monitor;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String html = "<html><body><form action='compareresultsreport' method='POST' enctype='multipart/form-data'>"+
				"<input name='oldResult' type='file' />" +
				"<input type='submit' title='Ok'/>"+
				"</form></body></html>";
		response.getWriter().println(html);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String oldMethodExecuted;
		long oldTimeExecuted;
		long diffTime;
		response.getWriter().println("<html><body>");
		reader.readLine();
		reader.readLine();
		reader.readLine();
		reader.readLine();
		String[] splitted;
		for (String line; (line = reader.readLine()) != null;) {
			splitted = line.split(" - ");
			if(splitted.length==2){
				oldMethodExecuted = splitted[0];
				oldTimeExecuted = Long.parseLong(splitted[1].replace("ms", "").trim());
				
				for(MonitoredMethodCall method : monitor){
					if(method.getMethodName().startsWith(oldMethodExecuted)){
						diffTime = method.getTimeExecuted() - oldTimeExecuted;
						response.getWriter().println(method.getMethodName() + " - <b style='"+(diffTime>0?"color:red;":"color:blue;")+"'>"+diffTime+"</b> ms<br/>");
						break;
					}
				}
			}
		}
		response.getWriter().println("</body></html>");
	}
}

