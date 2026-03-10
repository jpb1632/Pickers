let ws;
let reconnectTimer = null;

const MAX_TABLE_ROWS = 15;
const RECONNECT_DELAY_MS = 3000;
const previousPriceBySymbol = new Map();

function getTableBody() {
    return document.querySelector("#realTimeData");
}

function getCurrentStockNum() {
    const stockSection = document.getElementById("QnM5MjbkF8");
    return stockSection?.dataset.stockNum ?? "";
}

function getRealtimeWsUrl() {
    const stockSection = document.getElementById("QnM5MjbkF8");
    return stockSection?.dataset.wsUrl ?? "";
}

function isCurrentStockData(data) {
    const currentStockNum = getCurrentStockNum();
    if (!currentStockNum) {
        return true;
    }

    return String(data?.symbol ?? "") === currentStockNum;
}

function initializeTable() {
    const tableBody = getTableBody();
    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = `
        <tr>
            <td colspan="5" class="real-time-table-empty">실시간 데이터가 없습니다.</td>
        </tr>
    `;
}

function formatTimestamp(timestamp) {
    if (!timestamp) {
        return "시간 정보 없음";
    }

    const timeText = String(timestamp);
    if (timeText.includes(":")) {
        return timeText;
    }

    if (timeText.length !== 6) {
        return "시간 정보 없음";
    }

    return `${timeText.slice(0, 2)}:${timeText.slice(2, 4)}:${timeText.slice(4, 6)}`;
}

function formatNumber(value) {
    return Number(value || 0).toLocaleString();
}

function clearEmptyRow(tableBody) {
    if (
        tableBody.rows.length === 1 &&
        tableBody.rows[0].querySelector(".real-time-table-empty")
    ) {
        tableBody.innerHTML = "";
    }
}

function calculateChangeRate(symbol, currentPrice) {
    const previousPrice = previousPriceBySymbol.get(symbol);
    previousPriceBySymbol.set(symbol, currentPrice);

    if (typeof previousPrice !== "number" || previousPrice === 0) {
        return 0;
    }

    return ((currentPrice - previousPrice) / previousPrice) * 100;
}

function getChangeRateClass(changeRate) {
    if (changeRate > 0) {
        return "change-rate-positive";
    }

    if (changeRate < 0) {
        return "change-rate-negative";
    }

    return "";
}

function updateTable(data) {
    const tableBody = getTableBody();
    if (!data || !tableBody) {
        return;
    }

    clearEmptyRow(tableBody);

    const symbol = String(data.symbol ?? "");
    const formattedTime = formatTimestamp(data.timestamp);
    const price = formatNumber(data.price);
    const volume = formatNumber(data.volume);
    const cumulativeVolume = formatNumber(data.cumulativeVolume);
    const changeRate = calculateChangeRate(symbol, Number(data.price || 0));
    const changeRateText = `${changeRate.toFixed(2)}%`;
    const changeRateClass = getChangeRateClass(changeRate);

    const existingRow = Array.from(tableBody.rows).find(function (row) {
        return row.cells[4]?.innerText === formattedTime;
    });

    if (existingRow) {
        existingRow.cells[0].innerText = `${price}원`;
        existingRow.cells[1].innerText = volume;
        existingRow.cells[2].innerText = changeRateText;
        existingRow.cells[2].className = changeRateClass;
        existingRow.cells[3].innerText = cumulativeVolume;
        return;
    }

    tableBody.insertAdjacentHTML(
        "afterbegin",
        `
            <tr>
                <td>${price}원</td>
                <td>${volume}</td>
                <td class="${changeRateClass}">${changeRateText}</td>
                <td>${cumulativeVolume}</td>
                <td>${formattedTime}</td>
            </tr>
        `
    );

    while (tableBody.rows.length > MAX_TABLE_ROWS) {
        tableBody.deleteRow(-1);
    }
}

function scheduleReconnect() {
    if (reconnectTimer) {
        return;
    }

    reconnectTimer = setTimeout(function () {
        reconnectTimer = null;
        connectWebSocket();
    }, RECONNECT_DELAY_MS);
}

function bindWebSocket(socket) {
    socket.onopen = function () {
        initializeTable();
    };

    socket.onmessage = function (event) {
        let parsedData;

        try {
            parsedData = JSON.parse(event.data);
        } catch (error) {
            console.error("JSON parse error:", error);
            return;
        }

        if (!parsedData || typeof parsedData.price !== "number") {
            return;
        }

        if (!isCurrentStockData(parsedData)) {
            return;
        }

        updateTable(parsedData);
    };

    socket.onclose = function () {
        scheduleReconnect();
    };

    socket.onerror = function (error) {
        console.error("WebSocket error:", error);
    };
}

function connectWebSocket() {
    const realtimeWsUrl = getRealtimeWsUrl();
    if (!realtimeWsUrl) {
        console.error("Realtime WebSocket URL is not configured.");
        return;
    }

    ws = new WebSocket(realtimeWsUrl);
    bindWebSocket(ws);
}

document.addEventListener("DOMContentLoaded", function () {
    connectWebSocket();
});
