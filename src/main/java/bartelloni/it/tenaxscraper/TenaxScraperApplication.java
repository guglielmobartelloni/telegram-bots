package bartelloni.it.tenaxscraper;

import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import bartelloni.it.tenaxscraper.scraping.TenaxEventsFacade;
import bartelloni.it.tenaxscraper.telegram.bot.TelegramEventSender;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class TenaxScraperApplication {

    @Autowired
    private TelegramEventSender telegramEventSender;

    public static void main(String[] args) {
        SpringApplication.run(TenaxScraperApplication.class, args);
    }


    @Scheduled(cron = "0 0 8-22 * * *", zone = "Europe/Rome")
    public void scheduleFixedDelayTask() {
        final Collection<TenaxEvent> newEvents = new TenaxEventsFacade().getNewEvents();
        final List<Long> chatIds = getChatIds();
        newEvents.forEach(e -> telegramEventSender.sendEvent(e, chatIds));
    }

    private List<Long> getChatIds() {
        try {
            return FileUtils.readLines(new File("saved-users.txt"), StandardCharsets.UTF_8)
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
