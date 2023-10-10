package com.example.ppms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	public User findByEmailAndPassword(String email, String password);

	User findById(Optional<Integer> supId);

	List<User> findAllByIdIn(List<Integer> ids);
}