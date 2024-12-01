package com.koreait.touristspot_jby.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TourismService {

    @Value("${tourism.api.base-url}")
    private String BASE_URL;

    @Value("${tourism.api.decode-service-key}")
    private String SERVICE_KEY;

    private static final String MOBILE_OS = "ETC";
    private static final String MOBILE_APP = "TourismApp";
    private static final String DATA_TYPE = "json";

    public String searchTouristSpots(String keyword) {
        try {
            // URL 조립
            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + SERVICE_KEY);
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + MOBILE_OS);
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + MOBILE_APP);
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + DATA_TYPE);
            urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(keyword, "UTF-8"));

            System.out.println("Request URL: " + urlBuilder.toString());
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 필요한 헤더 추가
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");


            // 응답 읽기
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Message: " + conn.getResponseMessage());

            BufferedReader rd;
            if (responseCode >= 200 && responseCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            conn.disconnect();

            String responseBody = response.toString();
            System.out.println("Response Body: " + responseBody);

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);

            // 응답 내 "items" 추출 및 처리
            JsonNode items = root.path("response").path("body").path("items").path("item");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String title = item.path("title").asText();
                    System.out.println("관광지 이름: " + title);
                }
            } else {
                System.out.println("No items found in response.");
            }

            return responseBody;

        } catch (Exception e) {
            System.err.println("Error during API call: " + e.getMessage());
            throw new RuntimeException("API 호출 중 문제가 발생했습니다.", e);
        }
    }
}
