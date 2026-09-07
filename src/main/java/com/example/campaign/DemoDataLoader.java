package com.example.campaign;

import com.example.campaign.model.User;
import com.example.campaign.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DemoDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.save(sample("山田太郎", "taro@example.com", "090-1111-2222", "東京", 28));
        userRepository.save(sample("佐藤花子", "hanako@example.com", "080-3333-4444", "大阪", 24));
        userRepository.save(sample("鈴木一郎", "ichiro@example.com", "070-5555-6666", "東京", 35));
    }

    private User sample(String name, String email, String phone, String area, int age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setArea(area);
        user.setAge(age);
        return user;
    }
}
