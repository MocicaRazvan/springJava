package com.moc.wellness;

import com.moc.wellness.model.*;
import com.moc.wellness.model.user.JwtToken;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.*;
import com.moc.wellness.utils.jwt.JwtUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication

public class WellnessApplication {

    public static void main(String[] args) {
        SpringApplication.run(WellnessApplication.class, args);
    }


}




