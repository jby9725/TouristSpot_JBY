package com.koreait.touristspot_jby.controller;

import com.koreait.touristspot_jby.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // 날씨 데이터 조회 API (JSON 반환)
    @GetMapping("/weather")
    @ResponseBody
    public Map<String, Object> getWeather(@RequestParam String city) {
        Map<String, Object> weatherData = weatherService.getWeather(city);

        if (weatherData.containsKey("error")) {
            System.err.println("날씨 API 오류: " + weatherData.get("error"));
        }

        return weatherData;
    }
}
