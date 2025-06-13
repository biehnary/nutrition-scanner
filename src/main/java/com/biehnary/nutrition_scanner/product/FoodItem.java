package com.biehnary.nutrition_scanner.product;

import lombok.Value;

import java.util.Optional;

@Value
public class FoodItem {

    private final String code;
    private final String foodNm;
    private final NutritionInfo nutritionInfo;
}
