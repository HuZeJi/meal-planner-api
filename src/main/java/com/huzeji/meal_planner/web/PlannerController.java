package com.huzeji.meal_planner.web;

import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.model.dto.DailyPlanDto;
import com.huzeji.meal_planner.model.dto.WeeklyPlanDto;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListRecommendationDto;
import com.huzeji.meal_planner.service.PlannerSvc;
import com.huzeji.meal_planner.service.impl.OpenAISvcImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping( "/private/api/v1/planner")
public class PlannerController {
    private final PlannerSvc plannerSvc;
    private final OpenAISvcImpl openAISvcImpl;
    public PlannerController( PlannerSvc plannerSvc, OpenAISvcImpl openAISvcImpl) {
        this.plannerSvc = plannerSvc;
        this.openAISvcImpl = openAISvcImpl;
    }

    // TODO: fix when no user is located
    @GetMapping( "/daily-plan/user/{userId}/date/{date}" )
    @CrossOrigin(origins = "*") // Disable CORS for this specific endpoint
    public DailyPlanDto getDailyPlan(
            @PathVariable( "userId") UUID userId
            , @PathVariable("date") LocalDate date
    ) {
        return this.plannerSvc.getDailyPlan( date, userId);
    }

    @GetMapping( "/user/{userId}/date/{date}" )
    @CrossOrigin(origins = "*") // Disable CORS for this specific endpoint
    public WeeklyPlanDto getWeeklyPlan(
            @PathVariable( "userId") UUID userId
            , @PathVariable("date") LocalDate date
    ) {
        return this.plannerSvc.getWeeklyPlan( date, userId);
    }

    @PostMapping()
    @CrossOrigin(origins = "*") // Disable CORS for this specific endpoint
    public WeeklyPlanEntity generateWeeklyPlan( @RequestBody WeeklyPlanDto weeklyPlan ) {
        return this.plannerSvc.generateWeeklyPlan( weeklyPlan );
    }

    @PostMapping( "/chat" )
    public Mono<String> getChatResponse( @RequestBody String prompt ) {
        System.out.println(prompt);
        return openAISvcImpl.getChatResponse(prompt);
    }

    @GetMapping( "/shopping-list/weekly-plan/{weeklyPlanId}" )
    @CrossOrigin(origins = "*") // Disable CORS for this specific endpoint
    public Mono<ShoppingListRecommendationDto> getShoppingList(
            @PathVariable( "weeklyPlanId" ) UUID weeklyPlanId
    ) {
//        return openAIService.getShoppingList();
        return plannerSvc.getShoppingList( weeklyPlanId );
    }
}
