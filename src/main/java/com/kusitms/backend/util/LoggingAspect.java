package com.kusitms.backend.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("within(*com.kusitms.backend.controller..*)")
  public Object logging(ProceedingJoinPoint point) throws Throwable {

    long startedAt = System.currentTimeMillis();

    log.info("-------> REQUEST : {} ({})",
        point.getSignature().getDeclaringTypeName(),
        point.getSignature().getName());

    Object result = point.proceed();

    long endedAt = System.currentTimeMillis();

    log.info("-------> RESPONSE : {}({}) = {} ({}ms)",
        point.getSignature().getDeclaringTypeName(),
        point.getSignature().getName(),
        result,
        endedAt - startedAt);

    return result;
  }

}
