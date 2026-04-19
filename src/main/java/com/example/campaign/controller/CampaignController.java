package com.example.campaign.controller;

import com.example.campaign.model.User;
import com.example.campaign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.nio.charset.StandardCharsets;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CampaignController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody @jakarta.validation.Valid User user) {
        userRepository.save(user);
        return "Saved";
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<User> users = userRepository.findAll();

        Map<String, Integer> areaMap = new HashMap<>();

        for (User u : users) {
            areaMap.put(u.getArea(), areaMap.getOrDefault(u.getArea(), 0) + 1);
        }

        String topArea = "N/A";
        int max = 0;

        for (String area : areaMap.keySet()) {
            if (areaMap.get(area) > max) {
                max = areaMap.get(area);
                topArea = area;
            }
        }

        List<User> latest = users.stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .limit(10)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("total", users.size());
        result.put("topArea", topArea);
        result.put("list", latest);

        return result;
    }

    @PostMapping("/draw")
    public Map<String, Object> draw() {
        List<User> users = userRepository.findAll();
        Collections.shuffle(users);

        List<User> winners = users.stream()
                .limit(Math.min(10, users.size()))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("count", winners.size());
        result.put("winners", winners);

        return result;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return "User not found";
        }

        userRepository.deleteById(id);
        return "Deleted";
    }

    @GetMapping("/users/filter")
    public List<User> filterByArea(@RequestParam String area) {
        return userRepository.findByArea(area);
    }

    @GetMapping("/users/export")
    public ResponseEntity<byte[]> exportCsv() {
        List<User> users = userRepository.findAll();

        StringBuilder sb = new StringBuilder();

        // UTF-8 BOM để Excel mở tiếng Nhật không bị lỗi font
        sb.append("\uFEFF");

        // header
        sb.append("id,name,email,phone,area,age\n");

        for (User u : users) {
            sb.append(csvEscape(String.valueOf(u.getId()))).append(",")
                    .append(csvEscape(u.getName())).append(",")
                    .append(csvEscape(u.getEmail())).append(",")
                    .append(csvEscape(u.getPhone())).append(",")
                    .append(csvEscape(u.getArea())).append(",")
                    .append(csvEscape(String.valueOf(u.getAge()))).append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String csvEscape(String value) {
        if (value == null) {
            return "\"\"";
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}