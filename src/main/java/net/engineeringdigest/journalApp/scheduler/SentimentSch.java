package net.engineeringdigest.journalApp.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repository.UserRepoImpl;
import net.engineeringdigest.journalApp.services.EmailService;
import net.engineeringdigest.journalApp.services.SentimentAnalysisService;

@Component
public class SentimentSch {
    
    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImpl userRepoImpl;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSentiment() {
        List<User> users =  userRepoImpl.getUserForSA();
        for(User user : users) {
            List<JournalEntry> entries = user.getJournalEntries();
            List<String> filteredEntries = entries.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7,ChronoUnit.DAYS)))
            .map(x->x.getBody()).collect(Collectors.toList());
            String joined = String.join(" ", filteredEntries);

            String sentiment = sentimentAnalysisService.sentinentAnalysis(joined);
            emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", sentiment);
        }
    }
}
