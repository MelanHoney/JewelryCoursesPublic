package bots.telegram.service;

import bots.telegram.config.JewelryCoursesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import bots.telegram.service.commands.JewelryCoursesBotCommand;
import bots.telegram.service.factories.CommandFactory;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JewelryCoursesBot extends TelegramLongPollingBot {
    final JewelryCoursesConfig config;
    final SendMessageServiceImpl sendMessageService;

    public JewelryCoursesBot(JewelryCoursesConfig config) {
        this.config = config;
        this.sendMessageService = new SendMessageServiceImpl(this);
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Информация о курсе"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            sendMessageService.setChatId(update.getMessage().getChatId());
            processRequest(update.getMessage());
        } else if (update.hasPreCheckoutQuery()) {
            PreCheckoutQuery query = update.getPreCheckoutQuery();
            trySendAnswerPreCheckoutQuery(query);
        }
    }

    private void processRequest(Message message) {
        if (message.hasSuccessfulPayment()) {
            sendChannelJoinLinkAndMessage();
        } else if (message.hasText()) {
            JewelryCoursesBotCommand botCommand = CommandFactory.defineCommand(message, sendMessageService);
            botCommand.processCommand();
        }
    }

    private void sendChannelJoinLinkAndMessage() {
        CreateChatInviteLink chatInviteLink = new CreateChatInviteLink("-1002083800217");
        chatInviteLink.setMemberLimit(1);
        try {
            ChatInviteLink inviteLink = execute(chatInviteLink);
            SendMessage message = sendMessageService.prepareMessage("Внимание, ссылка работает только один раз! \n" + inviteLink.getInviteLink());
            sendMessageService.sendMessage(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void trySendAnswerPreCheckoutQuery(PreCheckoutQuery query) {
        if (query.getInvoicePayload() != null) {
            try {
                execute(new AnswerPreCheckoutQuery(query.getId(), true));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
