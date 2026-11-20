package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
public class DemoApplication {

    private final Instant startedAt = Instant.now();
    private final AtomicLong requests = new AtomicLong(0);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() {
        String hostname = "unknown";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
        }

        Duration uptime = Duration.between(startedAt, Instant.now());
        String formattedUptime = String.format("%dh %dm %ds",
                uptime.toHours(), uptime.toMinutesPart(), uptime.toSecondsPart());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", "ONLINE");
        data.put("hostname", hostname);
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("os", System.getProperty("os.name"));
        data.put("uptime", formattedUptime);
        data.put("requests", requests.incrementAndGet());
        data.put("serverTimeUtc", Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return data;
    }
}