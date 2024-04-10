package bots.telegram.service.commands;

import bots.telegram.service.SendMessageService;
import org.telegram.telegrambots.meta.api.methods.invoices.CreateInvoiceLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

public class SignUpCommand implements JewelryCoursesBotCommand {
    final SendMessageService sendMessageService;
    final String joiningText = "Текст про оплату";

    public SignUpCommand(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processCommand() {
        CreateInvoiceLink invoiceLink = sendMessageService.preparePaymentInvoiceLink("Курс ювелирных изделий", joiningText, BigDecimal.valueOf(300));
        SendMessage message = sendMessageService.prepareMessageWithInvoiceLink(invoiceLink, joiningText, "Оплатить курс");
        sendMessageService.sendMessage(message);
    }
}
