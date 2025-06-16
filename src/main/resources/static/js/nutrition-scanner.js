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
// 최근 선택된 제품 저장
let currentProductName = null;
const nutritionDetail = document.getElementById('nutrition-detail');


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
    }
}