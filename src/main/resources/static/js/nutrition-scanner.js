const FIELD_MAPPING = {
    'nutConSrtrQua': 'serving-size',
    'enerc': 'calories',
    'prot': 'protein',
    'fatce': 'fat',
    'chocdf': 'carbs',
    'sugar': 'sugar',
    'nat': 'sodium',
    'chole': 'cholesterol',
    'fatrn': 'trans-fat'
};

// 토글 상태 구현을 위한 변수
// 디스플레이 상태
let isNutritionVisible = false;
const aiAnalysis = document.getElementById('ai-analysis');
// 최근 선택된 제품 저장
let currentProductName = null;
const nutritionDetail = document.getElementById('nutrition-detail');

// AI 결과 캐시변수
const aiCache = {};

function showDetail(index) {
    // 서버에 주입한 전역변수 가져오기
    const foodItems = window.NutritionData.foodItems;
    // 선택된 아이템
    const selectedItem = foodItems[index];
    console.log('선택된 아이템', selectedItem);

    // 유효성 검사
    if (!foodItems || index < 0 || index >= foodItems.length) {
        console.error('잘못된 인덱스', index);
        return;
    }

    // 토글 온 상태 시 닫기 로직
    if (isNutritionVisible && currentProductName === selectedItem.foodNm) {
        nutritionDetail.style.display = 'none';
        aiAnalysis.style.display = 'none';
        isNutritionVisible = false;
        currentProductName = null;
    } else { // 닫힌 상태 혹은 전환
        // 아이템 매핑
        for (let key in FIELD_MAPPING) {
            const value = selectedItem.nutritionInfo[key];
            const htmlId = FIELD_MAPPING[key];
            document.getElementById(htmlId).textContent = value;
        }
        nutritionDetail.style.display = 'block';
        currentProductName = selectedItem.foodNm;
        isNutritionVisible = true;
        callAiAnalysis(selectedItem);
    }

}

function callAiAnalysis(item) {
    // 상품명으로 ai 분석결과 캐싱. 분석이 오래걸리기 때문에 매번 새로호출할 수 없음.
    const cacheKey = item.foodNm;

    if (aiCache[cacheKey]){
        console.log("캐시에 존재. 캐시에서 가져옴", cacheKey);

        document.getElementById('ai-analysis').style.display = 'block';
        document.getElementById('ai-loading').style.display = 'none';
        document.getElementById('ai-result').innerHTML = aiCache[cacheKey];

        return; // API 호출 안함.

    }

    // 캐시에 없는경우
    console.log("AI API호출", cacheKey);
    document.getElementById('ai-analysis').style.display = 'block';
    document.getElementById('ai-loading').style.display = 'block';
    document.getElementById('ai-result').innerHTML = '';

    // AI API 호출
    fetch('/api/analyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(item)
    })
        .then(response => response.text())
        .then(aiResult => {
            aiCache[cacheKey] = marked.parse(aiResult);
            // 결과 캐싱
            // 로딩 숨김 + 결과 표시
            document.getElementById('ai-loading').style.display = 'none';
            document.getElementById('ai-result').innerHTML = marked.parse(aiResult);
        })
        .catch(error => {
            console.error('AI 분석 오류:', error);
            document.getElementById('ai-loading').style.display = 'none';
            document.getElementById('ai-result').innerHTML = '분석 중 오류가 발생했습니다.';
        });
}