package uz.pdp.entity;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static uz.pdp.entity.BotServise.*;

public class BotController {
    static ExecutorService executorService = Executors.newFixedThreadPool(10);
    public void start() {

        telegramBot.setUpdatesListener(updates ->{
            for (Update update : updates) {
                executorService.execute(() ->{
                    try {
                        handleUpdate(update);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL ;
        });
    }

    private void handleUpdate(Update update) {
        if (update.message() != null){
            Message message = update.message();
            Long chatId = message.chat().id();
            if (message.text() != null){
                TgUser tgUser = BotServise.getOrCreatUser(chatId);
                if (message.text().equals("/start")){
                    BotServise.acceptStartAndSentMenu(tgUser);
                }
            }
        } else if (update.callbackQuery() != null) {
            handleCallBackQuery(update.callbackQuery());
        }
    }

    private void handleCallBackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.from().id();
        TgUser tgUser = getOrCreatUser(chatId);
        String data = callbackQuery.data();
        if (tgUser.getTgState().equals(TgState.SHOW_MENU)){
            acceptMenuAndShowPosts(tgUser,data);
        } else if (tgUser.getTgState().equals(TgState.SHOW_COMMENTS)) {
            BotServise.acceptPostAndSenComment(tgUser, data);
        } else if (tgUser.getTgState().equals(TgState.CONTINUE)) {
            BotServise.acceptCommentAndSenInformation(tgUser, data);
        }
    }
}
