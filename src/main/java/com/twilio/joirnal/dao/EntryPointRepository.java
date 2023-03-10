package com.twilio.joirnal.dao;

import com.twilio.joirnal.data.EntryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryPointRepository extends JpaRepository<EntryPoint, Long> {



}
