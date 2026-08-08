package com.self.study.flashsale.flashsale.config;

import java.time.Duration;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingReceiverTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingSenderTracingObservationHandler;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelPropagator;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class TracingConfig {

    @Value("${spring.application.name:flashsale}")
    private String applicationName;

    @Value("${management.otlp.tracing.endpoint:http://localhost:4318/v1/traces}")
    private String endpoint;

    @Bean
    public SpanExporter otlpHttpSpanExporter() {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint(endpoint)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SdkTracerProvider sdkTracerProvider(SpanExporter spanExporter) {
        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.of(AttributeKey.stringKey("service.name"), applicationName)));

        BatchSpanProcessor spanProcessor = BatchSpanProcessor.builder(spanExporter)
                .setScheduleDelay(Duration.ofMillis(500))
                .build();

        return SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(spanProcessor)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextPropagators contextPropagators() {
        return ContextPropagators.create(B3Propagator.injectingMultiHeaders());
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenTelemetrySdk openTelemetrySdk(SdkTracerProvider sdkTracerProvider, ContextPropagators contextPropagators) {
        return OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(contextPropagators)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public io.opentelemetry.api.trace.Tracer otelTracer(OpenTelemetrySdk openTelemetrySdk) {
        return openTelemetrySdk.getTracer(applicationName);
    }

    @Bean
    @ConditionalOnMissingBean
    public OtelCurrentTraceContext otelCurrentTraceContext() {
        return new OtelCurrentTraceContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public Tracer micrometerTracer(io.opentelemetry.api.trace.Tracer otelTracer, OtelCurrentTraceContext otelCurrentTraceContext) {
        return new OtelTracer(otelTracer, otelCurrentTraceContext, event -> {});
    }

    @Bean
    @ConditionalOnMissingBean
    public OtelPropagator otelPropagator(ContextPropagators contextPropagators, io.opentelemetry.api.trace.Tracer otelTracer) {
        return new OtelPropagator(contextPropagators, otelTracer);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public DefaultTracingObservationHandler defaultTracingObservationHandler(Tracer tracer) {
        return new DefaultTracingObservationHandler(tracer);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public PropagatingSenderTracingObservationHandler<?> propagatingSenderTracingObservationHandler(Tracer tracer, OtelPropagator otelPropagator) {
        return new PropagatingSenderTracingObservationHandler<>(tracer, otelPropagator);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public PropagatingReceiverTracingObservationHandler<?> propagatingReceiverTracingObservationHandler(Tracer tracer, OtelPropagator otelPropagator) {
        return new PropagatingReceiverTracingObservationHandler<>(tracer, otelPropagator);
    }
}
