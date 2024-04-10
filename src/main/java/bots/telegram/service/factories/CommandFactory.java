package bots.telegram.service.factories;

import org.telegram.telegrambots.meta.api.objects.Message;
import bots.telegram.service.SendMessageService;
import bots.telegram.service.commands.*;

public class CommandFactory {
    public static JewelryCoursesBotCommand defineCommand(Message message, SendMessageService sendMessageService) {
        return switch (message.getText()) {
            case "/start" -> new StartCommand(sendMessageService);
            case "У меня вопрос❓" -> new QuestionsCommand(sendMessageService);
            case "Иду на обучение\uD83D\uDCDA" -> new SignUpCommand(sendMessageService);
            default -> new WrongCommand(sendMessageService);
        };
    }
}
