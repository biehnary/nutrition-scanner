package com.biehnary.nutrition_scanner.web;

import com.biehnary.nutrition_scanner.product.FoodItem;
import com.biehnary.nutrition_scanner.service.FoodSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NutritionScannerController {

    private final FoodSearchService foodSearchService;

    @GetMapping("/search/name")
    public String getByItemName(@RequestParam("foodNm") String foodNm, Model model) {
        System.out.println("제품명 받음: " + foodNm);

        List<FoodItem> foodItems = foodSearchService.searchItem(foodNm);

        model.addAttribute("foodNm", foodNm);
        model.addAttribute("foodItems", foodItems);
        return "search-result";

    }

}
