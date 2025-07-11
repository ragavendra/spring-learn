package com.stopsnearme.app.ws.shared;
    

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RateLimitingFilter implements Filter {

    // Map to store request counts per IP address
    private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    
    // Maximum requests allowed per minute
    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// filter only for not this path 
		if(!httpServletRequest.getRequestURI().endsWith("/users")){
			// Allow the request to proceed
			chain.doFilter(request, response);
			return;
		}

        String clientIpAddress = httpServletRequest.getRemoteAddr();
        
        // Initialize request count for the client IP address
        requestCountsPerIpAddress.putIfAbsent(clientIpAddress, new AtomicInteger(0));
        AtomicInteger requestCount = requestCountsPerIpAddress.get(clientIpAddress);
        
        // Increment the request count
        int requests = requestCount.incrementAndGet();
		// logger.log(Level.WARNING, "Request is {0}", httpServletRequest.getRequestURI());

        // Check if the request limit has been exceeded
        if (requests > MAX_REQUESTS_PER_MINUTE) {
            httpServletResponse.setStatus(HttpServletResponse.SC_SEE_OTHER);
            httpServletResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        // Allow the request to proceed
        chain.doFilter(request, response);

        // Optional: Reset request counts periodically (not implemented in this simple example)
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional: Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'destroy'");
    }
}

 
