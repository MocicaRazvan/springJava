package com.moc.wellness.config;

import com.moc.wellness.enums.Role;
import com.moc.wellness.model.*;
import com.moc.wellness.model.user.JwtToken;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.*;
import com.moc.wellness.utils.jwt.JwtUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class InitDb {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        JwtUtils jwtUtils,
                                        JwtTokenRepository jwtTokenRepository,
                                        PostRepository postRepository,
                                        ExerciseRepository exerciseRepository,
                                        TrainingRepository trainingRepository,
                                        CommentRepository commentRepository,
                                        OrderRepository orderRepository,
                                        PasswordEncoder passwordEncoder

    ) {


        return args -> {
            if (!userRepository.existsByEmail("razvanmocica@gmail.com")) {
                var userAdmin = UserCustom.builder()
                        .email("razvanmocica@gmail.com")
                        .firstName("Razvan")
                        .lastName("Mocica")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.ADMIN)
                        .build();
                var savedAdmin = userRepository.save(userAdmin);
                JwtToken jwtTokenAdmin = JwtToken.builder()
                        .user(userAdmin)
                        .token(jwtUtils.generateToken(userAdmin))
                        .revoked(false)
                        .build();
                jwtTokenRepository.save(jwtTokenAdmin);
                System.out.println("Admin Token  " + jwtTokenAdmin.getToken());
                var userTrainer = UserCustom.builder()
                        .email("marcel@gmail.com")
                        .firstName("Marcel")
                        .lastName("Popescu")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.TRAINER)
                        .build();
                var savedTrainer = userRepository.save(userTrainer);
                JwtToken jwtTokenTrainer = JwtToken.builder()
                        .user(userTrainer)
                        .token(jwtUtils.generateToken(userTrainer))
                        .revoked(false)
                        .build();
                jwtTokenRepository.save(jwtTokenTrainer);
                System.out.println("Trainer Token  " + jwtTokenTrainer.getToken());
                var userUser = UserCustom.builder()
                        .email("user@gmail.com")
                        .firstName("User")
                        .lastName("user")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.USER)
                        .build();
                var savedUser = userRepository.save(userUser);
                JwtToken jwtTokenUser = JwtToken.builder()
                        .user(savedUser)
                        .token(jwtUtils.generateToken(savedUser))
                        .revoked(false)
                        .build();
                jwtTokenRepository.save(jwtTokenUser);
                System.out.println("User Token  " + jwtTokenUser.getToken());


                Set<UserCustom> likes = new HashSet<>();
                likes.add(savedAdmin);


                Post post = Post.builder()
                        .tags(List.of("MMM"))
                        .title("test")
                        .body("Test post")
                        .userDislikes(new HashSet<>())
                        .userLikes(likes)
                        .user(userTrainer)
                        .approved(true)
                        .build();
                var savedPost = postRepository.save(post);

                Exercise exercise = Exercise.builder()
                        .muscleGroups(List.of("bicep"))
                        .title("test")
                        .body("test")
                        .userDislikes(likes)
                        .userLikes(new HashSet<>())
                        .user(userTrainer)
                        .approved(true)
                        .build();
                Exercise exercise2 = Exercise.builder()
                        .muscleGroups(List.of("bicep"))
                        .title("test2")
                        .body("test2")
                        .userDislikes(new HashSet<>())
                        .userLikes(new HashSet<>())
                        .user(userTrainer)
                        .approved(false)
                        .build();

                var savedExercise = exerciseRepository.save(exercise);
                exerciseRepository.save(exercise2);
                Training training = Training.builder()
                        .user(userTrainer)
                        .title("test")
                        .body("test")
                        .userDislikes(new HashSet<>())
                        .userLikes(Set.of(savedAdmin))
                        .exercises(Set.of(savedExercise))
                        .price(10)
                        .approved(true)
                        .build();
                var savedTraining = trainingRepository.save(training);

                Training training2 = Training.builder()
                        .user(userTrainer)
                        .title("test2")
                        .body("test2")
                        .userDislikes(new HashSet<>())
                        .userDislikes(Set.of(savedUser))
                        .exercises(Set.of(savedExercise))
                        .price(11)
                        .approved(false)
                        .build();

                var savedTraining2 = trainingRepository.save(training2);


                var comment = Comment.builder()
                        .title("test")
                        .body("test")
                        .post(savedPost)
                        .user(savedTrainer)
                        .build();

                commentRepository.save(comment);


                Order order = Order.builder()
                        .payed(false)
                        .user(savedUser)
                        .trainings(Set.of(savedTraining))
                        .shippingAddress("address")
                        .build();

                var savedOrder = orderRepository.save(order);

            } else {
                JwtToken jwtTokenAdmin = jwtTokenRepository.findAllValidTokensByUser(1L).get(0);
                System.out.println("Admin Token  " + jwtTokenAdmin.getToken());

                JwtToken jwtTokenTrainer = jwtTokenRepository.findAllValidTokensByUser(2L).get(0);
                System.out.println("Trainer Token  " + jwtTokenTrainer.getToken());
                JwtToken jwtTokenUser = jwtTokenRepository.findAllValidTokensByUser(3L).get(0);
                System.out.println("User Token   " + jwtTokenUser.getToken());
            }

        };
    }
}
