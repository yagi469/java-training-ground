package com.example.week1.aspect;

import com.example.week1.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExecutionTimeLoggerAspectTest {
  @Test
  void logsExecutionTime() throws Throwable {
    ExecutionTimeLoggerAspect aspect = new ExecutionTimeLoggerAspect();
    ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
    when(pjp.proceed()).thenReturn("result");
    when(pjp.getSignature()).thenReturn(Mockito.mock(org.aspectj.lang.Signature.class));
    when(pjp.getSignature().getDeclaringTypeName()).thenReturn(UserService.class.getName());
    when(pjp.getSignature().getName()).thenReturn("dummyMethod");

    Object ret = aspect.logExecutionTime(pjp);
    assertEquals("result", ret);
    // Verify that proceed() was called exactly once
    verify(pjp, times(1)).proceed();
  }
}
