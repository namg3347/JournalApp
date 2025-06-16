package net.engineeringdigest.journalApp.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepoImpl;
import net.engineeringdigest.journalApp.services.EmailService;

@Component
public class SentimentSch {
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImpl userRepoImpl;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSentiment() {
        List<User> users =  userRepoImpl.getUserForSA();
        for(User user : users) {
            List<JournalEntry> entries = user.getJournalEntries();
            List<Sentiment> filteredEntries = entries.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7,ChronoUnit.DAYS)))
            .map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment : filteredEntries) {
                if(sentiment!=null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0)+1);
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount=0;
            for(Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()) {
                if(entry.getValue()>maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if(mostFrequentSentiment!=null) {
                emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", mostFrequentSentiment.toString());
            }
        }
    }
}
