package com.rental.system.controller;

import com.rental.system.model.Promotion;
import com.rental.system.service.OtherManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@PreAuthorize("hasAnyRole('MANAGER', 'REGULAR')")
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

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable int id, @RequestBody Promotion promotion) {
        return otherManagementService.updatePromotion(id, promotion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable int id) {
        if (otherManagementService.deletePromotion(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
