package reverse.proxy.apl.confing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spring.autoconfigure.spanner.GcpSpannerProperties;

import io.opentelemetry.api.OpenTelemetry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SpannerConfig {
	
	private final GcpSpannerProperties gcpSpannerProperties;
	
	@Bean
	SpannerOptions spannerOptions(OpenTelemetry openTelemetry) {
		SpannerOptions.enableOpenTelemetryMetrics();
		SpannerOptions.enableOpenTelemetryTraces();
		return SpannerOptions.newBuilder()
				.setOpenTelemetry(openTelemetry)
				.setProjectId(gcpSpannerProperties.getProjectId())
				.setEmulatorHost(gcpSpannerProperties.getEmulatorHost())
				.setCredentials(NoCredentials.getInstance())
				.build();

	}
}
