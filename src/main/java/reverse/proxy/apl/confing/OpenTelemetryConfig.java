package reverse.proxy.apl.confing;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import lombok.RequiredArgsConstructor;
import reverse.proxy.apl.util.TraceUtil;

/**
 * Opentemetry用の設定クラス
 */
@RequiredArgsConstructor
@Configuration
public class OpenTelemetryConfig {
	
	@Bean
	public AutoConfigurationCustomizerProvider otelCustomizer() {
		return p -> p.addSamplerCustomizer((sampler, configProperties) -> getCustomSampler());
	}
	
	
	@Bean
	public Tracer initOpenTelemetry(OpenTelemetry openTelemetry) {
		Tracer tracer = openTelemetry.getTracer("default");
		TraceUtil.init(openTelemetry);
		return tracer;
	}
	
	/**
	 * カスタム{@Link Sampler}
	 * 
	 * @return
	 */
	private Sampler getCustomSampler() {
		
		return new Sampler() {
			
			@Override
			public String getDescription() {

				return "CustomSpamler";
			}
			
			@Override
			public SamplingResult shouldSample(Context parentContext, String traceId, String name, SpanKind spanKind,
					Attributes attributes, List<LinkData> parentLinks) {
				return TraceUtil.isSampleOff() ? SamplingResult.drop() : SamplingResult.recordAndSample();
			}
		};
	}
	
}
