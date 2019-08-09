package com.github.visola.githubnotifier.data;

import com.github.visola.githubnotifier.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
