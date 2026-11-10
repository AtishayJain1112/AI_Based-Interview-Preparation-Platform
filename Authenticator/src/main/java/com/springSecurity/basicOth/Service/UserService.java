package com.springSecurity.basicOth.Service;

import com.springSecurity.basicOth.Client.MailClient;
import com.springSecurity.basicOth.DTO.*;
import com.springSecurity.basicOth.Entity.Role;
import com.springSecurity.basicOth.Entity.User;
import com.springSecurity.basicOth.Repository.RoleRepo;
import com.springSecurity.basicOth.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.springSecurity.basicOth.Enums.MailType.EXIT;
import static com.springSecurity.basicOth.Enums.MailType.WELCOME;
import static java.rmi.server.LogStream.log;

@Slf4j
@Service
public class UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private RoleRepo roleRepo;

    private final MailClient mailClient;

    public UserService(UserRepo userRepo,RoleRepo roleRepo,PasswordEncoder passwordEncoder,MailClient mailClient){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailClient = mailClient;
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));


        return new UserResponseDTO(
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );
    }
    @Transactional
    public boolean updateUser(Integer id, UpdateUserDTO updatedUser) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(updatedUser.getUsername());


        userRepo.save(user);
        return true;
    }
    @Transactional
    public RegisterResponseDTO saveUser(RegisterUserDTO user) {

        Role userRole = roleRepo.findByRoleName("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("ROLE_USER not found"));

        List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        User create = new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                roles
        );

        User savedUser = userRepo.save(create);

        String emailResponse = mailClient.sendEmail(
                new MailDTO(
                        savedUser.getEmail(),
                        savedUser.getUsername(),
                        " ",
                        WELCOME
                )
        );

        log.info("Email response: {}", emailResponse);

        return new RegisterResponseDTO(
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User created successfully"
        );
    }

    public Boolean deleteUser(Integer id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + id));

        userRepo.delete(user);
        log.info(mailClient.sendEmail(new MailDTO(user.getEmail(),user.getUsername()," ",EXIT)));
        return true;
    }

}
