// 지정가 버튼 영역 비활성화
function disablePriceLimitButton() {
    const priceLimitButton = document.getElementById('price-limit');
    const marketButton = document.getElementById('market-price'); // 정확한 ID로 시장가 버튼 요소 가져오기

    // 시장가 버튼이 활성화 상태라면 함수 실행 중단
    if (marketButton && marketButton.classList.contains('active')) {
        console.log("시장가 버튼 활성화 상태 - disablePriceLimitButton 실행 중단");
        return; // 함수 종료
    }

    if (priceLimitButton) {
        priceLimitButton.classList.add('price-limit-disabled');
        priceLimitButton.disabled = true;

        // 물음표 아이콘 스타일 변경
        const helpIcon = priceLimitButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.add('help-icon-disabled');
        }
    }
}


// 지정가 버튼 영역 활성화
function enablePriceLimitButton() {
    const priceLimitButton = document.getElementById('price-limit');
    if (priceLimitButton) {
        priceLimitButton.classList.remove('price-limit-disabled');
        priceLimitButton.disabled = false;

        // 즉각적으로 복원되도록 트랜지션 제거 및 리플로우 강제
        priceLimitButton.style.transition = 'none'; // 트랜지션 제거
        priceLimitButton.offsetHeight; // 리플로우 강제
        priceLimitButton.style.transition = ''; // 트랜지션 복원

        // 물음표 아이콘 스타일 복원
        const helpIcon = priceLimitButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.remove('help-icon-disabled');

            // 물음표 아이콘 복원 시 트랜지션 제거 및 리플로우 강제
            helpIcon.style.transition = 'none'; // 트랜지션 제거
            helpIcon.offsetHeight; // 리플로우 강제
            helpIcon.style.transition = ''; // 트랜지션 복원
        }
    }
}
// 시장가 버튼 영역 비활성화
function disableMarketButton() {
    const marketButton = document.getElementById('market-price');
    const priceLimitButton = document.getElementById('price-limit'); // 지정가 버튼 요소 가져오기

	// 지정가 버튼이 활성화 상태라면 함수 실행 중단
	    if (priceLimitButton && priceLimitButton.classList.contains('active')) {
	        console.log("지정가 버튼 활성화 상태 - disableMarketButton 실행 중단");
	        return; // 함수 종료
	    }

    if (marketButton) {
        marketButton.classList.add('market-type-disabled');
        marketButton.disabled = true;

        // 물음표 아이콘 스타일 변경
        const helpIcon = marketButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.add('help-icon-disabled');
        }
    }
}

// 시장가 버튼 영역 활성화
function enableMarketButton() {
    const marketButton = document.getElementById('market-price');
    if (marketButton) {
        marketButton.classList.remove('market-type-disabled');
        marketButton.disabled = false;

        // 즉각적으로 복원되도록 트랜지션 제거 및 리플로우 강제
        marketButton.style.transition = 'none'; // 트랜지션 제거
        marketButton.offsetHeight; // 리플로우 강제
        marketButton.style.transition = ''; // 트랜지션 복원

        // 물음표 아이콘 스타일 복원
        const helpIcon = marketButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.remove('help-icon-disabled');

            // 물음표 아이콘 복원 시 트랜지션 제거 및 리플로우 강제
            helpIcon.style.transition = 'none'; // 트랜지션 제거
            helpIcon.offsetHeight; // 리플로우 강제
            helpIcon.style.transition = ''; // 트랜지션 복원
        }
    }
}


// 주문 유형 영역 비활성화
function disableOrderTypeArea() {
    const orderTypeArea = document.querySelector('.orderType-hoverable-area');
    if (orderTypeArea) {
        const selectBox = orderTypeArea.querySelector('select');
        if (selectBox) {
            selectBox.style.borderColor = '#6E6E6E'; // 테두리 색 변경
        }
        orderTypeArea.classList.add('disabled'); // 비활성화 클래스 추가
    }
}

// 주문 유형 영역 활성화
function enableOrderTypeArea() {
    const orderTypeArea = document.querySelector('.orderType-hoverable-area');
    if (orderTypeArea) {
        const selectBox = orderTypeArea.querySelector('select');
        if (selectBox) {
            selectBox.style.borderColor = '#ddd'; // 기본 테두리 색으로 복원
        }
        orderTypeArea.classList.remove('disabled'); // 비활성화 클래스 제거
    }
}


// 판매 - 지정가 버튼 영역 비활성화
function disableSellPriceLimitButton() {
    const sellPriceLimitButton = document.getElementById('sell-price-limit');
    const sellMarketButton = document.getElementById('sell-market-price'); // 정확한 ID로 시장가 버튼 요소 가져오기

    // 시장가 버튼이 활성화 상태라면 함수 실행 중단
    if (sellMarketButton && sellMarketButton.classList.contains('active')) {
        return; // 함수 종료
    }

    if (sellPriceLimitButton) {
        sellPriceLimitButton.classList.add('sell-price-limit-disabled');
        sellPriceLimitButton.disabled = true;

        // 물음표 아이콘 스타일 변경
        const helpIcon = sellPriceLimitButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.add('help-icon-disabled');
        }
    }
}

