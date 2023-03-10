package com.twilio.joirnal.dao;

import com.twilio.joirnal.data.Journal;
import com.twilio.joirnal.data.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findAllByJournal(Journal journal);

    List<JournalEntry> findByJournal_Id(Long journalId);
}
