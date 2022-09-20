package com.vector.test.config;//package cn.com.enersun.dgpmicro.config;
//
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Configuration
//public class CorsFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
////        String origin = request.getHeader("Origin");
////        response.setHeader("Access-Control-Allow-Origin", origin);
////        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
////        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        String method = request.getMethod();
//        if (method.equalsIgnoreCase("OPTIONS")) {
//            servletResponse.getOutputStream().write("Success".getBytes("utf-8"));
//        } else {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
