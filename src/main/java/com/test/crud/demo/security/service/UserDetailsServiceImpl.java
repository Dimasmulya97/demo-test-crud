package com.test.crud.demo.security.service;

import com.test.crud.demo.constant.Constants;
import com.test.crud.demo.model.User;
import com.test.crud.demo.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException(Constants.Message.NOT_FOUND_DATA_MESSAGE);
        }
        return UserDetailsImpl.build(user);
    }
}