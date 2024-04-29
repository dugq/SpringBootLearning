package com.example.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Component
@Slf4j
public class MyHandlerException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,@NonNull Exception ex) {
        log.error("{} - {}", request.getRequestURI(), ex.getMessage(), ex);
        try {
            response.getWriter().write(ex.getMessage());
        } catch (IOException e) {
            log.warn("request closed");
        }
        return null;
    }
}
