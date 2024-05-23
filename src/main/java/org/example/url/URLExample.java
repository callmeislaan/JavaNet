package org.example.url;

import java.net.MalformedURLException;
import java.net.URL;

public class URLExample {
    public static void main(String[] args) {
        try {
            // Create a URL object
            URL url = new URL("https://example.com:8080/docs/resource1.html?name=networking&abc=1#intro");

            // Access URL components
            System.out.println("Protocol: " + url.getProtocol());
            System.out.println("Host: " + url.getHost());
            System.out.println("Port: " + url.getPort());
            System.out.println("File: " + url.getFile());
            System.out.println("Query: " + url.getQuery());
            System.out.println("Path: " + url.getPath());
            System.out.println("Ref: " + url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}