package reverse.proxy.apl.filter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;
import reverse.proxy.apl.util.TraceUtil;

public class Httpfilter implements Filter {
	
	public static final List<String> sampleOffUrlList = List.of(
			"/actuator/prometheus",
			"/favicon.ico"
		);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String url = ((HttpServletRequest)request).getRequestURI();
		if (sampleOffUrlList.contains(url)) {
			TraceUtil.setSampleOff();
		}
	
		Span span = TraceUtil.startSpan("reverse-proxy-apl");
		try (Scope scope = span.makeCurrent()) {
			TraceUtil.registRootSpan(span);
			span.setAttribute("url", ((HttpServletRequest) request).getRequestURI());
			chain.doFilter(request, response);
		} catch (Exception e) {
			span.setAttribute("System Error", e.getMessage());
			span.setStatus(StatusCode.ERROR);
		} finally {
			span.end();
			TraceUtil.rootSpanClear();
		}
	}
	
}
