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

function showDetail(index) {
    // 서버에 주입한 전역변수 가져오기
    const foodItems = window.NutritionData.foodItems;

    // 유효성 검사
    if (!foodItems || index < 0 || index >= foodItems.length) {
        console.error('잘못된 인덱스', index);
        return;
    }

    // 선택된 아이템
    const selectedItem = foodItems[index];

    console.log('선택된 아이템', selectedItem);

    // 아이템 매핑
    for (let key in FIELD_MAPPING) {
        const value = selectedItem.nutritionInfo[key];
        const htmlId = FIELD_MAPPING[key];
        document.getElementById(htmlId).textContent = value;
    }

    document.getElementById('nutrition-detail').style.display = 'block';
}