package com.huzeji.meal_planner.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzeji.meal_planner.model.DailyPlanEntity;
import com.huzeji.meal_planner.model.MealEntity;
import com.huzeji.meal_planner.model.UserEntity;
import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.model.dto.DailyPlanDto;
import com.huzeji.meal_planner.model.dto.MealDto;
import com.huzeji.meal_planner.model.dto.WeeklyPlanDto;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListItemDto;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListRecommendationDto;
import com.huzeji.meal_planner.model.enums.StatusEnum;
import com.huzeji.meal_planner.repository.DailyPlanRepository;
import com.huzeji.meal_planner.repository.WeeklyPlanRepository;
import com.huzeji.meal_planner.service.OpenAISvc;
import com.huzeji.meal_planner.service.PlannerSvc;
import com.huzeji.meal_planner.service.UserSvc;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.*;

@Service
class PlannerSvcImpl implements PlannerSvc {
    private final WeeklyPlanRepository weeklyPlanRepository;
    private final UserSvc userSvc;
    private final OpenAISvc openAISvc;
    private final DailyPlanRepository dailyPlanRepository;
    public PlannerSvcImpl(
            WeeklyPlanRepository weeklyPlanRepository
            , UserSvc userSvc
            , OpenAISvc openAISvc
            , DailyPlanRepository dailyPlanRepository
    ) {
        this.weeklyPlanRepository = weeklyPlanRepository;
        this.userSvc = userSvc;
        this.openAISvc = openAISvc;
        this.dailyPlanRepository = dailyPlanRepository;
    }
    @Override
    public WeeklyPlanEntity generateWeeklyPlan( WeeklyPlanDto weeklyPlanDto ) {
        // Generate weekly plan from dto
        WeeklyPlanEntity weeklyPlan = new WeeklyPlanEntity();
        weeklyPlan.setStartDate( weeklyPlanDto.getStartDate() );
        weeklyPlan.setEndDate( weeklyPlanDto.getEndDate() );
        weeklyPlan.setUser( getUser() );

        // generate daily plans from dto
        List<DailyPlanEntity> dailyPlans = weeklyPlanDto.getDailyPlans()
                .stream()
                .map( dailyPlanDto -> dailyPlanDtoToEntity( dailyPlanDto, weeklyPlan) )
                .toList();

        weeklyPlan.setDailyPlans( dailyPlans );

        return this.weeklyPlanRepository.save( weeklyPlan );
    }

    private DailyPlanEntity dailyPlanDtoToEntity( DailyPlanDto dailyPlanDto, WeeklyPlanEntity weeklyPlan ) {
        DailyPlanEntity dailyPlan = new DailyPlanEntity();
        dailyPlan.setDate( dailyPlanDto.getDate() );
        dailyPlan.setWeeklyPlan( weeklyPlan );
        dailyPlan.setStatus( StatusEnum.ACTIVE );
        // generate meals for each daily plan
        List<MealEntity> meals = dailyPlanDto.getMeals()
                .stream()
                .map( mealDto -> mealDtoToEntity( mealDto, dailyPlan ) )
                .toList();
        dailyPlan.setMeals( meals );
        return dailyPlan;
    }

    private MealEntity mealDtoToEntity(MealDto mealDto, DailyPlanEntity dailyPlan ) {

        List<String> tags = Optional.ofNullable( mealDto.getTags() ).orElseGet( ArrayList::new );

        MealEntity meal = new MealEntity();
        meal.setName( mealDto.getName() );
        meal.setDescription( mealDto.getDescription() );
        meal.setImage( mealDto.getImage() );
        meal.setRecipe( mealDto.getRecipe() );
        meal.setIngredients( mealDto.getIngredients() );
        meal.setTags( String.join(",", tags ) );
        meal.setDailyPlan( dailyPlan );
        return meal;
    }

    private UserEntity getUser(){
        // TODO: delete when users get implemented
        return userSvc.get()
                .stream()
                .findFirst()
                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @Override
    public WeeklyPlanDto getWeeklyPlan( LocalDate date, UUID userId ) {
        // TODO: move to user svc
        Map<String, Object> userFilter = new HashMap<>();
        userFilter.put( "id", userId );
        System.out.println( "userFilter = " + userFilter);
        UserEntity user = userSvc.get( userFilter )
                .stream()
                .findFirst()
                .orElseThrow( () -> new RuntimeException("User not found with id [" + userId + "].") );
//                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "User not found with id [" + userId + "]." ) );

        // TODO: move validation logic to weekly svc
        return weeklyPlanRepository.findBetweenDates( date, user )
                .stream()
                .map( WeeklyPlanDto::new )
                .peek( weeklyPlanDto -> System.out.println( "weeklyPlanDto = " + weeklyPlanDto ) )
                .findFirst()
                .orElseThrow( () -> new RuntimeException( "Weekly plan not found for date [" + date + "]." ) );
//                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Weekly plan not found for date [" + date + "]." ) );
    }

    // TODO: improve this method
    @Override
    public Mono<ShoppingListRecommendationDto> getShoppingList( UUID weeklyPlanId ) {
        WeeklyPlanEntity weeklyPlan = weeklyPlanRepository.findById( weeklyPlanId )
                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Weekly plan not found with id [" + weeklyPlanId + "]." ) );

        JsonNode shoppingList = weeklyPlan.getShoppingList();
        if( shoppingList != null ) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return Mono.just( objectMapper.treeToValue( shoppingList, ShoppingListRecommendationDto.class ) );
            } catch (Exception e) {
                throw new RuntimeException("Error converting JSON to object", e);
            }
        }

        ShoppingListRecommendationDto shoppingListRecommendation = Optional
                .ofNullable( openAISvc.generateShoppingList( weeklyPlan ).block() )
                .orElseThrow( () -> new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "Error generating shopping list." ) );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode shoppingListRecommendationStr = objectMapper.valueToTree( shoppingListRecommendation );
            weeklyPlan.setShoppingList( shoppingListRecommendationStr );
            weeklyPlanRepository.save( weeklyPlan );
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }

        return Mono.just( shoppingListRecommendation );
    }

    @Override
    public DailyPlanDto getDailyPlan( LocalDate date, UUID userId) {
        // TODO: move to user svc
        Map<String, Object> userFilter = new HashMap<>();
        userFilter.put( "id", userId );
        System.out.println( "userFilter = " + userFilter);
        UserEntity user = userSvc.get( userFilter )
                .stream()
                .findFirst()
                .orElseThrow( () -> new RuntimeException("User not found with id [" + userId + "].") );
//                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND, "User not found with id [" + userId + "]." ) );

        DailyPlanEntity dailyPlan = Optional
                .ofNullable( dailyPlanRepository.findOnDate( date, user ) )
                .orElseThrow( () -> new EntityNotFoundException( "Daily plan not found for date [" + date + "]." ) )
                .stream()
                .findFirst()
                .orElseThrow( () -> new EntityNotFoundException( "Daily plan not found for date [" + date + "]." ) );
        return new DailyPlanDto( dailyPlan );
    }
}
