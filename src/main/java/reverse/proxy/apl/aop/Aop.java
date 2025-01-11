package reverse.proxy.apl.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import reverse.proxy.apl.util.TraceUtil;

@Aspect
@Component
public class Aop {


    @Around("execution(* reverse.proxy.apl.repository..*(..))")
    public Object aroundDbAccess(ProceedingJoinPoint joinPoint) throws Throwable {
    	Span span = TraceUtil.startSpan("db_span");
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("method_name", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }
    
    @Around("execution(* reverse.proxy.apl.controller..*(..))")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = TraceUtil.startSpan("controller_span");
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("method_name", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }
    
}
