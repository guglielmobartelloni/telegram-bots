package bartelloni.it.personal.bots.tenax;

import bartelloni.it.personal.bots.tenax.TenaxEvent;
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
            String eventMessage = "Nuovo evento: " + event.getTitle() + "\n" +
                    "Data: " + event.getEventDate() + "\n" +
                    "Link: " + event.getLink();
            telegramBot.execute(new SendMessage(chatId, eventMessage));
        }
    }

}
