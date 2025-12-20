// 구매 - 지정가 버튼 활성화
function switchToLimit() {
    const limitButton = document.getElementById('price-limit'); // 구매 지정가 버튼
    const marketButton = document.getElementById('market-price'); // 구매 시장가 버튼
    const priceInput = document.querySelector('.price-input');
    const totalPrice = document.getElementById('total-Price'); // 구매 총 주문 금액 레이블
    const totalPriceValue = totalPrice.nextElementSibling; // 총 주문 금액 값

    limitButton.classList.add('active');
    marketButton.classList.remove('active');

    // 지정가의 모양새 설정
    priceInput.value = '54,100원'; // 초기 값
    priceInput.disabled = false; // 입력 가능
    priceInput.style.backgroundColor = ''; // 기본 배경색
    priceInput.style.color = ''; // 기본 글자 색

    // 총 주문 금액 레이블 설정
    totalPrice.textContent = '총 주문 금액';

    // 금액 값 복구
    if (totalPriceValue) {
        totalPriceValue.textContent = '0원';
    }

    // 수량 필드 복구
    const quantityInput = document.querySelector('.quantity-input');
    quantityInput.value = '최대 0주 가능';
}

// 구매 - 시장가 버튼 활성화
function switchToMarket() {
    const limitButton = document.getElementById('price-limit'); // 구매 지정가 버튼
    const marketButton = document.getElementById('market-price'); // 구매 시장가 버튼
    const priceInput = document.querySelector('.price-input');
    const totalPrice = document.getElementById('total-Price'); // 구매 총 주문 금액 레이블
    const totalPriceValue = totalPrice.nextElementSibling; // 총 주문 금액 값

    marketButton.classList.add('active');
    limitButton.classList.remove('active');

    // 시장가의 모양새 설정
    priceInput.value = '최대한 빠른 가격';
    priceInput.disabled = false; // 입력 불가
    priceInput.style.backgroundColor = '#f5f5f5'; // 비활성화 배경색
    priceInput.style.color = '#999'; // 비활성화 텍스트 색

    // 레이블 변경
    totalPrice.textContent = '예상 총 주문 금액';

    // 금액 값 업데이트
    if (totalPriceValue) {
        totalPriceValue.textContent = '최대 0원';
    }

    // 수량 필드 텍스트 변경
    const quantityInput = document.querySelector('.quantity-input');
    quantityInput.value = '수량 입력';
}

// 판매 - 지정가 버튼 활성화
function switchToSellLimit() {
    const limitButton = document.getElementById('sell-price-limit'); // 판매 지정가 버튼
    const marketButton = document.getElementById('sell-market-price'); // 판매 시장가 버튼
    const priceInput = document.querySelector('.price-input');
    const totalPrice = document.getElementById('sell-total-Price'); // 판매 총 주문 금액 레이블

    limitButton.classList.add('active');
    marketButton.classList.remove('active');

    // 지정가의 모양새 설정
    priceInput.value = '54,100원'; // 초기 값
    priceInput.disabled = false; // 입력 가능
    priceInput.style.backgroundColor = ''; // 기본 배경색
    priceInput.style.color = ''; // 기본 글자 색

    // 총 주문 금액 레이블 설정
    if (totalPrice) {
        totalPrice.textContent = '총 주문 금액';
    }
}

// 판매 - 시장가 버튼 활성화
function switchToSellMarket() {
    const limitButton = document.getElementById('sell-price-limit'); // 판매 지정가 버튼
    const marketButton = document.getElementById('sell-market-price'); // 판매 시장가 버튼
    const priceInput = document.querySelector('.price-input');
    const totalPrice = document.getElementById('sell-total-Price'); // 판매 총 주문 금액 레이블

    marketButton.classList.add('active');
    limitButton.classList.remove('active');

    // 시장가의 모양새 설정
    priceInput.value = '최대한 빠른 가격'; // 시장가 상태 텍스트
    priceInput.disabled = true; // 입력 불가
    priceInput.style.backgroundColor = '#f5f5f5'; // 비활성화 상태 배경색
    priceInput.style.color = '#999'; // 비활성화 상태 텍스트 색

    // 레이블 변경
    if (totalPrice) {
        totalPrice.textContent = '예상 총 주문 금액';
    }
}

