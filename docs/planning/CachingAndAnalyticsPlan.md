# 🚀 Future Upgrades: Caching & Analytics Plan

This document outlines the detailed plans, setup steps, and architecture details to implement Redis Caching and an Analytics Dashboard in the future.

---

## ⚡ 1. Caching & Performance Layer (Redis + Spring Cache)

Integrating Redis will optimize data retrieval speeds and reduce load on the MySQL database by caching static configurations and the vehicle inventory.

### 📦 Dependency Additions (`pom.xml`)
Add the Spring Cache and Redis starter dependencies:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### ⚙️ Configuration Class (`src/main/java/com/rental/system/config/RedisConfig.java`)
Create a custom configuration to handle JSON serialization (instead of raw binary serialization):
```java
package com.rental.system.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(10)) // Cache expires after 10 minutes
          .disableCachingNullValues()
          .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
          .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
```

### 🔄 Service Annotations (`src/main/java/com/rental/system/service/VehicleService.java`)
- **Reading Data (Cache Hit):** Annotate retrieval methods to save outputs in cache pools:
  ```java
  @Cacheable(value = "vehicles", key = "#root.methodName")
  public List<VehicleDTO> getAllVehicles() {
      // Database retrieval logic
  }
  ```
- **Modifying Data (Cache Eviction):** Clear stale cache pools when modifications are made to ensure cache consistency:
  ```java
  @CacheEvict(value = "vehicles", allEntries = true)
  public VehicleDTO createVehicle(VehicleDTO dto) {
      // Save and return logic
  }
  ```

---

## 📊 2. Analytics Dashboard & Reporting

Build backend aggregator queries and visual React chart modules to track application utilization and revenue.

### 📈 [Backend] Repository Mappings & Queries
Add aggregating queries using JPQL in your JPA Repositories:
- **Revenue trends (by month):**
  ```java
  @Query("SELECT FUNCTION('DATE_FORMAT', r.startDate, '%Y-%m') as month, SUM(p.price) as revenue " +
         "FROM Rent r JOIN r.payment p GROUP BY month ORDER BY month ASC")
  List<Object[]> getMonthlyRevenue();
  ```
- **Vehicle utilization:**
  ```java
  @Query("SELECT v.vehicleModel, COUNT(r) as rentCount " +
         "FROM Rent r JOIN r.vehicle v GROUP BY v.vehicleModel ORDER BY rentCount DESC")
  List<Object[]> getVehicleUtilization();
  ```

### 🌐 [Backend] Controller Endpoints (`src/main/java/com/rental/system/controller/AnalyticsController.java`)
Expose clean, mapped JSON models to the frontend:
- `GET /api/analytics/revenue` ➔ Returns `[{ "month": "2026-05", "revenue": 1450.00 }, ...]`
- `GET /api/analytics/utilization` ➔ Returns `[{ "model": "Civic", "rentCount": 12 }, ...]`
- `GET /api/analytics/payments` ➔ Returns `[{ "status": "PAID", "count": 28 }, ...]`

### 🎨 [Frontend] Dashboard Integration (`src/pages/Dashboard.jsx` in Frontend)
1.  **Install Recharts:**
    ```bash
    npm install recharts
    ```
2.  **Add Visualizations:**
    - **LineChart:** Plots the monthly revenue trends.
    - **BarChart:** Compares vehicle rental counts to show which models are rented most.
    - **PieChart:** Breaks down payment statuses (PAID vs. PENDING).
3.  **Excel/PDF Exports:**
    - Add an "Export Report" button on the Dashboard.
    - Triggers a backend download endpoint (`GET /api/analytics/export`) using Apache POI to serve a `.xlsx` spreadsheet.

---

## 🧪 Verification Plan

### Caching Verification
- Run a local Redis server.
- Verify Redis cache hits and misses by monitoring console logs or running `redis-cli monitor` in the terminal.
- Ensure updating/deleting a vehicle immediately clears the `vehicles` cache and forces a database reload on the next fetch.

### Analytics Verification
- Verify that charts on the Dashboard update in real-time when new bookings or returns are processed.
- Test export downloads to ensure files are generated with accurate data.
