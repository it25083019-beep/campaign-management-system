package com.example.campaign.controller;
import com.example.campaign.service.MailService;
import com.example.campaign.model.DrawHistory;
import com.example.campaign.model.User;
import com.example.campaign.repository.DrawHistoryRepository;
import com.example.campaign.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CampaignController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DrawHistoryRepository drawHistoryRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid User user) {
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

        for (User u : users) {
            u.setWinner(false);
            u.setMailSent(false);
        }

        for (User w : winners) {
            w.setWinner(true);
        }

        userRepository.saveAll(users);

        DrawHistory history = new DrawHistory();
        history.setDrawTime(LocalDateTime.now());
        history.setTotalUsers(users.size());
        history.setWinnersCount(winners.size());
        drawHistoryRepository.save(history);

        Map<String, Object> result = new HashMap<>();
        result.put("count", winners.size());
        result.put("winners", winners);

        return result;
    }

    @GetMapping("/winners")
    public List<User> getWinners() {
        return userRepository.findAll()
                .stream()
                .filter(User::isWinner)
                .toList();
    }

    @PostMapping("/send-mails")
    public Map<String, Object> sendMails() {
        List<User> winnersToSend = userRepository.findAll()
                .stream()
                .filter(User::isWinner)
                .filter(u -> !u.isMailSent())
                .toList();

        int success = 0;
        int fail = 0;
        List<String> failedEmails = new ArrayList<>();

        for (User u : winnersToSend) {
            try {
                System.out.println("START sending to: " + u.getEmail());
                mailService.sendWinnerMail(u.getEmail(), u.getName());
                System.out.println("SUCCESS sending to: " + u.getEmail());

                u.setMailSent(true);
                success++;
            } catch (Exception e) {
                fail++;
                failedEmails.add(u.getEmail());
                System.out.println("FAIL sending to: " + u.getEmail());
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        userRepository.saveAll(winnersToSend);

        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("fail", fail);
        result.put("total", winnersToSend.size());
        result.put("failedEmails", failedEmails);
        result.put("message", "メール送信処理が完了しました");

        return result;
    }

    @GetMapping("/draw-history")
    public List<DrawHistory> getDrawHistory() {
        return drawHistoryRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getDrawTime().compareTo(a.getDrawTime()))
                .toList();
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
        sb.append("\uFEFF");
        sb.append("id,name,email,phone,area,age,isWinner,mailSent\n");

        for (User u : users) {
            sb.append(csvEscape(String.valueOf(u.getId()))).append(",")
                    .append(csvEscape(u.getName())).append(",")
                    .append(csvEscape(u.getEmail())).append(",")
                    .append(csvEscape(u.getPhone())).append(",")
                    .append(csvEscape(u.getArea())).append(",")
                    .append(csvEscape(String.valueOf(u.getAge()))).append(",")
                    .append(csvEscape(String.valueOf(u.isWinner()))).append(",")
                    .append(csvEscape(String.valueOf(u.isMailSent()))).append("\n");
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