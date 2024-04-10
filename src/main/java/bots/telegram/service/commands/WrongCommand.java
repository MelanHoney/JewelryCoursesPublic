package bots.telegram.service.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import bots.telegram.service.SendMessageService;

public class WrongCommand implements JewelryCoursesBotCommand {
    final SendMessageService sendMessageService;

    public WrongCommand(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processCommand() {
        SendMessage sendMessage = sendMessageService.prepareMessage("я не знаю такой команды");
        sendMessageService.sendMessage(sendMessage);
    }
}
