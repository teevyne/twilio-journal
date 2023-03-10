package com.twilio.joirnal.dao;

import com.twilio.joirnal.data.EntryPoint;
import com.twilio.joirnal.data.JournalSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalSubscriptionRepository extends JpaRepository<JournalSubscription, Long> {

    List<JournalSubscription> findAllByJournal_IdAndMember_Email(Long journalId, String email);

}
