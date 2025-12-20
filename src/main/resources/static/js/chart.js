document.addEventListener("DOMContentLoaded", function () {
    let chart; // 차트 객체
    const ctx = document.getElementById('realTimeChart').getContext('2d');
    let ws; // WebSocket 객체
    const maxDataPoints = 50; // 최대 데이터 포인트 수

    const chartContainer = document.querySelector(".chart-container");
    const canvas = document.getElementById("realTimeChart");

    // 컨테이너와 캔버스 높이 설정
    chartContainer.style.height = "420px";
    canvas.style.height = "100%";

    // WebSocket 초기화 함수
    function initializeWebSocket() {
        ws = new WebSocket("ws://localhost:9000");

        ws.onopen = () => {
            console.log("WebSocket 연결 성공");
            initializeChart(); // 차트 초기화
        };

        ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log("받은 데이터:", data);
            if (chart) {
                updateLineChart(chart, data);
            } else {
                console.warn("차트가 아직 초기화되지 않았습니다.");
            }
        };

        ws.onclose = () => {
            console.log("WebSocket 연결 종료");
            showConnectionStatus(false); // 연결 상태 표시
            setTimeout(initializeWebSocket, 4000); // 4초 후 재연결 시도
        };

        ws.onerror = (error) => {
            console.error("WebSocket 오류:", error);
        };
    }

    // 연결 상태 표시 함수
    function showConnectionStatus(isConnected) {
        const statusElement = document.getElementById("connectionStatus");
        if (isConnected) {
            statusElement.textContent = "연결 상태: 연결됨";
            statusElement.style.color = "green";
        } else {
            statusElement.textContent = "연결 상태: 연결 끊김 (재연결 중...)";
            statusElement.style.color = "red";
        }
    }

    // 차트 초기화 함수
    function initializeChart() {
        chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [
                    {
                        label: '실시간 가격',
                        data: [],
                        borderColor: 'rgba(75, 192, 192, 1)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        fill: false,
                        tension: 0.4,
                        pointRadius: 3,
                    },
                ],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false, 
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: 'minute',
                            displayFormats: {
                                minute: 'HH:mm',
                            },
                        },
                        title: {
                            display: true,
                            text: '',
                        },
                    },
                    y: {
                        beginAtZero: false,
                        ticks: {
                            callback: function (value) {
                                return value.toLocaleString() + '원'; // 축 레이블에 '원' 추가
                            },
                        },
                        title: {
                            display: true,
                            text: '가격',
                        },
                    },
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                    },
                },
            },
        });
        console.log("차트 초기화 완료");
    }

    // 차트 업데이트 함수
    function updateLineChart(chart, data) {
        if (!data || typeof data.price !== "number") {
            console.warn("유효하지 않은 데이터입니다:", data);
            return;
        }

        const currentTime = new Date(); // 현재 시간
        chart.data.labels.push(currentTime); // x축 레이블 추가
        chart.data.datasets[0].data.push(data.price); // y축 데이터 추가

        // 데이터 포인트 제한
        if (chart.data.labels.length > maxDataPoints) {
            chart.data.labels.shift();
            chart.data.datasets[0].data.shift();
        }

        // Y축 범위 조정
        const maxPrice = Math.ceil(Math.max(...chart.data.datasets[0].data));
        const minPrice = maxPrice - 400; // 최대값 - 400원
        chart.options.scales.y.min = minPrice;
        chart.options.scales.y.max = maxPrice + 200; // 최대값 + 200원

        chart.update(); // 차트 업데이트
    }

    // WebSocket 및 차트 초기화
    initializeWebSocket();
});
