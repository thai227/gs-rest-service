package com.thai.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    List<User> findByOrgUuid(String s);

    @Query("Select u from User u where u.id = :id")
    User findByIdManually(@Param("id") Integer id);
}
