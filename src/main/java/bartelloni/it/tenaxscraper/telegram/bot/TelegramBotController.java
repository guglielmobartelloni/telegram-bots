package bartelloni.it.tenaxscraper.telegram.bot;

import bartelloni.it.tenaxscraper.scraping.Scraper;
import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@BotController
public class TelegramBotController implements TelegramMvcController {

    @Value("${bot.token}")
    private String botToken;

    @Autowired
    private TelegramEventSender telegramEventSender;
    private Scraper scraper=new Scraper();

    @Override
    public String getToken() {
        return botToken;
    }

    @MessageRequest("/start")
    public String start(Chat chat, User user) throws IOException {
        saveChatId(chat.id());
        return "Benvenuto " + user.username();
    }

    @MessageRequest("/eventi")
    public String getEventi(Chat chat, User user) throws IOException {
        final List<TenaxEvent> events = scraper.getEvents();
        for (TenaxEvent event : events) {
            telegramEventSender.sendEvent(event, List.of(chat.id()));
        }
        return null;
    }

    private void saveChatId(Long id) throws IOException {
        File file = new File("saved-users.txt");
        if(FileUtils.readLines(file,StandardCharsets.UTF_8).stream().anyMatch(e -> e.equals(id))){
            return;
        }
        FileUtils.writeStringToFile(
                file, id + "\n", StandardCharsets.UTF_8, true);
    }
}
