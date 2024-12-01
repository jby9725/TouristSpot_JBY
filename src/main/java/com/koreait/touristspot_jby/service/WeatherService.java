package com.koreait.touristspot_jby.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeather(String city) {

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric") // 섭씨 단위
                .toUriString();

        // System.out.println("Weather API URL: " + url);

        try {
            // API 호출
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            // 오류 처리 및 로그 출력
            System.err.println("Weather API 호출 오류: " + e.getMessage());
            e.printStackTrace();

            // 빈 맵 반환
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Weather API 호출 중 오류가 발생했습니다.");
            return errorResponse;
        }
    }
}
