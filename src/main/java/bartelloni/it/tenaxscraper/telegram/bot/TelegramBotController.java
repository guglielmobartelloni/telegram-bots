package bartelloni.it.tenaxscraper.telegram.bot;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@BotController
public class TelegramBotController implements TelegramMvcController {

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getToken() {
        return botToken;
    }

    @MessageRequest("/start")
    public String start(Chat chat, User user) throws IOException {
        saveChatId(chat.id());
        return "Benvenuto " + user.username();
    }

    private void saveChatId(Long id) throws IOException {
        File file = new File("saved-users.txt");
        FileUtils.writeStringToFile(
                file, id + "\n", StandardCharsets.UTF_8, true);
    }
}
