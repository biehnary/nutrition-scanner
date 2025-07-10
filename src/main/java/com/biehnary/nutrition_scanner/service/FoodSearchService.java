package com.biehnary.nutrition_scanner.service;


import com.biehnary.nutrition_scanner.product.FoodItem;
import com.biehnary.nutrition_scanner.product.NutritionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodSearchService {

    @Value("${api.food.serviceKey}")
    private String serviceKey;

    private final WebClient webClient;

    //아이템 서치
    public List<FoodItem> searchItem(String foodNm) {
        System.out.println("검색요청: " + foodNm);

        try {
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String encodedFood = URLEncoder.encode(foodNm, StandardCharsets.UTF_8);

            //URI 생성
            String urlString = "https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo02/getFoodNtrCpntDbInq02?" +
                    "serviceKey=" + encodedKey +
                    "&FOOD_NM_KR=" + encodedFood;


            URI uri = URI.create(urlString);
            System.out.println("생성된 URI: " + uri.toString());

            String response = webClient
                    .get()
                    .uri(uri)  // UriBuilder 대신 URI 직접 사용
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("API 응답 : " + response);
            return parseResponse(response);

        } catch (Exception e) {
            System.out.println("API 호출 실패" + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 파싱 널체크 메서드
    private Double safeParseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    // XML 응답 파싱
    private List<FoodItem> parseResponse(String xmlResponse) {

        List<FoodItem> foodItemList = new ArrayList<>();

        // 스트링 널 체크 or 연산자 작동방식 이용
        if (xmlResponse == null || xmlResponse.isEmpty()) {
            return new ArrayList<>();
        }

        // 도큐먼트 빌더 파싱
        try {
            //DocumentBuilder 생성 - 도큐빌더의 내부 설정값이 복잡해서 팩토리를 따로 두고 자동초기화 하는 방식을 취함.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            //XML 파싱
            Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

            //doc 에서 트리구조 리스트 만들어 item 찾기. xml 구조부터 파악해야함.
            NodeList items = doc.getElementsByTagName("item");
            if (items.getLength() == 0) {
                return new ArrayList<>();
            }

            // 아이템 선택
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                // 데이터 추출
                String foodName = item.getElementsByTagName("FOOD_NM_KR").item(0).getTextContent();
                // nutrition 도메인 정보
                String servingSize = item.getElementsByTagName("SERVING_SIZE").item(0).getTextContent();
                String calories = item.getElementsByTagName("AMT_NUM1").item(0).getTextContent();
                String protein = item.getElementsByTagName("AMT_NUM3").item(0).getTextContent();
                String fat = item.getElementsByTagName("AMT_NUM4").item(0).getTextContent();
                String carbs = item.getElementsByTagName("AMT_NUM6").item(0).getTextContent();
                String sugar = item.getElementsByTagName("AMT_NUM7").item(0).getTextContent();
                String sodium = item.getElementsByTagName("AMT_NUM13").item(0).getTextContent();
                String cholesterol = item.getElementsByTagName("AMT_NUM23").item(0).getTextContent();
                String transFat = item.getElementsByTagName("AMT_NUM25").item(0).getTextContent();

                // 더블로 변환 (널처리 해야함. 위 String에서 빈문자 인가가능성 높음)

                Double caloriesDouble = safeParseDouble(calories);
                Double proteinDouble = safeParseDouble(protein);
                Double fatDouble = safeParseDouble(fat);
                Double carbsDouble = safeParseDouble(carbs);
                Double sugarDouble = safeParseDouble(sugar);
                Double sodiumDouble = safeParseDouble(sodium);
                Double cholesterolDouble = safeParseDouble(cholesterol);
                Double transFatDouble = safeParseDouble(transFat);

                NutritionInfo nutritionInfo = new NutritionInfo(
                        servingSize,
                        caloriesDouble,
                        proteinDouble,
                        fatDouble,
                        carbsDouble,
                        sugarDouble,
                        sodiumDouble,
                        cholesterolDouble,
                        transFatDouble
                );

                FoodItem foodItem = new FoodItem("", foodName, nutritionInfo);
                System.out.println("foodItem = " + foodItem);
                foodItemList.add(foodItem);
            }
            return foodItemList;


        } catch (Exception e) {
            System.out.println("XML 파싱 실패 : " + e.getMessage());

            return new ArrayList<>();
        }

    }
}
