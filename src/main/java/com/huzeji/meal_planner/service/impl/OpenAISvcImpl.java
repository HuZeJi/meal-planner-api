package com.huzeji.meal_planner.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzeji.meal_planner.model.DailyPlanEntity;
import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.model.dto.DailyPlanDto;
import com.huzeji.meal_planner.model.dto.gpt.config.*;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListRecommendationDto;
import com.huzeji.meal_planner.service.OpenAISvc;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class OpenAISvcImpl implements OpenAISvc {
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public OpenAISvcImpl( WebClient webClient ) {
        this.webClient = webClient;
    }

    @Override
    public Mono<ShoppingListRecommendationDto> generateShoppingList( WeeklyPlanEntity weeklyPlan ){
        ChatCompletionRequest request = createRequestBody( weeklyPlan );

        String response = webClient
                .post()
                .uri( API_URL )
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue( request )
                .retrieve()
                .bodyToMono(String.class)
                .map( this::parseOpenAIAPIResponse )
                .block();
        System.out.println( response );
        return Mono.just( parseOpenAIAPIResponseV2( response ) );
    }

    @Deprecated
    public Mono<String> getChatResponseTest( String prompt ) {
        return webClient.post()
//                .uri("https://webhook.site/e1c35845-f994-421f-8ee6-b7be6ac3cec7")
                .uri( API_URL )
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createRequestBody(null))
                .retrieve()
                .bodyToMono(String.class)
                .map( this::parseOpenAIAPIResponse );
    }


    // TODO: set this as response
    @Deprecated
    public Mono<String> getChatResponse(String prompt) {
        ChatCompletionRequest request = createRequestBody(null);
//        return Mono.just("You are a very good nutritionist that helps people to generate meal plans for the week and also the shopping list for the meal plans.");
        return webClient.post()
//                .uri("https://webhook.site/e1c35845-f994-421f-8ee6-b7be6ac3cec7")
                .uri(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(createRequestBody(null))
                .retrieve()
                .bodyToMono(String.class)
                .map( this::parseOpenAIAPIResponse );
    }

    private ShoppingListRecommendationDto parseOpenAIAPIResponseV2( String response ){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree( response );

            return objectMapper.treeToValue( jsonResponse, ShoppingListRecommendationDto.class );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    private String parseOpenAIAPIResponse( String response ){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response);
            return jsonResponse.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    private ChatCompletionRequest createRequestBody( WeeklyPlanEntity weeklyPlan ) {
        ChatCompletionRequest request = new ChatCompletionRequest();

        // TODO: Move this configurations to a database | environments | console
        request.setFrequencyPenalty( 0.0 );
        request.setMaxTokens( 16383 );
        request.setModel( "gpt-4o" );
        request.setPresencePenalty( 0.0 );
        request.setTemperature( 1.0 );
        request.setTopP( 1.0 );

        // TODO: transform this into a extension like class
        Message systemMessage = new Message();
        systemMessage.setRole( "system" );
        systemMessage.setContent( Collections.singletonList( new Content( "text", "You are a very good nutritionist that helps people to generate meal plans for the week and also the shopping list for the meal plans. The result output must be in spanish and the currency is GTQ." ) ) );

        String mealPlan = DailyPlanDto.generatePrompt( weeklyPlan.getDailyPlans() );
        System.out.println(mealPlan);

        Message userMessage = new Message();
        userMessage.setRole( "user" );
        userMessage.setContent( Collections.singletonList( new Content( "text", mealPlan ) ) );

        request.setMessages( Arrays.asList( systemMessage, userMessage ) );
        request.setResponseFormat( ResponseFormat.getConfigurations() );
        return request;
    }
}
