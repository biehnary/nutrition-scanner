package com.biehnary.nutrition_scanner.service;

import com.biehnary.nutrition_scanner.product.FoodItem;
import com.biehnary.nutrition_scanner.product.NutritionInfo;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeService {

    private Client client;

    @Value("${api.gemini.key}")
    private String apikey;

    public String analyzeNutrition(FoodItem foodItem){
        String productName = foodItem.getFoodNm();
        NutritionInfo nutrition = foodItem.getNutritionInfo();

        // 데이터 가져오는 과정
        String servingSize = nutrition.getNutConSrtrQua();
        Double calories = nutrition.getEnerc();
        Double protein = nutrition.getProt();
        Double fat = nutrition.getFatce();
        Double carbs = nutrition.getChocdf();
        Double sugar = nutrition.getSugar();
        Double sodium = nutrition.getNat();
        Double chole = nutrition.getChole(); // 콜레스태롤 수치

        // 프롬프트 작성
        String prompt = "제품명: " + productName + "\n" +
                "기준 제공량: " + (servingSize != null && !servingSize.trim().isEmpty()
                                ? servingSize : "정보없음") + "\n" +
                formatNutrient("칼로리", calories, "kcal") + "\n" +
                formatNutrient("단백질", protein, "g") + "\n" +
                formatNutrient("지방", fat, "g") + "\n" +
                formatNutrient("탄수화물", carbs, "g") + "\n" +
                formatNutrient("당", sugar, "g") + "\n" +
                formatNutrient("나트륨", sodium, "mg") + "\n" +
                formatNutrient("콜레스테롤", chole, "mg") + "\n" +
                "제품의 영양성분을 분석해 영양 상의 건전도를 평가해주세요." +
                "해당 식품과 어울리는 제품, 보완할 수 있는 제품, 조리법을 추천해주세요." +
                "답변은 담백하고 공손하게 해주세요.";


        // api 호출
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        // 응답 리턴
        return response.text();

    }

    private String formatNutrient(String name, Double value, String unit) {
        return name + ": " + (value != null ? value + unit : "정보없음");
    }

    @PostConstruct // 스프링은 컴파일 이후 실행되기 때문에 스프링 문법에서 api를 받아오려면 모든 설정 이후 실행되어야한다. 포스트 컨스트럭트, 스프링 사이클을 이해해야함.
    public void initClient() {
        this.client = Client.builder().apiKey(apikey).build();
    }
}
