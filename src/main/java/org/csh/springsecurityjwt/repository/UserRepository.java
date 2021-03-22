package org.csh.springsecurityjwt.repository;

import java.util.Optional;

import org.csh.springsecurityjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUserName(String username);
}
