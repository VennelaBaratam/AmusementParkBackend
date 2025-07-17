package com.amusement.amusement_park.repository;

import com.amusement.amusement_park.entity.UserMembership;
import com.amusement.amusement_park.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    Optional<UserMembership> findByUser(User user);
}