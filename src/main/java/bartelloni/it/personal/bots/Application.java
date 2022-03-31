package bartelloni.it.personal.bots;

import bartelloni.it.personal.bots.gym.GymBooker;
import bartelloni.it.personal.bots.tenax.TenaxEvent;
import bartelloni.it.personal.bots.tenax.TenaxEventsFacade;
import bartelloni.it.personal.bots.utils.TelegramEventSender;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class Application {

    @Autowired
    private TelegramEventSender telegramEventSender;
    @Autowired
    private GymBooker gymBooker;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Scheduled(cron = "0 0 8-22 * * *", zone = "Europe/Rome")
    public void scheduleFixedDelayTask() {
        final Collection<TenaxEvent> newEvents = new TenaxEventsFacade().getNewEvents();
        final List<Long> chatIds = getChatIds();
        newEvents.forEach(e -> telegramEventSender.sendEvent(e, chatIds));
    }

    @Scheduled(cron = "0 0 11 * * *")
    public void book() throws IOException {
        final Date today = new Date();
        final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String appointmentTimestamp = sf.format(today) + " 18:30:00";
        gymBooker.book(Timestamp.valueOf(appointmentTimestamp));

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
