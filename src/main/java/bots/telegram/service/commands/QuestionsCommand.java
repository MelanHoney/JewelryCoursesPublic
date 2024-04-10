package bots.telegram.service.commands;

import bots.telegram.service.SendMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class QuestionsCommand implements JewelryCoursesBotCommand {
    final SendMessageService sendMessageService;
    final String QAMessage = "Тут будут ответы на вопросы";
    public QuestionsCommand(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processCommand() {
        SendMessage message = sendMessageService.prepareMessage(QAMessage);
        sendMessageService.sendMessage(message);
    }
}
