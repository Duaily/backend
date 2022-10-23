package com.kusitms.backend.config;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kusitms.backend.response.ExceptionResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(403, "해당 자원에 대한 접근 권한이 없습니다.");
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("utf-8");
    new ObjectMapper().writeValue(response.getWriter(), exceptionResponse);
  }
}
