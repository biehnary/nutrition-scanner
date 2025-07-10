package com.biehnary.nutrition_scanner.web;

import com.biehnary.nutrition_scanner.product.FoodItem;
import com.biehnary.nutrition_scanner.service.AnalyzeService;
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

    private final AnalyzeService analyzeService;

    @GetMapping("/")
    public String index() {
        return "index";
    }


    // 결과창 컨트롤러
    private final FoodSearchService foodSearchService;

    // 상품 검색 시 결과창
    @GetMapping("/search/name")
    public String getByItemName(@RequestParam("foodNm") String foodNm, Model model) {
        System.out.println("제품명 받음: " + foodNm);

        List<FoodItem> foodItems = foodSearchService.searchItem(foodNm);

        // ai 콘솔테스트
        if (!foodItems.isEmpty()) {
            FoodItem firstItem = foodItems.get(0);
            String aiResponse = analyzeService.analyzeNutrition(firstItem);
            System.out.println("==AI 응답테스트 ==");
            System.out.println(aiResponse);
            System.out.println("===========");
        }

        model.addAttribute("foodNm", foodNm);
        model.addAttribute("foodItems", foodItems);
        return "search-result";

    }

}
