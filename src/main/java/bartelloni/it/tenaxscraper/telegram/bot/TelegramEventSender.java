package bartelloni.it.tenaxscraper.telegram.bot;

import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TelegramEventSender {

    private TelegramBot telegramBot;


    public TelegramEventSender(@Value("${bot.token}") String botToken) {
        this.telegramBot = new TelegramBot(botToken);
    }

    public void sendEvent(TenaxEvent event) {
        List<Long> chadIdList = getChatIds();
        for (Long chatId : chadIdList) {
            SendSticker sendSticker = new SendSticker(chatId, event.getImageLink());
            telegramBot.execute(sendSticker);
            telegramBot.execute(new SendMessage(chatId, "Nuovo evento: " + event.getTitle()));
            telegramBot.execute(new SendMessage(chatId, "Data: " + event.getEventDate()));
            telegramBot.execute(new SendMessage(chatId, "Link: " + event.getLink()));
        }
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
