document.addEventListener("DOMContentLoaded", function () {
    const canvas = document.getElementById("realTimeChart");
    const stockSection = document.getElementById("QnM5MjbkF8");
    const currentPriceLabel = document.getElementById("currentStockPrice");
    const priceInputs = document.querySelectorAll('[data-role="current-price-input"]');
    const currentStockNum = stockSection?.dataset.stockNum ?? "";
    const realtimeWsUrl = stockSection?.dataset.wsUrl ?? "";
    const points = [];
    const numberFormatter = new Intl.NumberFormat("ko-KR");

    const MAX_POINTS = 300;
    const VIEW_WINDOW_MS = 3 * 60 * 1000;
    const VIEW_WINDOW_BUFFER_MS = 10 * 1000;
    const MIN_RENDER_INTERVAL_MS = 180;
    const SHORT_MA_PERIOD = 12;
    const LONG_MA_PERIOD = 30;
    const RECONNECT_DELAY_MS = 4000;

    let chart;
    let socket;
    let renderScheduled = false;
    let reconnectTimer = null;
    let lastRenderAt = 0;

    if (!canvas || typeof Chart === "undefined") {
        return;
    }

    function matchesCurrentStock(data) {
        if (!currentStockNum) {
            return true;
        }

        return String(data?.symbol ?? "") === currentStockNum;
    }

    function formatPrice(value) {
        return `${numberFormatter.format(Math.round(Number(value || 0)))}원`;
    }

    function formatAxisPrice(value) {
        return numberFormatter.format(Math.round(Number(value || 0)));
    }

    function parseCurrentPrice(text) {
        return Number(String(text ?? "").replace(/[^0-9.-]/g, "")) || 0;
    }

    function parseTimestamp(timestamp) {
        const matched = String(timestamp ?? "").match(/^(\d{2}):(\d{2}):(\d{2})$/);
        const date = new Date();

        if (!matched) {
            return date;
        }

        date.setHours(Number(matched[1]), Number(matched[2]), Number(matched[3]), 0);
        return date;
    }

    function updateCurrentPriceDisplay(price) {
        if (!Number.isFinite(price)) {
            return;
        }

        const formattedPrice = formatPrice(price);

        if (currentPriceLabel) {
            currentPriceLabel.textContent = formattedPrice;
        }

        priceInputs.forEach(function (input) {
            input.value = formattedPrice;
        });
    }

    function seedInitialPoint() {
        if (points.length > 0) {
            return;
        }

        const initialPrice = parseCurrentPrice(currentPriceLabel?.textContent);
        if (initialPrice <= 0) {
            return;
        }

        points.push({
            x: new Date(),
            y: initialPrice,
        });
    }

    function trimPoints(latestTime) {
        while (
            points.length > 0 &&
            latestTime - points[0].x.getTime() > VIEW_WINDOW_MS + VIEW_WINDOW_BUFFER_MS
        ) {
            points.shift();
        }

        while (points.length > MAX_POINTS) {
            points.shift();
        }
    }

    function normalizePointTime(date) {
        const normalized = new Date(date);
        normalized.setMilliseconds(0);
        return normalized;
    }

    function buildMovingAverage(period) {
        return points.map(function (point, index) {
            if (index + 1 < period) {
                return { x: point.x, y: null };
            }

            const range = points.slice(index - period + 1, index + 1);
            const sum = range.reduce(function (acc, item) {
                return acc + item.y;
            }, 0);

            return {
                x: point.x,
                y: Math.round(sum / period),
            };
        });
    }

    function getSegmentColor(context) {
        const from = context.p0.parsed.y;
        const to = context.p1.parsed.y;

        if (to > from) {
            return "#ef4444";
        }

        if (to < from) {
            return "#10b981";
        }

        return "#94a3b8";
    }

    function getLatestPointColor() {
        if (points.length < 2) {
            return "#ef4444";
        }

        const previousPrice = points[points.length - 2].y;
        const currentPrice = points[points.length - 1].y;

        if (currentPrice > previousPrice) {
            return "#ef4444";
        }

        if (currentPrice < previousPrice) {
            return "#10b981";
        }

        return "#94a3b8";
    }

    function buildDatasets() {
        return [
            {
                label: "Price",
                data: [],
                parsing: false,
                borderWidth: 2.4,
                tension: 0.32,
                cubicInterpolationMode: "monotone",
                pointRadius: function (context) {
                    return context.dataIndex === points.length - 1 ? 2.8 : 0;
                },
                pointHoverRadius: 4,
                pointBorderWidth: 0,
                pointBackgroundColor: function () {
                    return getLatestPointColor();
                },
                segment: {
                    borderColor: function (context) {
                        return getSegmentColor(context);
                    },
                },
            },
            {
                label: "MA12",
                data: [],
                parsing: false,
                borderColor: "#f59e0b",
                borderWidth: 1.6,
                pointRadius: 0,
                tension: 0.28,
                cubicInterpolationMode: "monotone",
                spanGaps: true,
            },
            {
                label: "MA30",
                data: [],
                parsing: false,
                borderColor: "#8b5cf6",
                borderWidth: 1.4,
                pointRadius: 0,
                tension: 0.28,
                cubicInterpolationMode: "monotone",
                spanGaps: true,
            },
        ];
    }

    function createChart() {
        if (chart) {
            return;
        }

        chart = new Chart(canvas.getContext("2d"), {
            type: "line",
            data: {
                datasets: buildDatasets(),
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                animation: false,
                normalized: true,
                interaction: {
                    mode: "index",
                    intersect: false,
                },
                layout: {
                    padding: {
                        top: 12,
                        right: 8,
                        bottom: 4,
                        left: 8,
                    },
                },
                plugins: {
                    legend: {
                        display: true,
                        position: "top",
                        align: "end",
                        labels: {
                            usePointStyle: true,
                            pointStyle: "line",
                            boxWidth: 12,
                            color: "#64748b",
                            font: {
                                size: 11,
                                weight: "600",
                            },
                        },
                    },
                    tooltip: {
                        backgroundColor: "rgba(255, 255, 255, 0.96)",
                        titleColor: "#0f172a",
                        bodyColor: "#334155",
                        borderColor: "#dbe3ef",
                        borderWidth: 1,
                        padding: 10,
                        callbacks: {
                            label: function (context) {
                                if (context.parsed.y == null) {
                                    return null;
                                }

                                return `${context.dataset.label}: ${formatPrice(context.parsed.y)}`;
                            },
                        },
                    },
                },
                scales: {
                    x: {
                        type: "timeseries",
                        time: {
                            unit: "minute",
                            displayFormats: {
                                minute: "HH:mm",
                                second: "HH:mm:ss",
                            },
                        },
                        grid: {
                            color: "rgba(203, 213, 225, 0.25)",
                            drawTicks: false,
                        },
                        ticks: {
                            color: "#94a3b8",
                            maxRotation: 0,
                            autoSkip: true,
                            maxTicksLimit: 7,
                            font: {
                                size: 11,
                            },
                        },
                        border: {
                            display: false,
                        },
                    },
                    y: {
                        position: "right",
                        grid: {
                            color: "rgba(203, 213, 225, 0.3)",
                            drawTicks: false,
                        },
                        ticks: {
                            color: "#64748b",
                            callback: function (value) {
                                return formatAxisPrice(value);
                            },
                            font: {
                                size: 11,
                            },
                        },
                        border: {
                            display: false,
                        },
                    },
                },
            },
        });

        renderChart();
    }

    function updateAxisRange() {
        if (!chart || points.length === 0) {
            return;
        }

        const prices = points.map(function (point) {
            return point.y;
        });
        const minPrice = Math.min.apply(null, prices);
        const maxPrice = Math.max.apply(null, prices);
        const baseRange = maxPrice - minPrice || maxPrice * 0.01;
        const padding = Math.max(50, Math.ceil(baseRange * 0.18));
        const latestTime = points[points.length - 1].x.getTime();

        chart.options.scales.y.min = Math.max(0, minPrice - padding);
        chart.options.scales.y.max = maxPrice + padding;
        chart.options.scales.x.min = latestTime - VIEW_WINDOW_MS;
        chart.options.scales.x.max = latestTime + 1000;
    }

    function renderChart() {
        if (!chart) {
            return;
        }

        chart.data.datasets[0].data = points.slice();
        chart.data.datasets[1].data = buildMovingAverage(SHORT_MA_PERIOD);
        chart.data.datasets[2].data = buildMovingAverage(LONG_MA_PERIOD);
        updateAxisRange();
        chart.update("none");
    }

    function scheduleRender() {
        if (renderScheduled) {
            return;
        }

        renderScheduled = true;

        const elapsed = Date.now() - lastRenderAt;
        const delay = Math.max(0, MIN_RENDER_INTERVAL_MS - elapsed);

        window.setTimeout(function () {
            window.requestAnimationFrame(function () {
                renderScheduled = false;
                lastRenderAt = Date.now();
                renderChart();
            });
        }, delay);
    }

    function appendPoint(data) {
        const price = Math.round(Number(data?.price || 0));
        if (price <= 0) {
            return;
        }

        const pointTime = normalizePointTime(parseTimestamp(data?.timestamp));
        const lastPoint = points[points.length - 1];

        if (lastPoint && lastPoint.x.getTime() === pointTime.getTime()) {
            lastPoint.y = price;
        } else {
            points.push({
                x: pointTime,
                y: price,
            });
        }

        trimPoints(pointTime.getTime());
        updateCurrentPriceDisplay(price);
        scheduleRender();
    }

    function scheduleReconnect() {
        if (reconnectTimer) {
            return;
        }

        reconnectTimer = window.setTimeout(function () {
            reconnectTimer = null;
            connectWebSocket();
        }, RECONNECT_DELAY_MS);
    }

    function connectWebSocket() {
        if (!realtimeWsUrl) {
            console.error("Realtime WebSocket URL is not configured.");
            return;
        }

        socket = new WebSocket(realtimeWsUrl);

        socket.onmessage = function (event) {
            let data;

            try {
                data = JSON.parse(event.data);
            } catch (error) {
                console.error("JSON parsing error:", error);
                return;
            }

            if (!matchesCurrentStock(data)) {
                return;
            }

            createChart();
            appendPoint(data);
        };

        socket.onclose = function () {
            scheduleReconnect();
        };

        socket.onerror = function (error) {
            console.error("WebSocket error:", error);
        };
    }

    seedInitialPoint();
    createChart();
    connectWebSocket();
});
