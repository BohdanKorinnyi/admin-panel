package com.omnia.admin.service.impl;

import com.omnia.admin.dao.UserDao;
import com.omnia.admin.model.User;
import com.omnia.admin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public Optional<User> getUserByName(String username) {
        try {
            return Optional.ofNullable(userDao.getUserByName(username));
        } catch (EmptyResultDataAccessException e) {
            log.info("User has not been found, username=" + username);
        } catch (Exception e) {
            log.info("User has not been found, username=" + username, e);
        }
        return Optional.empty();
    }
}
