package com.kma.ap1.huyendtt.service;

import com.kma.ap1.huyendtt.model.User;

public interface UserService {

     User findUserByEmail(String email);

     void saveUser(User user);
}