document.addEventListener("DOMContentLoaded", () => {
    // 판매 - 지정가 버튼 활성화
    function switchToSellLimit() {
        const limitButton = document.getElementById('sell-price-limit'); // 판매 지정가 버튼
        const marketButton = document.getElementById('sell-market-price'); // 판매 시장가 버튼
        const priceInput = document.querySelector('#sell-form .price-input'); // 판매 영역의 입력 필드
        const profitSpan = document.querySelector('#sell-form .profit-value'); // 예상 수익률
        const profitAmountSpan = document.querySelector('#sell-form .profit-amount'); // 예상 손익
        const totalPriceLabel = document.querySelector('#sell-form label[for="sell-total-price"]');
        const totalPriceValue = totalPriceLabel ? totalPriceLabel.nextElementSibling : null;

        // 버튼 활성화 상태 전환
        limitButton.classList.add('active');
        marketButton.classList.remove('active');

        // 지정가 모드로 설정
        priceInput.value = '54,100원'; // 초기 값
        priceInput.disabled = false; // 입력 가능
        priceInput.style.backgroundColor = ''; // 기본 배경색
        priceInput.style.color = ''; // 기본 텍스트 색

        // 수익률 및 손익 복구
        if (profitSpan) profitSpan.textContent = '-2.1%';
        if (profitAmountSpan) profitAmountSpan.textContent = '-1,200원';

        // 총 금액 복구
        if (totalPriceLabel) totalPriceLabel.textContent = '총 금액';
        if (totalPriceValue) totalPriceValue.textContent = '0원';
    }

    // 판매 - 시장가 버튼 활성화
    function switchToSellMarket() {
        const limitButton = document.getElementById('sell-price-limit'); // 판매 지정가 버튼
        const marketButton = document.getElementById('sell-market-price'); // 판매 시장가 버튼
        const priceInput = document.querySelector('#sell-form .price-input'); // 판매 영역의 입력 필드
        const profitSpan = document.querySelector('#sell-form .profit-value'); // 예상 수익률
        const profitAmountSpan = document.querySelector('#sell-form .profit-amount'); // 예상 손익
        const totalPriceLabel = document.querySelector('#sell-form label[for="sell-total-price"]');
        const totalPriceValue = totalPriceLabel ? totalPriceLabel.nextElementSibling : null;

        // 버튼 활성화 상태 전환
        marketButton.classList.add('active');
        limitButton.classList.remove('active');

        // 시장가 모드로 설정
        priceInput.value = '최대한 빠른 가격';
        priceInput.disabled = true;
        priceInput.style.backgroundColor = '#f5f5f5'; // 비활성화 상태 배경색
        priceInput.style.color = '#999'; // 비활성화 상태 텍스트 색

        // 수익률 및 손익 초기화
        if (profitSpan) profitSpan.textContent = '-';
        if (profitAmountSpan) profitAmountSpan.textContent = '-';

        // 예상 총 금액 표시
        if (totalPriceLabel) totalPriceLabel.textContent = '예상 총 금액';
        if (totalPriceValue) totalPriceValue.textContent = '최소 0원';
    }

    // 이벤트 핸들러 연결
    const sellLimitButton = document.getElementById('sell-price-limit');
    const sellMarketButton = document.getElementById('sell-market-price');

    if (sellLimitButton) sellLimitButton.addEventListener('click', switchToSellLimit);
    if (sellMarketButton) sellMarketButton.addEventListener('click', switchToSellMarket);
});




// 구매 버튼 표시
function showBuy() {
    document.getElementById('buy-tab').classList.add('active');
    document.getElementById('sell-tab').classList.remove('active');

    document.getElementById('buy-form').style.display = 'block';
    document.getElementById('sell-form').style.display = 'none';
}

// 판매 버튼 표시
function showSell() {
    document.getElementById('sell-tab').classList.add('active');
    document.getElementById('buy-tab').classList.remove('active');

    document.getElementById('sell-form').style.display = 'block';
    document.getElementById('buy-form').style.display = 'none';
}
