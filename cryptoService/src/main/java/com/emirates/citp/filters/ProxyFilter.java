package com.emirates.citp.filters;

import javax.servlet.*;
import javax.servlet.http.*;

public class ProxyFilter implements Filter {
protected FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
	   this.filterConfig = filterConfig;
	   }
	
	public void destroy() {
	   this.filterConfig = null;
	   }
	
	
	public void doFilter(ServletRequest request, 
	ServletResponse response, FilterChain chain) 
	    throws java.io.IOException, ServletException   {
	    String IP = request.getRemoteAddr();
		System.out.println( "ip is "+IP);
	    chain.doFilter(request, response);  
	    
	    java.lang.String remoteip=( (HttpServletRequest) request).getHeader("X-FORWARDED-FOR");
	  System.out.println(" Remote I/P is "+remoteip);
	  
	  String s1 = ( (HttpServletRequest) request).getHeader("HTTP_X_FORWARDED_FOR");
	  String s2 = ( (HttpServletRequest) request).getHeader("HTTP-X-FORWARDED-FOR");
	  String s3 = ( (HttpServletRequest) request).getHeader("X_FORWARDED_FOR");
	  
	  String s4 = ( (HttpServletRequest) request).getHeader("X-FORWARD_FOR");
	  System.out.println(" Remote I/P s1 is "+s1);
	  System.out.println(" Remote I/P s2 is "+s2);
	  System.out.println(" Remote I/P s3 is "+s3);
	  System.out.println(" Remote I/P s4dcd is "+s4);
	            }
  }