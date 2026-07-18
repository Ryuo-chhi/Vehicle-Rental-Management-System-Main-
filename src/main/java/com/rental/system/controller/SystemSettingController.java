package com.rental.system.controller;

import com.rental.system.model.SystemSetting;
import com.rental.system.service.OtherManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
@PreAuthorize("hasRole('MANAGER')")
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

    @GetMapping("/{key}")
    public ResponseEntity<SystemSetting> getSettingByKey(@PathVariable String key) {
        return otherManagementService.getAllSettings().stream()
                .filter(setting -> setting.getSettingKey().equals(key))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> updateSetting(@RequestParam String key, @RequestParam String value) {
        otherManagementService.updateSetting(key, value);
        return ResponseEntity.ok().build();
    }
}
