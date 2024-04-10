package bots.telegram.service.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import bots.telegram.service.SendMessageService;

import java.util.ArrayList;
import java.util.List;

public class StartCommand implements JewelryCoursesBotCommand {
    final SendMessageService sendMessageService;
    final String helloMessage = """
            привет, будущий ювелир!\s

            меня зовут Настя, и я создала курс по украшениям, где собрала весь свой 4х-летний опыт и знания, чтобы ты научился создавать разные украшения и зарабатывать на этом!

            если есть вопросы —> нажимай кнопку «у меня вопрос», либо сразу переходи к действую по кнопке «иду на обучение» и приступай к изучению нового дела \uD83E\uDE75""";

    public StartCommand(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processCommand() {
        ReplyKeyboardMarkup keyboardMarkup = buildKeyboard();
        SendMessage message = sendMessageService.prepareMarkupKeyboardMessage(keyboardMarkup, helloMessage);
        sendMessageService.sendMessage(message);
    }

    private ReplyKeyboardMarkup buildKeyboard() {
        List<KeyboardButton> keyboardButtons = buildKeyboardButtons();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(new KeyboardRow(keyboardButtons));
        return buildReplyKeyboardMarkup(keyboardRows);
    }

    private List<KeyboardButton> buildKeyboardButtons() {
        List<KeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(new KeyboardButton("У меня вопрос❓"));
        keyboardButtons.add(new KeyboardButton("Иду на обучение\uD83D\uDCDA"));
        return keyboardButtons;
    }

    private ReplyKeyboardMarkup buildReplyKeyboardMarkup(List<KeyboardRow> keyboardRows) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setIsPersistent(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
