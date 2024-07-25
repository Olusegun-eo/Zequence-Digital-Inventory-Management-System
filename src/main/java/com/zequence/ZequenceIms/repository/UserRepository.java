package com.zequence.ZequenceIms.repository;


import com.zequence.ZequenceIms.entity.User;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthenticationDetails(UserAuthenticationDetails userAuthenticationDetails);
}