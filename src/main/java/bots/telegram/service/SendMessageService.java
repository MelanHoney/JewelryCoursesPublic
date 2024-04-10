package bots.telegram.service;

import org.telegram.telegrambots.meta.api.methods.invoices.CreateInvoiceLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;
import java.util.List;

public interface SendMessageService {
    void sendMessage(SendMessage sendMessage);
    SendMessage prepareMessage(String text);
    SendMessage prepareMessage(String text, List<MessageEntity> messageEntityList);
    CreateInvoiceLink preparePaymentInvoiceLink(String productName, String description, BigDecimal price);
    SendMessage prepareMessageWithInvoiceLink(CreateInvoiceLink invoiceLink, String text, String buttonText);
    SendMessage prepareMarkupKeyboardMessage(ReplyKeyboardMarkup keyboardMarkup, String text);
}
