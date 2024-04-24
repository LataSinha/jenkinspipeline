package com.nagarro.miniassignment.confg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean(name = "api1WebClient")
    public WebClient api1WebClient() {
        return WebClient.builder()
                .baseUrl("https://randomuser.me/api")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(getHttpClient(2000)))
                .build();
    }

    @Bean(name = "api2WebClient")
    public WebClient api2WebClient() {
        return WebClient.builder()
                .baseUrl("https://api.nationalize.io/")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(getHttpClient(1000)))
                .build();
    }

    @Bean(name = "api3WebClient")
    public WebClient api3WebClient() {
        return WebClient.builder()
                .baseUrl("https://api.genderize.io/")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(getHttpClient(1000)))
                .build();
    }

    private HttpClient getHttpClient(int timeoutMillis) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS));
                });
    }
}
