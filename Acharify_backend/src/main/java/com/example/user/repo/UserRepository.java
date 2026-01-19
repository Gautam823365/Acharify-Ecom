


package com.example.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    // Use JPQL with COUNT to avoid Oracle SQL errors for exists
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    Boolean existsByUsername(@Param("username") String username);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    Boolean existsByEmail(@Param("email") String email);
    
    // Your native query to fetch limited user by username (optional)
    @Query(value = "SELECT * FROM hr.users u WHERE u.username = :username AND ROWNUM <= 1", nativeQuery = true)
    Optional<User> findByUsernameLimited(@Param("username") String username);

}
