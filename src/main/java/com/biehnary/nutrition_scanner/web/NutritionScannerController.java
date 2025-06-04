package com.biehnary.nutrition_scanner.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NutritionScannerController {

    @GetMapping("/search/name")
    public String getByItemName(@RequestParam("foodNm") String foodNm, Model model) {
        System.out.println("제품명 받음: " + foodNm);
        model.addAttribute("foodNm", foodNm);
        return "search-result";

    }

}
