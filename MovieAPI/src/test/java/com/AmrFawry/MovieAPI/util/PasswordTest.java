package com.AmrFawry.MovieAPI.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "easyeasy!"; // Your actual password
        String storedHash = "$2a$10$6Ah6GvrlAiy.hOR9Q8Jgae8rhovt40MRxiC8ck4L73hUFwcJg9CTC"; // Your DB hash

        boolean match = encoder.matches(rawPassword, storedHash);
        System.out.println("Manual password match: " + match);
    }
}
