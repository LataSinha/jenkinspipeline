package com.nagarro.miniassignment.webClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class WebClientService {

    private final WebClient api1WebClient;
    private final WebClient api2WebClient;
    private final WebClient api3WebClient;
    private final Executor executor;

    private JsonNode api1Response;
    private JsonNode api2Response;
    private JsonNode api3Response;

    @Autowired
    public WebClientService(
            @Qualifier("api1WebClient") WebClient api1WebClient,
            @Qualifier("api2WebClient") WebClient api2WebClient,
            @Qualifier("api3WebClient") WebClient api3WebClient,
            Executor executor
    ) {
        this.api1WebClient = api1WebClient;
        this.api2WebClient = api2WebClient;
        this.api3WebClient = api3WebClient;
        this.executor = executor;
    }

    public void executeParallelApiCalls() {
        // Calling API 1 to get a random user
        Mono<JsonNode> randomUserMono = api1WebClient.get()
                .retrieve()
                .bodyToMono(JsonNode.class);

        api1Response = randomUserMono.block();
        System.out.println("API 1 Response: " + api1Response);

        // Extracting the name from API 1 response
        String userName = api1Response.get("results").get(0).get("name").get("first").asText();

        // CompletableFuture to execute API 2 and API 3 in parallel
        CompletableFuture<Void> api2Future = CompletableFuture.runAsync(() -> {
            Mono<JsonNode> nationalizeMono = api2WebClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("name", userName).build())
                    .retrieve()
                    .bodyToMono(JsonNode.class);

            api2Response = nationalizeMono.block();
            System.out.println("API 2 Response: " + api2Response);
        }, executor);

        CompletableFuture<Void> api3Future = CompletableFuture.runAsync(() -> {
            Mono<JsonNode> genderizeMono = api3WebClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("name", userName).build())
                    .retrieve()
                    .bodyToMono(JsonNode.class);

            api3Response = genderizeMono.block();
            System.out.println("API 3 Response: " + api3Response);
        }, executor);

        // Waiting for both CompletableFuture to complete
        CompletableFuture.allOf(api2Future, api3Future).join();
    }

    public JsonNode getApi1Response() {
        return api1Response;
    }

    public JsonNode getApi2Response() {
        return api2Response;
    }

    public JsonNode getApi3Response() {
        return api3Response;
    }
}