// 판매 - 지정가 버튼 영역 활성화
function enableSellPriceLimitButton() {
    const sellPriceLimitButton = document.getElementById('sell-price-limit');
    if (sellPriceLimitButton) {
        sellPriceLimitButton.classList.remove('sell-price-limit-disabled');
        sellPriceLimitButton.disabled = false;

        // 즉각적으로 복원되도록 트랜지션 제거 및 리플로우 강제
        sellPriceLimitButton.style.transition = 'none'; // 트랜지션 제거
        sellPriceLimitButton.offsetHeight; // 리플로우 강제
        sellPriceLimitButton.style.transition = ''; // 트랜지션 복원

        // 물음표 아이콘 스타일 복원
        const helpIcon = sellPriceLimitButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.remove('help-icon-disabled');

            // 물음표 아이콘 복원 시 트랜지션 제거 및 리플로우 강제
            helpIcon.style.transition = 'none'; // 트랜지션 제거
            helpIcon.offsetHeight; // 리플로우 강제
            helpIcon.style.transition = ''; // 트랜지션 복원
        }
    }
}

// 판매 - 시장가 버튼 영역 비활성화
function disableSellMarketButton() {
    const sellMarketButton = document.getElementById('sell-market-price');
    const sellPriceLimitButton = document.getElementById('sell-price-limit'); // 지정가 버튼 요소 가져오기

    // 지정가 버튼이 활성화 상태라면 함수 실행 중단
    if (sellPriceLimitButton && sellPriceLimitButton.classList.contains('active')) {
        return; // 함수 종료
    }

    if (sellMarketButton) {
        sellMarketButton.classList.add('sell-market-price-disabled');
        sellMarketButton.disabled = true;

        // 물음표 아이콘 스타일 변경
        const helpIcon = sellMarketButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.add('help-icon-disabled');
        }
    }
}

// 판매 - 시장가 버튼 영역 활성화
function enableSellMarketButton() {
    const sellMarketButton = document.getElementById('sell-market-price');
    if (sellMarketButton) {
        sellMarketButton.classList.remove('sell-market-price-disabled');
        sellMarketButton.disabled = false;

        // 즉각적으로 복원되도록 트랜지션 제거 및 리플로우 강제
        sellMarketButton.style.transition = 'none'; // 트랜지션 제거
        sellMarketButton.offsetHeight; // 리플로우 강제
        sellMarketButton.style.transition = ''; // 트랜지션 복원

        // 물음표 아이콘 스타일 복원
        const helpIcon = sellMarketButton.querySelector('.help-icon');
        if (helpIcon) {
            helpIcon.classList.remove('help-icon-disabled');

            // 물음표 아이콘 복원 시 트랜지션 제거 및 리플로우 강제
            helpIcon.style.transition = 'none'; // 트랜지션 제거
            helpIcon.offsetHeight; // 리플로우 강제
            helpIcon.style.transition = ''; // 트랜지션 복원
        }
    }
}
// 판매 - 주문 유형 영역 비활성화
function disableSellOrderTypeArea() {
    const sellOrderTypeArea = document.querySelector('.sell-orderType-hoverable-area');
    if (sellOrderTypeArea) {
        const selectBox = sellOrderTypeArea.querySelector('select');
        if (selectBox) {
            selectBox.style.borderColor = '#6E6E6E'; // 테두리 색 변경
        }
        sellOrderTypeArea.classList.add('disabled'); // 비활성화 클래스 추가
    }
}

