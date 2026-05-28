package com.rental.system.controller;

import com.rental.system.model.SystemSetting;
import com.rental.system.service.OtherManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SystemSettingController {
    private final OtherManagementService otherManagementService;

    @Autowired
    public SystemSettingController(OtherManagementService otherManagementService) {
        this.otherManagementService = otherManagementService;
    }

    @GetMapping
    public List<SystemSetting> getAllSettings() {
        return otherManagementService.getAllSettings();
    }

    @PostMapping
    public ResponseEntity<Void> updateSetting(@RequestParam String key, @RequestParam String value) {
        otherManagementService.updateSetting(key, value);
        return ResponseEntity.ok().build();
    }
}
