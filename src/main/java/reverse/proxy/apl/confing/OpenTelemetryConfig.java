package reverse.proxy.apl.confing;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.instrumentation.resources.ContainerResource;
import io.opentelemetry.instrumentation.resources.HostResource;
import io.opentelemetry.instrumentation.resources.OsResource;
import io.opentelemetry.instrumentation.resources.ProcessResource;
import io.opentelemetry.instrumentation.resources.ProcessRuntimeResource;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.ResourceAttributes;

/**
 * Opentemetry用の設定クラス
 */
@Configuration
public class OpenTelemetryConfig {
	
	private static final Logger LOG = Logger.getLogger(OpenTelemetryConfig.class.getName());

	/**
	 * OpenTelemetryの設定を作成する
	 * 
	 * @return
	 */
	@Bean
	OpenTelemetry openTelemetry() {
		
		if (GlobalOpenTelemetry.get() != null) {
			// Eclipseリスタート用、すでに登録作成済みの場合は、作成した設定を返却する。
			LOG.info("OpenTelemetryの設定がすでに作成されているため、削除をしてから再作成をします。");
			GlobalOpenTelemetry.resetForTest();
		}
		
		Resource resource = Resource.getDefault()
				.merge(ContainerResource.get())
				.merge(HostResource.get())
				.merge(OsResource.get())
				.merge(ProcessResource.get())
				.merge(ProcessRuntimeResource.get())
				.merge(Resource.create(Attributes.builder()
						.put(ResourceAttributes.SERVICE_NAME, "reverse-proxy")
						.build()));
		
		SpanExporter spanExporter = OtlpHttpSpanExporter.builder().build();
		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
				.setResource(resource)
				.build();
		
		return OpenTelemetrySdk.builder()
				.setTracerProvider(sdkTracerProvider)
				.buildAndRegisterGlobal();
	}
}
