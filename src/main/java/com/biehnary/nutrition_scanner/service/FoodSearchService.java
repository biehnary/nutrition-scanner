package com.biehnary.nutrition_scanner.service;


import com.biehnary.nutrition_scanner.product.FoodItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodSearchService {

    @Value("${api.food.serviceKey}")
    private String serviceKey;

    private final WebClient webClient;
    public Optional<FoodItem> searchItem(String foodNm) {
        System.out.println("검색요청: " + foodNm);

        try {
            String response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1471000/FoodNtrCpntDbInfo02/getFoodNtrCpntDbInq02")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("FOOD_NM_KR", foodNm)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 3)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("API 응답 : " + response);
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("API 호출 실패" + e.getMessage());
            return Optional.empty();
        }
    }
}
