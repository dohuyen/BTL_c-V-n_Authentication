package com.kma.ap1.huyendtt.repository;

import com.kma.ap1.huyendtt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}