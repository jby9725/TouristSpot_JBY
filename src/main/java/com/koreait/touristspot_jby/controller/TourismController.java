package com.koreait.touristspot_jby.controller;

import com.koreait.touristspot_jby.service.TourismService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TourismController {

    private final TourismService tourismService;

    public TourismController(TourismService tourismService) {
        this.tourismService = tourismService;
    }

    @GetMapping("/")
    public String showMainPage() {
        // resources/templates/main.html 반환
        return "tourism/main";
    }

    @GetMapping("/tourism/search")
    @ResponseBody
    public String searchTouristSpots(@RequestParam String keyword) {
        System.err.println("Received keyword: " + keyword); // 로그 추가
        return tourismService.searchTouristSpots(keyword);
    }
}