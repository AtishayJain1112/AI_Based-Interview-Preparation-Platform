package com.springSecurity.basicOth.Security;

import com.springSecurity.basicOth.Entity.User;
import com.springSecurity.basicOth.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserRepo userRepo;
    public CustomUserDetailService(UserRepo user){
        this.userRepo = user;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User Not found"
                ));
        return new CustomUserDetails(user);
    }
}
