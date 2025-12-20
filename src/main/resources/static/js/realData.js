// ====================================================================
// 🔥 실시간 주식 데이터 테이블 업데이트 (수정 버전)
// ====================================================================

// WebSocket 연결 설정
const ws = new WebSocket("ws://localhost:9000");

// 초기화 WebSocket 연결 성공 핸들러
ws.onopen = function () {
    console.log("✅ WebSocket 연결 성공");
    initializeTable(); // 테이블 초기화
};

// WebSocket 메시지 수신 핸들러
ws.onmessage = function (event) {
    const rawMessage = event.data;
    console.log("📨 수신 원본 메시지:", rawMessage);

    // ✅ 수정: 직접 JSON 파싱 (복잡한 파싱 로직 제거)
    let parsedData;
    try {
        parsedData = JSON.parse(rawMessage);
        console.log("✅ 파싱된 데이터:", parsedData);
    } catch (error) {
        console.error("❌ JSON 파싱 실패:", error);
        return;
    }

    // 필수 필드 확인
    if (!parsedData || !parsedData.price) {
        console.warn("⚠️  유효하지 않은 데이터:", parsedData);
        return;
    }

    // 테이블 업데이트
    updateTable(parsedData);

    // 차트 업데이트
    if (window.stockChart) {
        updateLineChart(window.stockChart, parsedData);
    } else {
        console.warn("⚠️  차트가 아직 초기화되지 않았습니다.");
    }
};

// WebSocket 연결 종료 핸들러
ws.onclose = function () {
    console.log("❌ WebSocket 연결 종료");
    reconnectWebSocket();
};

// WebSocket 오류 핸들러
ws.onerror = function (error) {
    console.error("❌ WebSocket 오류:", error);
};

// WebSocket 재연결 로직
function reconnectWebSocket() {
    console.log("🔄 WebSocket 재연결 시도 중...");
    setTimeout(() => {
        const newWs = new WebSocket("ws://localhost:9000");
        newWs.onopen = ws.onopen;
        newWs.onmessage = ws.onmessage;
        newWs.onclose = ws.onclose;
        newWs.onerror = ws.onerror;
    }, 3000); // 3초 후 재연결 시도
}

// 테이블 초기화
function initializeTable() {
    const tableBody = document.querySelector("#realTimeData");
    if (tableBody) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="real-time-table-empty">실시간 데이터가 없습니다.</td>
            </tr>
        `;
    }
}

// 실시간 테이블 업데이트
let previousPrice = null;
const MAX_TABLE_ROWS = 15; // ✅ 수정: 최대 15개 행 표시

function updateTable(data) {
    const tableBody = document.querySelector("#realTimeData");

    if (!data || !tableBody) {
        console.warn("⚠️  테이블 업데이트 실패: data 또는 tableBody가 없음");
        return;
    }

    // 첫 번째 빈 행 제거 (초기화 시 생성된 행)
    if (tableBody.rows.length === 1 && 
        tableBody.rows[0].querySelector('.real-time-table-empty')) {
        tableBody.innerHTML = '';
    }

    // 등락률 계산
    let priceChangeRate = 0;
    if (previousPrice !== null && previousPrice !== 0) {
        priceChangeRate = ((data.price - previousPrice) / previousPrice) * 100;
    }
    previousPrice = data.price;

    // 등락률 스타일 클래스
    const changeRateClass =
        priceChangeRate > 0
            ? "change-rate-positive"
            : priceChangeRate < 0
            ? "change-rate-negative"
            : "";

    // ✅ 수정: 시간 포맷 개선
    const formattedTime = formatTimestamp(data.timestamp);
    
    // ✅ 수정: 각 필드 안전하게 처리
    const price = data.price ? data.price.toLocaleString() : '0';
    const volume = data.volume ? data.volume.toLocaleString() : '0';
    const cumulativeVolume = data.cumulativeVolume ? data.cumulativeVolume.toLocaleString() : '0';
    const change = priceChangeRate.toFixed(2);

    console.log("📊 테이블 데이터:", {
        price, volume, change, cumulativeVolume, formattedTime
    });

    // 기존 행 확인 (같은 시간의 데이터인지)
    const existingRow = Array.from(tableBody.rows).find(
        (row) => row.cells[4]?.innerText === formattedTime
    );

    if (existingRow) {
        // 기존 행 업데이트 (같은 시간의 데이터면 덮어쓰기)
        existingRow.cells[0].innerText = `${price}원`;
        existingRow.cells[1].innerText = volume;
        existingRow.cells[2].innerText = `${change}%`;
        existingRow.cells[2].className = changeRateClass;
        existingRow.cells[3].innerText = cumulativeVolume;
        console.log("🔄 기존 행 업데이트:", formattedTime);
    } else {
        // 새로운 행 추가 (맨 위에)
        const newRow = `
            <tr>
                <td>${price}원</td>
                <td>${volume}</td>
                <td class="${changeRateClass}">${change}%</td>
                <td>${cumulativeVolume}</td>
                <td>${formattedTime}</td>
            </tr>
        `;
        tableBody.insertAdjacentHTML("afterbegin", newRow);
        console.log("➕ 새 행 추가:", formattedTime);

        // ✅ 수정: 행 개수 제한 (15개 초과 시 마지막 행 삭제)
        if (tableBody.rows.length > MAX_TABLE_ROWS) {
            tableBody.deleteRow(-1); // 마지막 행 제거
            console.log("🗑️  오래된 행 삭제 (최대 15개 유지)");
        }
    }
}

// 시간 포맷 변환 (HHmmss → HH:mm:ss)
function formatTimestamp(timestamp) {
    // ✅ 수정: 다양한 시간 포맷 지원
    if (!timestamp) return "시간 정보 없음";
    
    // 문자열로 변환
    const timeStr = String(timestamp);
    
    // "HH:mm:ss" 형식인 경우 (이미 포맷됨)
    if (timeStr.includes(':')) {
        return timeStr;
    }
    
    // "HHmmss" 형식인 경우 (6자리)
    if (timeStr.length === 6) {
        const hh = timeStr.slice(0, 2);
        const mm = timeStr.slice(2, 4);
        const ss = timeStr.slice(4, 6);
        return `${hh}:${mm}:${ss}`;
    }
    
    // 기타 형식
    return "시간 정보 없음";
}

// 차트 업데이트 (필요 시 사용)
function updateLineChart(chart, data) {
    if (!chart || !data) return;

    const formattedTime = formatTimestamp(data.timestamp);
    chart.data.labels.push(formattedTime);
    chart.data.datasets[0].data.push(data.price);

    // 차트 데이터 포인트 제한 (최대 50개)
    if (chart.data.labels.length > 50) {
        chart.data.labels.shift();
        chart.data.datasets[0].data.shift();
    }

    chart.update();
}

console.log("✅ 실시간 데이터 테이블 스크립트 로드 완료");