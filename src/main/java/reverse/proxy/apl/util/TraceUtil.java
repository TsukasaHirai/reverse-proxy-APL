package reverse.proxy.apl.util;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;


public final class TraceUtil {
	
	private static OpenTelemetry openTelemetry = OpenTelemetry.noop();
	
	private static ThreadLocal<Span> threadLocal = new ThreadLocal<>();
	
	private static ThreadLocal<Boolean> isSampleOff = new ThreadLocal<>();
	
	private TraceUtil() {
		
	}
	
	public static void init(OpenTelemetry openTelemetry) {
		TraceUtil.openTelemetry = openTelemetry;
	}
	
	public static void registRootSpan(Span span) {
		threadLocal.set(span);
	}
	
	public static boolean isSampleOff() {
		return isSampleOff.get() != null;
	}
	
	public static void setSampleOff() {
		isSampleOff.set(true);
	}
	
	
	public static void rootSpanClear() {
		threadLocal.remove();
		isSampleOff.remove();
	}
	
	public static Span startSpan(String spanName) {
		return openTelemetry.getTracer(spanName).spanBuilder(spanName).startSpan();
	}
	
	
	public static void putAttributeCurrentAndRootSpan(Span span, String key, String value) {
		span.setAttribute(key, value);
		
		if (threadLocal.get() != null) {
			threadLocal.get().setAttribute(key, value);
		}
	}
	
}
