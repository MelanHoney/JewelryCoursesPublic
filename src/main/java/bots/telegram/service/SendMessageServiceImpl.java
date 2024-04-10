package bots.telegram.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.methods.invoices.CreateInvoiceLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SendMessageServiceImpl implements SendMessageService {
    private final JewelryCoursesBot telegramBot;
    @Setter
    private Long chatId;
    private String providerToken = "381764678:TEST:82509";

    protected SendMessageServiceImpl(JewelryCoursesBot bot) {
        this.telegramBot = bot;
    }

    public SendMessage prepareMessage(String text, List<MessageEntity> messageEntities) {
        SendMessage message = new SendMessage();
        message.setEntities(messageEntities);
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }

    public SendMessage prepareMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        return message;
    }

    public SendMessage prepareMarkupKeyboardMessage(ReplyKeyboardMarkup replyKeyboardMarkup, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setText(text);
        message.setReplyMarkup(replyKeyboardMarkup);
        message.setChatId(chatId);
        return message;
    }

    public CreateInvoiceLink preparePaymentInvoiceLink(String productName, String description, BigDecimal price) {
        CreateInvoiceLink link = new CreateInvoiceLink();
        link.setTitle(productName);
        link.setDescription(description);
        link.setPayload(String.valueOf(chatId));
        link.setProviderToken(providerToken);
        link.setCurrency("RUB");
        ArrayList<LabeledPrice> labeledPrices = new ArrayList<>();
        labeledPrices.add(new LabeledPrice("Цена", price.multiply(BigDecimal.valueOf(100L)).intValue()));
        link.setPrices(labeledPrices);
        return link;
    }

    public SendMessage prepareMessageWithInvoiceLink(CreateInvoiceLink invoiceLink, String text, String buttonText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(tryMakingReplyMarkup(invoiceLink, buttonText));
        return message;
    }

    private ReplyKeyboard tryMakingReplyMarkup(CreateInvoiceLink invoiceLink, String buttonText) {
        try {
            String response = telegramBot.execute(invoiceLink);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(makeInlineKeyboardRow(response, buttonText));
            markup.setKeyboard(keyboard);
            return markup;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<InlineKeyboardButton> makeInlineKeyboardRow(String paymentLink, String buttonText) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(buttonText);
        keyboardButton.setUrl(paymentLink);
        ArrayList<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(keyboardButton);
        return keyboardButtons;
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message: "+ message.getText() + ". Error: " + e.getMessage());
        }
    }
}
