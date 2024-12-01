package com.koreait.touristspot_jby.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
public class MapController {

    // 시/도 데이터를 저장
    private List<String> cities;

    public MapController() {
        // 시/도 데이터를 리스트에 추가
        cities = Arrays.asList(
                "서울",
                "경기도",
                "부산",
                "경상남도",
                "인천",
                "경상북도",
                "대구",
                "전라남도",
                "전북",
                "충청남도",
                "충청북도",
                "강원도",
                "광주",
                "대전",
                "울산",
                "제주도",
                "세종"
        );
    }

    // 시 리스트를 반환
    @GetMapping("/cities")
    @ResponseBody
    public List<String> getCities() {
        return cities;
    }
}
