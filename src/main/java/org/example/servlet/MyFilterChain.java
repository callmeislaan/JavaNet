package org.example.servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyFilterChain implements FilterChain {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

    }
}
