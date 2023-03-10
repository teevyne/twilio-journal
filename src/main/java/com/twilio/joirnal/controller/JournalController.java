package com.twilio.joirnal.controller;

import com.twilio.joirnal.data.Journal;
import com.twilio.joirnal.data.JournalEntry;
import com.twilio.joirnal.data.Member;
import com.twilio.joirnal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable("id") Long id) {
        Journal journal = journalService.getJournalById(id).get();
        return ResponseEntity.ok(journal);
    }

    @PostMapping("/")
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        Journal createdJournal = journalService.createJournal(journal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJournal);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Journal> updateJournal(@PathVariable("id") Long id, @RequestBody Journal journal) {
//        Journal updatedJournal = journalService.updateJournal(id, journal);
//        return ResponseEntity.ok(updatedJournal);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJournal(@PathVariable("id") Long id) {
//        journalService.deleteJournal(id);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/")
    public ResponseEntity<List<Journal>> getAllJournals() {
        List<Journal> journals = journalService.getAllJournals();
        return ResponseEntity.ok(journals);
    }

    // endpoint for adding a new entry to a journal
    @PostMapping("/{id}/entry")
    public ResponseEntity<JournalEntry> addEntry(@PathVariable("id") Long id, @RequestBody JournalEntry entry) throws MessagingException {
        JournalEntry addedEntry = journalService.createJournalEntry(id, entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedEntry);
    }

    // endpoint for getting all entries of a journal
    @GetMapping("/{id}/entries")
    public ResponseEntity<List<JournalEntry>> getAllEntries(@PathVariable("id") Long id) {
        List<JournalEntry> entries = journalService.getJournalEntries(id);
        return ResponseEntity.ok(entries);
    }

    // endpoint for getting the contributors of a journal
//    @GetMapping("/{id}/contributors")
//    public ResponseEntity<List<User>> getContributors(@PathVariable("id") Long id) {
//        List<User> contributors = journalService.getContributors(id);
//        return ResponseEntity.ok(contributors);
//    }

    // endpoint for sending email and SMS notifications to all members of a group except the contributor
//    @PostMapping("/{id}/notify/{contributorId}")
//    public ResponseEntity<Void> sendNotifications(@PathVariable("id") Long id, @PathVariable("contributorId") Long contributorId) {
//        journalService.sendNotifications(id, contributorId);
//        return ResponseEntity.ok().build();
//    }

    // endpoint for closing a journal
    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeJournal(@PathVariable("id") Long id) {
        journalService.closeJournal(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/winner")
    public ResponseEntity<Member> getMemberWithHighestScore(@PathVariable("id") Long journalId) {
        return ResponseEntity.ok(journalService.getMemberWithHighestScore(journalId));
    }

}

