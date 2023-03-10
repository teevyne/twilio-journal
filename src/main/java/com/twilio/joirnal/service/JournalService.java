package com.twilio.joirnal.service;

import com.twilio.joirnal.data.Journal;
import com.twilio.joirnal.data.JournalEntry;
import com.twilio.joirnal.data.Member;
import com.twilio.joirnal.data.JournalSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.joirnal.dao.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JournalService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JournalSubscriptionRepository journalSubscriptionRepository;

    @Autowired
    private TwilioService twilioService;

    public JournalEntry createJournalEntry(Long memberId, JournalEntry journalEntry) throws MessagingException {

        // Save the journal entry to the database
        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);

        // Get the member from the database using the email
//        JournalContributor contributor = journalContributorRepository.findByEmail(memberEmail);
        Member member = memberRepository.findById(memberId).orElse(null);

        // Add one point to the contributor's total points
        assert member != null;
        member.setTotalPoints(member.getTotalPoints() + 1);
        memberRepository.save(member);

        // Get all the subscribers to the journal, excluding the contributor
        List<JournalSubscription> subscribers = journalSubscriptionRepository
                .findAllByJournal_IdAndMember_Email(journalEntry.getJournal().getId(), member.getEmail());

        // Send an SMS and an email to each subscriber
        for (JournalSubscription subscriber : subscribers) {
            String message = String
                    .format("A new journal entry has been added to the %s journal: %s",
                            journalEntry.getJournal().getName(), journalEntry.getTitle());
            twilioService.sendSms(subscriber.getMember().getPhoneNumber(), message);
            twilioService.sendEmail(subscriber.getMember().getEmail(), "New Journal Entry", message);
        }

        return savedJournalEntry;
    }

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public List<JournalEntry> getJournalEntries(Long journalId) {
        return journalEntryRepository.findByJournal_Id(journalId);
    }

    public Optional<Journal> getJournalById(Long journalId) {
        return journalRepository.findById(journalId);
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public JournalSubscription createJournalSubscription(JournalSubscription journalSubscription) {
        return journalSubscriptionRepository.save(journalSubscription);
    }

    public void deleteJournalSubscription(Long id) {
        journalSubscriptionRepository.deleteById(id);
    }

    public void closeJournal(Long journalId) {
        Journal journal = journalRepository.findById(journalId).orElse(null);
        assert journal != null;
        journal.setClosed(true);
        journalRepository.save(journal);
    }

    // method to get member with the highest score
    public Member getMemberWithHighestScore(Long journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalStateException("Journal " + journalId + " not found"));

        if (!journal.isClosed()) {
            throw new IllegalStateException("Journal is not closed yet");
        }

        // group entries by member and sum up their scores
        Map<Member, Integer> scores = journalEntryRepository.findByJournal_Id(journalId)
                .stream()
                .collect(Collectors.groupingBy(JournalEntry::getMember,
                        Collectors.summingInt(JournalEntry::getScore)));

        // sort the members by score in descending order and return the first one
        return scores.entrySet()
                .stream().
                max(
                        Map.Entry.<Member, Integer>comparingByValue()
                )
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

