package org.example.servlet;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MyServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        PrintWriter writer = res.getWriter();
        int number = 3;
        int square = number * number;
        String responseBody = String.format("<html>\r\n" +
                "<body>\r\n" +
                "<h1>Square application</h1>\r\n" +
                "<p>%d square is %d</p>\r\n" +
                "</body>\r\n" +
                "</html>\r\n", number, square);
        writer.print(responseBody);
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
