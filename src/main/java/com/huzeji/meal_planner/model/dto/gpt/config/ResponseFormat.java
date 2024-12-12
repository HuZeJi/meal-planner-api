package com.huzeji.meal_planner.model.dto.gpt.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFormat {
    private String type;
    @JsonProperty( "json_schema" )
    private JsonSchema jsonSchema;


    public static ResponseFormat getConfigurations() {
        // setting the default for the total
        Schema currencySchema = new Schema();
        currencySchema.setDescription( "The currency of the estimated total." );
        currencySchema.setType( "string" );
        Schema quantitySchema = new Schema();
        quantitySchema.setDescription( "The estimated total quantity cost." );
        quantitySchema.setType( "number" );

        Schema approximateTotalSchema = new Schema();
        approximateTotalSchema.setDescription( "The estimated total cost of the shopping list." );
        approximateTotalSchema.setProperties( new HashMap<>() );
        approximateTotalSchema.setAdditionalProperties( Boolean.FALSE );
        approximateTotalSchema.addProperty( "quantity", quantitySchema );
        approximateTotalSchema.addProperty( "currency", currencySchema );
        approximateTotalSchema.setRequired( Arrays.asList( "currency", "quantity" ) );

        // setting the items of the shopping list

        Schema subTotalCurrencySchema = new Schema();
        subTotalCurrencySchema.setDescription( "The currency of the estimated total." );
        subTotalCurrencySchema.setType( "string" );
        Schema subTotalQuantitySchema = new Schema();
        subTotalQuantitySchema.setDescription( "The estimated total quantity cost." );
        subTotalQuantitySchema.setType( "number" );

        Schema approximateSubTotalSchema = new Schema();
        approximateSubTotalSchema.setDescription( "The estimated subtotal for this item." );
        approximateSubTotalSchema.setAdditionalProperties( Boolean.FALSE );
        approximateSubTotalSchema.setProperties( new HashMap<>() );
        approximateSubTotalSchema.addProperty( "currency", subTotalCurrencySchema );
        approximateSubTotalSchema.addProperty( "quantity", subTotalQuantitySchema );
        approximateSubTotalSchema.setRequired( Arrays.asList( "quantity", "currency" ) );

        Schema ingredientTypeSchema = new Schema();
        ingredientTypeSchema.setDescription( "The type of the ingredient." );
        ingredientTypeSchema.setType( "string" );

        Schema itemNameSchema = new Schema();
        itemNameSchema.setDescription( "The name of the ingredient." );
        itemNameSchema.setType( "string" );

        Schema itemQuantitySchema = new Schema();
        itemQuantitySchema.setDescription( "The quantity of the ingredient." );
        itemQuantitySchema.setType( "number" );

        Schema itemQuantityUnitSchema = new Schema();
        itemQuantityUnitSchema.setDescription( "The unit of the quantity of the ingredient." );
        itemQuantityUnitSchema.setType( "string" );

        // setting the required at items

        Schema requiredAtItemDaySchema = new Schema();
        requiredAtItemDaySchema.setDescription( "The day when the item is needed." );
        requiredAtItemDaySchema.setType( "string" );

        Schema requiredAtItemMealTimeSchema = new Schema();
        requiredAtItemMealTimeSchema.setDescription( "The meal time when the item is required." );
        requiredAtItemMealTimeSchema.setType( "string" );

        Schema requiredAtItems = new Schema();
        requiredAtItems.setType( "object" );
        requiredAtItems.setProperties( new HashMap<>() );
        requiredAtItems.addProperty( "day", requiredAtItemDaySchema );
        requiredAtItems.addProperty( "mealTime", requiredAtItemMealTimeSchema );
        requiredAtItems.setRequired( Arrays.asList( "day", "mealTime" ) );
        requiredAtItems.setAdditionalProperties( Boolean.FALSE );

        Schema requiredAtItemSchema = new Schema();
        requiredAtItemSchema.setType( "array" );
        requiredAtItemSchema.setItems( requiredAtItems );
        requiredAtItemSchema.setDescription( "When the item is required." );

        Schema itemsSchema = new Schema();
        itemsSchema.setAdditionalProperties( Boolean.FALSE );
        itemsSchema.setProperties( new HashMap<>() );
        itemsSchema.addProperty( "name", itemNameSchema );
        itemsSchema.addProperty( "quantity", itemQuantitySchema );
        itemsSchema.addProperty( "quantityUnit", itemQuantityUnitSchema );
        itemsSchema.addProperty( "approximateSubTotal", approximateSubTotalSchema );
        itemsSchema.addProperty( "requiredAt", requiredAtItemSchema );
        itemsSchema.addProperty( "ingredientType", ingredientTypeSchema );
        itemsSchema.setRequired( Arrays.asList( "name", "quantity", "quantityUnit", "approximateSubTotal", "requiredAt", "ingredientType" ) );


        Schema shoppingListSchema = new Schema();
        shoppingListSchema.setDescription( "A list of items to purchase." );
        shoppingListSchema.setItems( itemsSchema );
        shoppingListSchema.setType( "array" );

        // setting the shopping list

        Schema schema = new Schema();
        schema.setProperties( new HashMap<>() );
        schema.setAdditionalProperties( Boolean.FALSE );
        schema.addProperty( "approximateTotal", approximateTotalSchema );
        schema.addProperty( "shoppingList", shoppingListSchema );
        schema.setRequired( Arrays.asList( "shoppingList", "approximateTotal" ) );

        JsonSchema jsonSchema = new JsonSchema();
        jsonSchema.setSchema( schema );
        jsonSchema.setName( "shoppingListSchema" );

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setType( "json_schema" );
        responseFormat.setJsonSchema( jsonSchema );

        return responseFormat;
    }
}
