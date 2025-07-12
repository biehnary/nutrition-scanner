package com.biehnary.nutrition_scanner.web;

import com.biehnary.nutrition_scanner.product.FoodItem;
import com.biehnary.nutrition_scanner.service.AnalyzeService;
import com.biehnary.nutrition_scanner.service.FoodSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NutritionScannerController {

    private final AnalyzeService analyzeService;
    private final FoodSearchService foodSearchService;


    // 제미나이 ai result
    @PostMapping("/api/analyze")
    @ResponseBody
    public String analyzeNutrition(@RequestBody FoodItem foodItem) {
        return analyzeService.analyzeNutrition(foodItem);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 상품 검색 시 결과창
    @GetMapping("/search/name")
    public String getByItemName(@RequestParam("foodNm") String foodNm, Model model) {
        System.out.println("제품명 받음: " + foodNm);

        List<FoodItem> foodItems = foodSearchService.searchItem(foodNm);

        model.addAttribute("foodNm", foodNm);
        model.addAttribute("foodItems", foodItems);
        return "search-result";

    }

}
