package bartelloni.it.tenaxscraper;

import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import bartelloni.it.tenaxscraper.scraping.TenaxEventsFacade;
import bartelloni.it.tenaxscraper.telegram.bot.TelegramEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collection;

@SpringBootApplication
@EnableScheduling
public class TenaxScraperApplication {

    @Autowired
    private TelegramEventSender telegramEventSender;

    public static void main(String[] args) {
        SpringApplication.run(TenaxScraperApplication.class, args);
    }


    @Scheduled(cron = "0 0 8-10 * * *", zone = "Europe/Rome")
    public void scheduleFixedDelayTask() {
        final Collection<TenaxEvent> newEvents = new TenaxEventsFacade().getNewEvents();
        newEvents.forEach(telegramEventSender::sendEvent);
    }
}
