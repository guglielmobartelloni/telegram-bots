package bartelloni.it.tenaxscraper.telegram.bot;

import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

class TelegramEventSenderTest {

    @Test
    void sendEvent() {
        final TenaxEvent event = TenaxEvent.builder()
                .imageLink("https://tenax.org/wp-content/uploads/2022/03/30eLode-Square02.jpg")
                .link("https://tenax.org/event/30-e-lode/")
                .title("30 e lode")
                .eventDate("24 Marzo 2022")
                .build();
        new TelegramEventSender("5164483626:AAFSr_aLEkjkVq5pbxNGpROCAqQOYeJlQc0").sendEvent(event);
    }
}