// 판매 - 주문 유형 영역 활성화
function enableSellOrderTypeArea() {
    const sellOrderTypeArea = document.querySelector('.sell-orderType-hoverable-area');
    if (sellOrderTypeArea) {
        const selectBox = sellOrderTypeArea.querySelector('select');
        if (selectBox) {
            selectBox.style.borderColor = '#ddd'; // 기본 테두리 색으로 복원
        }
        sellOrderTypeArea.classList.remove('disabled'); // 비활성화 클래스 제거
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 주문 유형 설명창 열기
function showOrderTypeExplanation(event) {
    const explanation = document.getElementById('orderTypeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 주문 유형 hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	// 지정가 버튼 영역 비활성화
    disablePriceLimitButton();
	// 시장가 버튼 영역 비활성화
	disableMarketButton();

	 
    // 페이지 클릭 차단, 닫기 버튼과 셀렉트 박스는 제외
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click');

    const selectBox = hoverableArea.querySelector('select');
    if (selectBox) {
        selectBox.classList.add('allow-click');
        selectBox.addEventListener('click', preventExplanationMovement);
        selectBox.addEventListener('change', preventExplanationMovement);
    }
}

// 주문 유형 설명창 닫기
function closeOrderTypeExplanation() {
    const explanation = document.getElementById('orderTypeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.orderType-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 주문 유형 hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }
	
	// 지정가 버튼 복구
    enablePriceLimitButton();
	// 시장가 버튼 복구
	enableMarketButton();
	// 판매 시장가 버튼 복구
	enableSellPriceLimitButton();
	
    // 페이지 클릭 허용
    document.body.classList.remove('no-click');

    const selectBox = hoverableArea.querySelector('select');
    if (selectBox) {
        selectBox.classList.remove('allow-click');
        selectBox.removeEventListener('click', preventExplanationMovement);
        selectBox.removeEventListener('change', preventExplanationMovement);
    }
}

function preventExplanationMovement(event) {
    event.stopPropagation();
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 구매부분 비율 퍼센트 설명창 열기
function showPercentageExplanation(event) {
    const explanation = document.getElementById('percentageExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 220}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 구매 비율 hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	// 주문 유형 영역 비활성화 및 스타일 변경
	disableOrderTypeArea();
	// 지정가 버튼 영역 비활성화
	disablePriceLimitButton();
	// 시장가 버튼 영역 비활성화
	disableMarketButton();	
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();


    // 페이지 클릭 차단, 닫기 버튼만 허용
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click');

    // 주문 유형 영역의 클릭 및 hover 차단
    const orderTypeArea = document.querySelector('.orderType-hoverable-area');
    if (orderTypeArea) {
        orderTypeArea.classList.add('disabled'); // 클릭 비활성화
    }
}
// 구매부분 비율 퍼센트 설명창 닫기
function closePercentageExplanation() {
    const explanation = document.getElementById('percentageExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.percentage-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 구매 비율 hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 주문 유형 영역의 클릭 및 hover 복원
    const orderTypeArea = document.querySelector('.orderType-hoverable-area');
    if (orderTypeArea) {
        orderTypeArea.classList.remove('disabled'); // 클릭 활성화
    }
	
	// 주문 유형 영역 활성화 및 스타일 복원
	enableOrderTypeArea();
	
	// 지정가 버튼 복구
	enablePriceLimitButton();
	// 시장가 버튼 복구
	enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();


    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 미수거래 설명창 열기
function showMisuExplanation(event) {
    const explanation = document.getElementById('misuExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 250}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	// 주문 유형 영역 비활성화 및 스타일 변경
	disableOrderTypeArea();
	// 지정가 버튼 영역 비활성화
	disablePriceLimitButton();
	// 시장가 버튼 영역 비활성화
	disableMarketButton();

    // 페이지 클릭 차단, 닫기 버튼만 허용
    document.body.classList.add('no-click');
	
	// 닫기 버튼과 체크박스는 클릭 가능하도록 처리
	    explanation.querySelector('button').classList.add('allow-click');
	    const checkbox = hoverableArea.querySelector('input[type="checkbox"]');
	    if (checkbox) {
	        checkbox.classList.add('allow-click');

	        // 체크박스 클릭 시 이벤트 전파 방지
	        checkbox.addEventListener('click', function (e) {
	            e.stopPropagation();
	        });
	    }
}
// 미수거래 설명창 닫기
function closeMisuExplanation() {
    const explanation = document.getElementById('misuExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.misu-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화 및 원래 상태 복원
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }
	
	// 주문 유형 영역 활성화 및 스타일 복원
	enableOrderTypeArea();
	// 지정가 버튼 복구
	enablePriceLimitButton();
	// 시장가 버튼 복구
	enableMarketButton();
	
    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
	
	// 체크박스 클릭 가능 클래스 제거
	    const checkbox = hoverableArea.querySelector('input[type="checkbox"]');
	    if (checkbox) {
	        checkbox.classList.remove('allow-click');
	    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 구매 - 지정가 설명창 열기
function showBuyPriceExplanation(event) {
  const explanation = document.getElementById('priceTypeExplanation');
  const overlay = document.getElementById('overlay');
  const priceLimitButton = document.getElementById('price-limit');
  const rect = event.currentTarget.getBoundingClientRect();

  // 설명창 위치 설정
  explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`;
  explanation.style.left = `${window.scrollX + rect.left - 87}px`;

  // 설명창과 오버레이 표시
  explanation.style.display = 'block';
  overlay.style.display = 'block';
  overlay.classList.add('active'); // 오버레이 활성화

  // 주문 유형 영역 비활성화 및 스타일 변경
  disableOrderTypeArea();

  // 페이지 클릭 차단, 닫기 버튼 제외
  document.body.classList.add('no-click');
  explanation.querySelector('button').classList.add('allow-click'); // X 버튼 클릭 허용
}
// 구매 - 지정가 설명창 닫기
function closeBuyPriceExplanation() {
    const explanation = document.getElementById('priceTypeExplanation');
    const overlay = document.getElementById('overlay');
    const priceLimitButton = document.getElementById('price-limit');
    const marketButton = document.getElementById('price-market');

    // 설명창 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';
    overlay.classList.remove('active'); // 오버레이 비활성화

    // 지정가 버튼을 활성화 상태로 복구
    if (priceLimitButton) {
        priceLimitButton.classList.add('active'); // 지정가 버튼 활성화
        priceLimitButton.style.color = ''; // 글씨 색 초기화
        priceLimitButton.style.backgroundColor = ''; // 배경 초기화
        priceLimitButton.style.borderColor = '#ddd'; // 테두리 초기화
    }

    // 시장가 버튼 비활성화
    if (marketButton) {
        marketButton.classList.remove('active'); // 시장가 버튼 비활성화
        marketButton.style.color = ''; // 기본 텍스트 색상
        marketButton.style.backgroundColor = ''; // 배경 초기화
        marketButton.style.borderColor = '#ddd'; // 테두리 초기화
    }
	
	// 주문 유형 영역 활성화 및 스타일 복원
	enableOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');

    // 지정가로 복구
    switchToLimit();
}

///// 구매 가능 금액 //////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 구매 가능 금액 설명창 열기
function showAmountExplanation(event) {
    const explanation = document.getElementById('amountExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 220}px`; // 위치 조정
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

	// 주문 유형 영역 비활성화 및 스타일 변경
	disableOrderTypeArea();
	// 지정가 버튼 영역 비활성화
	disablePriceLimitButton();
	// 시장가 버튼 영역 비활성화
	disableMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click'); // 닫기 버튼 클릭 허용
}

// 구매 가능 금액 설명창 닫기
function closeAmountExplanation() {
    const explanation = document.getElementById('amountExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.amount-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

	// 주문 유형 영역 활성화 및 스타일 복원
	enableOrderTypeArea();
	// 지정가 버튼 복구
	enablePriceLimitButton();
	// 시장가 버튼 복구
	enableMarketButton();
	
    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 총 주문 금액 설명창 열기
function showTotalPriceExplanation(event) {
    const explanation = document.getElementById('totalPriceExplanation'); // 설명창 ID
    const overlay = document.getElementById('overlay'); // 오버레이
    const hoverableArea = event.currentTarget; // 현재 클릭된 영역
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`; // 위치 조정
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

	// 주문 유형 영역 비활성화 및 스타일 변경
	disableOrderTypeArea();
	// 지정가 버튼 영역 비활성화
	disablePriceLimitButton();
	// 시장가 버튼 영역 비활성화
	disableMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click'); // 닫기 버튼 클릭 허용
}

// 총 주문 금액 설명창 닫기
function closeTotalPriceExplanation() {
    const explanation = document.getElementById('totalPriceExplanation'); // 설명창 ID
    const overlay = document.getElementById('overlay'); // 오버레이
    const hoverableArea = document.querySelector('.total-price-hoverable-area'); // hoverable-area

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

	// 주문 유형 영역 활성화 및 스타일 복원
	enableOrderTypeArea();
	// 지정가 버튼 복구
	enablePriceLimitButton();
	// 시장가 버튼 복구
	enableMarketButton();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 수량 설명창 열기
function showQuantityExplanation(event) {
    const explanation = document.getElementById('quantityExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 140}px`; // 위치 조정
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	// 주문 유형 영역 비활성화 및 스타일 변경
	disableOrderTypeArea();
	// 지정가 버튼 영역 비활성화
	disablePriceLimitButton();
	// 시장가 버튼 영역 비활성화
	disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();


    // 페이지 클릭 차단
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click'); // 닫기 버튼 클릭 허용
}

// 수량 설명창 닫기
function closeQuantityExplanation() {
    const explanation = document.getElementById('quantityExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.quantity-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }
	
	// 주문 유형 영역 활성화 및 스타일 복원
	enableOrderTypeArea();
	// 지정가 버튼 복구
	enablePriceLimitButton();
	// 시장가 버튼 복구
	enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 시장가 설명창 열기
function showMarketPriceExplanation(event) {
    const explanation = document.getElementById('marketPriceExplanation');
    const overlay = document.getElementById('overlay');
    const marketButton = document.getElementById('market-price');
    const rect = event.currentTarget.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`;
    explanation.style.left = `${window.scrollX + rect.left - 87}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';
    overlay.classList.add('active'); // 오버레이 활성화

    // 시장가 버튼 활성화
    if (marketButton) {
        marketButton.classList.add('active'); // 시장가 버튼에 active 클래스 추가
    }

    // 주문 유형 영역 비활성화 및 스타일 변경
    disableOrderTypeArea();

    // 페이지 클릭 차단, 닫기 버튼 제외
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click'); // X 버튼 클릭 허용
}
// 시장가 설명창 닫기
function closeMarketPriceExplanation() {
    const explanation = document.getElementById('marketPriceExplanation');
    const overlay = document.getElementById('overlay');
    const priceLimitButton = document.getElementById('price-limit');
    const marketButton = document.getElementById('market-price');

    // 설명창 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';
    overlay.classList.remove('active'); // 오버레이 비활성화

    // 시장가 버튼 비활성화
    if (marketButton) {
        marketButton.classList.remove('active'); // 시장가 버튼에서 active 클래스 제거
    }

    // 주문 유형 영역 활성화 및 스타일 복원
    enableOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');

    // 시장가로 복구
    switchToMarket();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 총 수익 설명창 열기
function showProfitExplanation(event) {
    const explanation = document.getElementById('totalProfitExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 250}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼과 체크박스는 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
    const checkbox = hoverableArea.querySelector('input[type="checkbox"]');
    if (checkbox) {
        checkbox.classList.add('allow-click');

        // 체크박스 클릭 시 이벤트 전파 방지
        checkbox.addEventListener('click', function (e) {
            e.stopPropagation();
        });
    }
}
// 총 수익 설명창 닫기
function closeProfitExplanation() {
    const explanation = document.getElementById('totalProfitExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.profit-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();

	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');

    // 체크박스 클릭 가능 클래스 제거
    const checkbox = hoverableArea.querySelector('input[type="checkbox"]');
    if (checkbox) {
        checkbox.classList.remove('allow-click');
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 총 금액 설명창 열기
function showTotalAmountExplanation(event) {
    const explanation = document.getElementById('totalAmountExplanation'); // 총 금액 설명창 ID
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 200}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 총 금액 설명창 닫기
function closeTotalAmountExplanation() {
    const explanation = document.getElementById('totalAmountExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.total-amount-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();

	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 내 주식-수량 설명창 열기
function showMyQuantityExplanation(event) {
    const explanation = document.getElementById('myQuantityExplanation'); // ID 변경
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 내 주식-수량 설명창 닫기
function closeMyQuantityExplanation() {
    const explanation = document.getElementById('myQuantityExplanation'); // ID 변경
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.myquantity-hoverable-area'); // 클래스 이름 변경

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 1주 평균 금액 설명창 열기
function showAveragePriceExplanation(event) {
    const explanation = document.getElementById('averagePriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 210}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 1주 평균 금액 설명창 닫기
function closeAveragePriceExplanation() {
    const explanation = document.getElementById('averagePriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.average-price-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 드롭다운 설명창 열기
function showDropdownExplanation(event) {
    const explanation = document.getElementById('dropdownExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 250}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 드롭다운 설명창 닫기
function closeDropdownExplanation() {
    const explanation = document.getElementById('dropdownExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.dropdown-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 시간 프레임 설명창 열기
function showTimeframeExplanation(event) {
    const explanation = document.getElementById('timeframeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 250}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 시간 프레임 설명창 닫기
function closeTimeframeExplanation() {
    const explanation = document.getElementById('timeframeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.timeframe-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 보조지표 설명창 열기
function showIndicatorExplanation(event) {
    const explanation = document.getElementById('indicatorExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 220}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 보조지표 설명창 닫기
function closeIndicatorExplanation() {
    const explanation = document.getElementById('indicatorExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.indicator-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 그리기 설명창 열기
function showDrawingExplanation(event) {
    const explanation = document.getElementById('drawingExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 220}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 그리기 설명창 닫기
function closeDrawingExplanation() {
    const explanation = document.getElementById('drawingExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.drawing-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 종목비교 설명창 열기
function showComparisonExplanation(event) {
    const explanation = document.getElementById('comparisonExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 165}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 종목비교 설명창 닫기
function closeComparisonExplanation() {
    const explanation = document.getElementById('comparisonExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.comparison-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 차트모양 설명창 열기
function showChartStyleExplanation(event) {
    const explanation = document.getElementById('chartStyleExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 차트모양 설명창 닫기
function closeChartStyleExplanation() {
    const explanation = document.getElementById('chartStyleExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.chart-style-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 종목 정보 설명창 열기
function showStockInfoExplanation(event) {
    const explanation = document.getElementById('stockInfoExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();

	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 종목 정보 설명창 닫기
function closeStockInfoExplanation() {
    const explanation = document.getElementById('stockInfoExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.stock-info-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 뉴스 / 공시 설명창 열기
function showNewsInfoExplanation(event) {
    const explanation = document.getElementById('newsInfoExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();

	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 뉴스 / 공시 설명창 닫기
function closeNewsInfoExplanation() {
    const explanation = document.getElementById('newsInfoExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.news-info-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 커뮤니티 설명창 열기
function showCommunityInfoExplanation(event) {
    const explanation = document.getElementById('communityInfoExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 153}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 커뮤니티 설명창 닫기
function closeCommunityInfoExplanation() {
    const explanation = document.getElementById('communityInfoExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.community-info-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 주식 이름 설명창 열기
function showStockNameExplanation(event) {
    const explanation = document.getElementById('stockNameExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 주식 이름 설명창 닫기
function closeStockNameExplanation() {
    const explanation = document.getElementById('stockNameExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.stock-name-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 주식 코드 설명창 열기
function showStockCodeExplanation(event) {
    const explanation = document.getElementById('stockCodeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 주식 코드 설명창 닫기
function closeStockCodeExplanation() {
    const explanation = document.getElementById('stockCodeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.stock-code-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 실시간 주식 가격 설명창 열기
function showStockPriceExplanation(event) {
    const explanation = document.getElementById('stockPriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active'); // 해당 클래스 추가

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

function closeStockPriceExplanation() {
    const explanation = document.getElementById('stockPriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.stock-price-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    hoverableArea.classList.remove('active'); // 클래스 제거

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 차트 제목 설명창 열기
function showChartTitleExplanation(event) {
    const explanation = document.getElementById('chartTitleExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 210}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 차트 제목 설명창 닫기
function closeChartTitleExplanation() {
    const explanation = document.getElementById('chartTitleExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.chart-title-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 체결가 설명창 열기
function showTradePriceExplanation(event) {
    const explanation = document.getElementById('tradePriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 체결가 설명창 닫기
function closeTradePriceExplanation() {
    const explanation = document.getElementById('tradePriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.trade-price-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 체결량 설명창 열기
function showTradeVolumeExplanation(event) {
    const explanation = document.getElementById('tradeVolumeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 140}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 체결량 설명창 닫기
function closeTradeVolumeExplanation() {
    const explanation = document.getElementById('tradeVolumeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.trade-volume-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 등락률 설명창 열기
function showChangeRateExplanation(event) {
    const explanation = document.getElementById('changeRateExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 170}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 등락률 설명창 닫기
function closeChangeRateExplanation() {
    const explanation = document.getElementById('changeRateExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.change-rate-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 거래량 설명창 열기
function showVolumeExplanation(event) {
    const explanation = document.getElementById('volumeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 거래량 설명창 닫기
function closeVolumeExplanation() {
    const explanation = document.getElementById('volumeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.volume-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 시간 설명창 열기
function showTimeExplanation(event) {
    const explanation = document.getElementById('timeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 165}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 시간 설명창 닫기
function closeTimeExplanation() {
    const explanation = document.getElementById('timeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.time-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 내 주식 평균 설명창 열기
function showStockAverageExplanation(event) {
    const explanation = document.getElementById('stockAverageExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 내 주식 평균 설명창 닫기
function closeStockAverageExplanation() {
    const explanation = document.getElementById('stockAverageExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.stock-average-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 구매 후 예상 설명창 열기
function showPurchaseEstimateExplanation(event) {
    const explanation = document.getElementById('purchaseEstimateExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 구매 후 예상 설명창 닫기
function closePurchaseEstimateExplanation() {
    const explanation = document.getElementById('purchaseEstimateExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.purchase-estimate-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 대기 설명창 열기
function showPendingOrderExplanation(event) {
    const explanation = document.getElementById('pendingOrderExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget.closest('.pending-order-hoverable-area'); // 해당 버튼만

    // 설명창 위치 설정
    const rect = event.currentTarget.getBoundingClientRect();
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`;
    explanation.style.left = `${window.scrollX + rect.left - 280}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 해당 버튼만 활성화
    hoverableArea.classList.add('active-highlight');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 대기 설명창 닫기
function closePendingOrderExplanation() {
    const explanation = document.getElementById('pendingOrderExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.pending-order-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 버튼 강조 해제
    if (hoverableArea) {
        hoverableArea.classList.remove('active-highlight');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

// 완료 설명창 열기
function showCompletedOrderExplanation(event) {
    const explanation = document.getElementById('completedOrderExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget.closest('.completed-order-hoverable-area'); // 해당 버튼만

    // 설명창 위치 설정
    const rect = event.currentTarget.getBoundingClientRect();
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 165}px`;
    explanation.style.left = `${window.scrollX + rect.left - 280}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 해당 버튼만 활성화
    hoverableArea.classList.add('active-highlight');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 완료 설명창 닫기
function closeCompletedOrderExplanation() {
    const explanation = document.getElementById('completedOrderExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.completed-order-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 버튼 강조 해제
    if (hoverableArea) {
        hoverableArea.classList.remove('active-highlight');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

// 조건주문 설명창 열기
function showConditionalOrderExplanation(event) {
    const explanation = document.getElementById('conditionalOrderExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget.closest('.conditional-order-hoverable-area'); // 해당 버튼만

    // 설명창 위치 설정
    const rect = event.currentTarget.getBoundingClientRect();
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 200}px`;
    explanation.style.left = `${window.scrollX + rect.left - 280}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 해당 버튼만 활성화
    hoverableArea.classList.add('active-highlight');

    // 버튼 및 주문 유형 영역 비활성화
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 조건주문 설명창 닫기
function closeConditionalOrderExplanation() {
    const explanation = document.getElementById('conditionalOrderExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.conditional-order-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 버튼 강조 해제
    if (hoverableArea) {
        hoverableArea.classList.remove('active-highlight');
    }

    // 버튼 및 주문 유형 영역 활성화
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 가격 설명창 열기
function showPriceExplanation(event) {
    const explanation = document.getElementById('priceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 290}px`; // 위치 조정
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');

    // 주문 유형 영역 비활성화 및 스타일 변경
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();


    // 페이지 클릭 차단
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click'); // 닫기 버튼 클릭 허용
}

// 가격 설명창 닫기
function closePriceExplanation() {
    const explanation = document.getElementById('priceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.price-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 주문 유형 영역 활성화 및 스타일 복원
    enableOrderTypeArea();
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();


    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 판매 - 지정가 설명창 열기
function showSellPriceLimitExplanation(event) {
  const explanation = document.getElementById('sellpriceTypeExplanation');
  const overlay = document.getElementById('overlay');
  const rect = event.currentTarget.getBoundingClientRect();

  // 설명창 위치 설정
  explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 230}px`;
  explanation.style.left = `${window.scrollX + rect.left - 87}px`;

  // 설명창과 오버레이 표시
  explanation.style.display = 'block';
  overlay.style.display = 'block';
  overlay.classList.add('active'); // 오버레이 활성화

  // 주문 유형 및 버튼 영역 비활성화
  disableOrderTypeArea();
  disablePriceLimitButton();
  disableMarketButton();
  disableSellOrderTypeArea();

  // 셀렉트 창 클릭 차단
  const selectElements = document.querySelectorAll('select');
  selectElements.forEach((select) => {
    select.classList.add('no-click');
  });

  // 페이지 클릭 차단, 닫기 버튼 제외
  document.body.classList.add('no-click');
  explanation.querySelector('button').classList.add('allow-click'); // X 버튼 클릭 허용
}

// 판매 - 지정가 설명창 닫기
function closeSellPriceLimitExplanation() {
  const explanation = document.getElementById('sellpriceTypeExplanation');
  const overlay = document.getElementById('overlay');

  // 설명창 숨기기
  explanation.style.display = 'none';
  overlay.style.display = 'none';
  overlay.classList.remove('active'); // 오버레이 비활성화

  // 버튼 및 주문 유형 영역 복구
  enableOrderTypeArea();
  enablePriceLimitButton();
  enableMarketButton();
  enableSellOrderTypeArea();

  // 셀렉트 창 클릭 차단 해제
  const selectElements = document.querySelectorAll('select');
  selectElements.forEach((select) => {
    select.classList.remove('no-click');
  });

  // 페이지 클릭 허용
  document.body.classList.remove('no-click');
}

// 판매 - 시장가 설명창 열기
function showSellMarketPriceExplanation(event) {
  const explanation = document.getElementById('sellmarketPriceExplanation');
  const overlay = document.getElementById('overlay');
  const rect = event.currentTarget.getBoundingClientRect();

  // 설명창 위치 설정
  explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`;
  explanation.style.left = `${window.scrollX + rect.left - 87}px`;

  // 설명창과 오버레이 표시
  explanation.style.display = 'block';
  overlay.style.display = 'block';
  overlay.classList.add('active'); // 오버레이 활성화

  // 주문 유형 및 버튼 영역 비활성화
  disableOrderTypeArea();
  disableSellOrderTypeArea();

  // 셀렉트 창 클릭 차단
  const selectElements = document.querySelectorAll('select');
  selectElements.forEach((select) => {
    select.classList.add('no-click');
  });

  // 페이지 클릭 차단
  document.body.classList.add('no-click');
  explanation.querySelector('button').classList.add('allow-click'); // 닫기 버튼 클릭 허용
}

// 판매 - 시장가 설명창 닫기
function closeSellMarketPriceExplanation() {
  const explanation = document.getElementById('sellmarketPriceExplanation');
  const overlay = document.getElementById('overlay');

  // 설명창 숨기기
  explanation.style.display = 'none';
  overlay.style.display = 'none';
  overlay.classList.remove('active'); // 오버레이 비활성화

  // 주문 유형 및 버튼 영역 복구
  enableOrderTypeArea();
  enableSellOrderTypeArea();

  // 셀렉트 창 클릭 차단 해제
  const selectElements = document.querySelectorAll('select');
  selectElements.forEach((select) => {
    select.classList.remove('no-click');
  });

  // 페이지 클릭 허용
  document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 판매 - 주문 유형 설명창 열기
function showSellOrderTypeExplanation(event) {
    const explanation = document.getElementById('sellOrderTypeExplanation');
    const overlay = document.getElementById('overlay'); // 기존 overlay 재사용
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 180}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 주문 유형 hoverable-area 활성화
    hoverableArea.classList.add('active');
	
    // 기타 버튼 비활성화 (필요 시 구현)
    disablePriceLimitButton();
    disableMarketButton();
	// 판매 지정가 버튼 영역 비활성화
	disableSellPriceLimitButton();
	// 판매 시장가 버튼 영역 비활성화
	disableSellMarketButton();

    // 페이지 클릭 차단, 닫기 버튼과 셀렉트 박스는 제외
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click');

    const selectBox = hoverableArea.querySelector('select');
    if (selectBox) {
        selectBox.classList.add('allow-click');
        selectBox.addEventListener('click', preventExplanationMovement);
        selectBox.addEventListener('change', preventExplanationMovement);
    }
}

// 판매 - 주문 유형 설명창 닫기
function closeSellOrderTypeExplanation() {
    const explanation = document.getElementById('sellOrderTypeExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.sell-orderType-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 주문 유형 hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }
	
    // 기타 버튼 복구 (필요 시 구현)
    enablePriceLimitButton();
    enableMarketButton();
	enableSellPriceLimitButton();
	enableSellMarketButton();
    // 페이지 클릭 허용
    document.body.classList.remove('no-click');

    const selectBox = hoverableArea.querySelector('select');
    if (selectBox) {
        selectBox.classList.remove('allow-click');
        selectBox.removeEventListener('click', preventExplanationMovement);
        selectBox.removeEventListener('change', preventExplanationMovement);
    }
}

function preventExplanationMovement(event) {
    event.stopPropagation();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 예상 수익률 설명창 열기
function showSellProfitExplanation(event) {
    const explanation = document.getElementById('sellProfitExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 190}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 예상 수익률 설명창 닫기
function closeSellProfitExplanation() {
    const explanation = document.getElementById('sellProfitExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.sell-profit-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';
	
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();
	
    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 예상 손익 설명창 열기
function showSellProfitAmountExplanation(event) {
    const explanation = document.getElementById('sellProfitAmountExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 210}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 예상 손익 설명창 닫기
function closeSellProfitAmountExplanation() {
    const explanation = document.getElementById('sellProfitAmountExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.sell-profit-amount-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';
	
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 총 금액 설명창 열기
function showSellTotalPriceExplanation(event) {
    const explanation = document.getElementById('sellTotalPriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();
	
	resetActiveAreas();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 210}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // hoverable-area 활성화
    hoverableArea.classList.add('active');
	
	disableSellOrderTypeArea();
	disableSellPriceLimitButton();
	disableSellMarketButton();

    // 페이지 클릭 차단
    document.body.classList.add('no-click');

    // 닫기 버튼만 클릭 가능하도록 처리
    explanation.querySelector('button').classList.add('allow-click');
}

// 총 금액 설명창 닫기
function closeSellTotalPriceExplanation() {
    const explanation = document.getElementById('sellTotalPriceExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.sell-total-price-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';
	
	enableSellPriceLimitButton();
	enableSellMarketButton();
	enableSellOrderTypeArea();

    // hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 판매부분 비율 퍼센트 설명창 열기
function showSellPercentageExplanation(event) {
    const explanation = document.getElementById('sellpercentageExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = event.currentTarget;
    const rect = hoverableArea.getBoundingClientRect();

    // 다른 hoverable-area의 활성화 상태 초기화
    resetActiveAreas();

    // 설명창 위치 설정
    explanation.style.top = `${window.scrollY + rect.top - explanation.offsetHeight - 220}px`;
    explanation.style.left = `${window.scrollX + rect.left}px`;

    // 설명창과 오버레이 표시
    explanation.style.display = 'block';
    overlay.style.display = 'block';

    // 구매 비율 hoverable-area 활성화
    hoverableArea.classList.add('active');
    
    // 주문 유형 영역 비활성화 및 스타일 변경
    disableOrderTypeArea();
    disablePriceLimitButton();
    disableMarketButton();
    disableSellOrderTypeArea();
    disableSellPriceLimitButton();
    disableSellMarketButton();

    // 페이지 클릭 차단, 닫기 버튼만 허용
    document.body.classList.add('no-click');
    explanation.querySelector('button').classList.add('allow-click');

    // 주문 유형 영역의 클릭 및 hover 차단
    const orderTypeArea = document.querySelector('.orderType-hoverable-area');
    if (orderTypeArea) {
        orderTypeArea.classList.add('disabled'); // 클릭 비활성화
    }
}

// 판매부분 비율 퍼센트 설명창 닫기
function closeSellPercentageExplanation() {
    const explanation = document.getElementById('sellpercentageExplanation');
    const overlay = document.getElementById('overlay');
    const hoverableArea = document.querySelector('.sell-percentage-hoverable-area');

    // 설명창과 오버레이 숨기기
    explanation.style.display = 'none';
    overlay.style.display = 'none';

    // 구매 비율 hoverable-area 비활성화
    if (hoverableArea) {
        hoverableArea.classList.remove('active');
    }

    // 주문 유형 영역의 클릭 및 hover 복원
    const orderTypeArea = document.querySelector('.orderType-hoverable-area');
    if (orderTypeArea) {
        orderTypeArea.classList.remove('disabled'); // 클릭 활성화
    }
    
    // 주문 유형 영역 활성화 및 스타일 복원
    enableOrderTypeArea();

    // 버튼 복구
    enablePriceLimitButton();
    enableMarketButton();
    enableSellPriceLimitButton();
    enableSellMarketButton();
    enableSellOrderTypeArea();

    // 페이지 클릭 허용
    document.body.classList.remove('no-click');
}

// 다른 활성화된 영역 초기화 함수
function resetActiveAreas() {
    // 모든 활성화된 hoverable-area 초기화
    const activeAreas = document.querySelectorAll('.sell-percentage-hoverable-area.active, .sell-total-price-hoverable-area.active');
    activeAreas.forEach(area => {
        area.classList.remove('active');
    });

    // 설명창과 오버레이 숨기기
    const explanations = document.querySelectorAll('.explanation-box');
    explanations.forEach(explanation => {
        explanation.style.display = 'none';
    });

    // 오버레이 숨기기
    const overlay = document.getElementById('overlay');
    if (overlay) {
        overlay.style.display = 'none';
    }
}
