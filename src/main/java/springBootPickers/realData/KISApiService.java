package springBootPickers.realData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KISApiService {

    private static final Logger log = LoggerFactory.getLogger(KISApiService.class);

    private static final String BASE_URL = "https://openapi.koreainvestment.com:9443";
    private static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String CURRENT_PRICE_TR_ID = "FHKST01010100";

    public String getApprovalKey(String appKey, String appSecret) {
        JSONObject requestBody = createCredentialBody(appKey, appSecret, "secretkey");
        return requestToken("/oauth2/Approval", requestBody, "approval_key", "접속키");
    }

    public String getAccessToken(String appKey, String appSecret) {
        JSONObject requestBody = createCredentialBody(appKey, appSecret, "appsecret");
        return requestToken("/oauth2/tokenP", requestBody, "access_token", "액세스 토큰");
    }

    public Long getDomesticCurrentPrice(String accessToken, String appKey, String appSecret, String stockCode) {
        if (isBlank(accessToken) || isBlank(stockCode)) {
            return null;
        }

        try {
            HttpURLConnection connection = openConnection(
                    buildCurrentPriceUrl(stockCode),
                    "GET"
            );
            connection.setRequestProperty("authorization", "Bearer " + accessToken);
            connection.setRequestProperty("appkey", appKey);
            connection.setRequestProperty("appsecret", appSecret);
            connection.setRequestProperty("tr_id", CURRENT_PRICE_TR_ID);
            connection.setRequestProperty("custtype", "P");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.warn("현재가 조회 실패 - 종목 {}, HTTP {}", stockCode, responseCode);
                return null;
            }

            JSONObject output = readJsonResponse(connection).optJSONObject("output");
            if (output == null) {
                log.warn("현재가 조회 응답에 output이 없습니다. 종목={}", stockCode);
                return null;
            }

            long currentPrice = parseLong(output.optString("stck_prpr", ""));
            return currentPrice > 0 ? currentPrice : null;
        } catch (Exception e) {
            log.warn("현재가 조회 실패 - 종목 {}: {}", stockCode, e.getMessage());
            return null;
        }
    }

    private String requestToken(String path, JSONObject requestBody, String responseField, String label) {
        try {
            HttpURLConnection connection = openConnection(BASE_URL + path, "POST");
            connection.setDoOutput(true);
            writeJsonBody(connection, requestBody);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("{} 발급 실패 (HTTP {})", label, responseCode);
                return null;
            }

            String token = readJsonResponse(connection).optString(responseField, "");
            if (token.isBlank()) {
                log.error("{} 응답에 {} 값이 없습니다.", label, responseField);
                return null;
            }

            log.info("{} 발급 완료", label);
            return token;
        } catch (Exception e) {
            log.error("{} 발급 중 오류", label, e);
            return null;
        }
    }

    private JSONObject createCredentialBody(String appKey, String appSecret, String secretFieldName) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", CLIENT_CREDENTIALS);
        requestBody.put("appkey", appKey);
        requestBody.put(secretFieldName, appSecret);
        return requestBody;
    }

    private String buildCurrentPriceUrl(String stockCode) {
        return BASE_URL
                + "/uapi/domestic-stock/v1/quotations/inquire-price"
                + "?fid_cond_mrkt_div_code=J"
                + "&fid_input_iscd=" + stockCode;
    }

    private HttpURLConnection openConnection(String requestUrl, String method) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);
        return connection;
    }

    private void writeJsonBody(HttpURLConnection connection, JSONObject requestBody) throws Exception {
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] body = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(body);
        }
    }

    private JSONObject readJsonResponse(HttpURLConnection connection) throws Exception {
        return new JSONObject(readResponseBody(connection));
    }

    private String readResponseBody(HttpURLConnection connection) throws Exception {
        try (
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
    }

    private long parseLong(String value) {
        String digits = value == null ? "" : value.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return 0L;
        }

        return Long.parseLong(digits);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
