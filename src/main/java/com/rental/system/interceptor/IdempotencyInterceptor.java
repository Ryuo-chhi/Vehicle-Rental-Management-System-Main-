package com.rental.system.interceptor;

import com.rental.system.exception.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, Long> processedKeys = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public IdempotencyInterceptor() {
        // Clean up old keys every 1 hour to prevent memory leak
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            processedKeys.entrySet().removeIf(entry -> now - entry.getValue() > TimeUnit.HOURS.toMillis(24));
        }, 1, 1, TimeUnit.HOURS);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            String idempotencyKey = request.getHeader("Idempotency-Key");
            
            if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                Long previousTime = processedKeys.putIfAbsent(idempotencyKey, System.currentTimeMillis());
                if (previousTime != null) {
                    throw new DuplicateRequestException("This request has already been processed.");
                }
            }
        }
        return true;
    }
}
