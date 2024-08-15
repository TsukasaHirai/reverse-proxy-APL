package reverse.proxy.apl.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class Aop {

    private final Tracer tracer;

    @Around("execution(* reverse.proxy.apl.repository..*(..))")
    public Object aroundDbAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.spanBuilder("DB_Span").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("method_name", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            span.end();
        }
    }
}
