package bartelloni.it.tenaxscraper.telegram.bot;

import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelegramEventSender {

    private TelegramBot telegramBot;


    public TelegramEventSender() {
        this.telegramBot = new TelegramBot("5164483626:AAFSr_aLEkjkVq5pbxNGpROCAqQOYeJlQc0");
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
