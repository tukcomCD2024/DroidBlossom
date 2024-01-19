package site.timecapsulearchive.core.global.config.resttemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AligoRestTemplateConfig {

    private static final int MAX_CONNECTION_TOTAL = 10;
    private static final int MAX_CONNECTION_PER_ROUTE = 10;
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final String ALIGO_API_URL = "https://apis.aligo.in";

    @Bean
    public RestTemplate aligoRestTemplate(RestTemplateBuilder builder) {
        return builder
            .messageConverters(formHttpConverter(), mappingJackson2HttpMessageConverter())
            .requestFactory(this::clientHttpRequestFactory)
            .rootUri(ALIGO_API_URL)
            .build();
    }

    private FormHttpMessageConverter formHttpConverter() {
        FormHttpMessageConverter formConverter = new FormHttpMessageConverter();

        formConverter.addPartConverter(new ByteArrayHttpMessageConverter());
        formConverter.setMultipartCharset(StandardCharsets.UTF_8);
        formConverter.setSupportedMediaTypes(
            Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
        return formConverter;
    }

    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();

        jsonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        return jsonConverter;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECTION_TIMEOUT);
        factory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        factory.setHttpClient(httpClient());

        return factory;
    }

    private HttpClient httpClient() {
        return HttpClientBuilder.create()
            .setConnectionManager(connectionManager())
            .evictIdleConnections(TimeValue.ofSeconds(KEEP_ALIVE_SECONDS))
            .evictExpiredConnections()
            .build();
    }

    private HttpClientConnectionManager connectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(MAX_CONNECTION_TOTAL)
            .setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE)
            .build();
    }
}
