package springBootPickers.realData;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 한국투자증권 REST API 호출 서비스
 * - approval_key / access_token 발급
 */
@Service
public class KISApiService {

	private static final Logger log = LoggerFactory.getLogger(KISApiService.class);
    
    private static final String BASE_URL = "https://openapi.koreainvestment.com:9443";
    
    /**
     * WebSocket 접속키(approval_key) 발급
     */
    public String getApprovalKey(String appKey, String appSecret) {
        try {
            URL url = new URL(BASE_URL + "/oauth2/Approval");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // 요청 기본 설정
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            
            // 요청 바디 구성
            JSONObject body = new JSONObject();
            body.put("grant_type", "client_credentials");
            body.put("appkey", appKey);
            body.put("secretkey", appSecret);
            
            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 응답 처리
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                
                // approval_key 파싱
                JSONObject jsonResponse = new JSONObject(response.toString());
                String approvalKey = jsonResponse.getString("approval_key");
                
                log.info("접속키 발급 성공");
                return approvalKey;
            } else {
                log.error("접속키 발급 실패 (HTTP {})", responseCode);
                return null;
            }
            
        } catch (Exception e) {
            log.error("접속키 발급 중 예외 발생", e);
            return null;
        }
    }
    
    /**
     * REST API 액세스 토큰(access_token) 발급
     */
    public String getAccessToken(String appKey, String appSecret) {
        try {
            URL url = new URL(BASE_URL + "/oauth2/tokenP");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            
            JSONObject body = new JSONObject();
            body.put("grant_type", "client_credentials");
            body.put("appkey", appKey);
            body.put("appsecret", appSecret);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                
                JSONObject jsonResponse = new JSONObject(response.toString());
                String accessToken = jsonResponse.getString("access_token");
                
                log.info("액세스 토큰 발급 성공");
                return accessToken;
            } else {
                log.error("액세스 토큰 발급 실패 (HTTP {})", responseCode);
                return null;
            }
            
        } catch (Exception e) {
            log.error("액세스 토큰 발급 중 예외 발생", e);
            return null;
        }
    }
}
