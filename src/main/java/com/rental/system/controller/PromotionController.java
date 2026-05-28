package com.rental.system.controller;

import com.rental.system.model.Promotion;
import com.rental.system.service.OtherManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    private final OtherManagementService otherManagementService;

    @Autowired
    public PromotionController(OtherManagementService otherManagementService) {
        this.otherManagementService = otherManagementService;
    }

    @GetMapping
    public List<Promotion> getAllPromotions() {
        return otherManagementService.getAllPromotions();
    }

    @PostMapping
    public ResponseEntity<Promotion> addPromotion(@RequestBody Promotion promotion) {
        otherManagementService.savePromotion(promotion);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Promotion> getPromotionByCode(@PathVariable String code) {
        return otherManagementService.findPromotionByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
