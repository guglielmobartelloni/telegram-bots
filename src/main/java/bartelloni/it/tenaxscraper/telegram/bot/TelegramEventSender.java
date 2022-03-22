package bartelloni.it.tenaxscraper.telegram.bot;

import bartelloni.it.tenaxscraper.scraping.TenaxEvent;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramEventSender {

    private TelegramBot telegramBot;


    public TelegramEventSender(@Value("${bot.token}") String botToken) {
        this.telegramBot = new TelegramBot(botToken);
    }

    public void sendEvent(TenaxEvent event, List<Long> chatIdList) {
        for (Long chatId : chatIdList) {
            SendSticker sendSticker = new SendSticker(chatId, event.getImageLink());
//            telegramBot.execute(sendSticker);
            telegramBot.execute(new SendMessage(chatId, "Nuovo evento: " + event.getTitle()));
            telegramBot.execute(new SendMessage(chatId, "Data: " + event.getEventDate()));
            telegramBot.execute(new SendMessage(chatId, "Link: " + event.getLink()));
        }
    }

}
