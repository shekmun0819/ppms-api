package com.example.ppms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Host;

@Repository
public interface HostRepository extends JpaRepository<Host, Integer> {

	Optional<Host> findAllByUserId(Integer userId);
}
