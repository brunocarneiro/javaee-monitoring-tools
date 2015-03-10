# javaee-monitoring-tools
Intercept classes to help finding out bottlenecks, execution path and more...

Usage:
ejb-jar.xml

	<assembly-descriptor>
		<interceptor-binding>
			<ejb-name>*</ejb-name>
			<interceptor-class>monitoringtools.methodcall.MonitorInterceptor</interceptor-class>
		</interceptor-binding>
	</assembly-descriptor>
	
pom.xml

	<dependency>
	  <artifactId>monitoring-tools</artifactId>
	  <groupId>com.github.brunocarneiro</groupId>
	  <version>0.0.1-SNAPSHOT</version>
	</dependency>
	
	
Check out Monitor stats:
http://[host]:[port]/[contextPath]/monitorreport
