package com.example.ppms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ppms.model.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

  List<Venue> findAllByCsmindId(Integer csmindId);
}
