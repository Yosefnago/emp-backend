package com.ms.sw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class YosefApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        String rawPassword = "1234";
        String hashed = "";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(rawPassword));
    }


}
