package uz.pdp.entity;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

public class BotServise {
    public static TelegramBot telegramBot = new TelegramBot("7449264666:AAFqrSxL0Ustlx1yQXmTMpk7Z_p1sCigRxk");

    public static TgUser getOrCreatUser(Long chatId) {
        for (TgUser tgUser : DB.TG_USERS) {
            if (tgUser.getId().equals(chatId)) {
                return tgUser;
            }
        }
        TgUser tgUser = new TgUser();
        tgUser.setId(chatId);
        DB.TG_USERS.add(tgUser);
        return tgUser;
    }

    public static void acceptStartAndSentMenu(TgUser tgUser) {
        SendMessage sendMessage = new SendMessage(
                tgUser.getId(),
                """
                        Assalomu aleykum hurmatli foydalanuvchi
                        Menuni tanlang.
                        """
        );

SendMessage sendMessage1=new SendMessage(tgUser.getId(),"Userlardan birini tanlang:");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (User user : DB.USERS) {
            inlineKeyboardMarkup.addRow(
                    new InlineKeyboardButton(user.getName()).callbackData("12"),
                    new InlineKeyboardButton("Post").callbackData(user.getId() + "")
            );
        }
        sendMessage1.replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
        telegramBot.execute(sendMessage1);
        tgUser.setTgState(TgState.SHOW_MENU);
    }


    public static void acceptMenuAndShowPosts(TgUser tgUser, String data) {

        for (User user : DB.USERS) {
            if (user.getId().toString().equals(data)) {
                tgUser.setSelectedUser(user);
            }
        }
        User selectedUser = tgUser.getSelectedUser();
        if (selectedUser != null){
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            if (selectedUser.getId().toString().equals(data)){
                for (Post post : DB.POSTS) {
                    if (post.getUserId().equals(selectedUser.getId())){
                        inlineKeyboardMarkup.addRow(
                                new InlineKeyboardButton(post.getTitle()).callbackData("name_"),
                                new InlineKeyboardButton("Comments").callbackData(post.getId() +"")
                        );
                    }
                }
            }
            SendMessage sendMessage = new SendMessage(tgUser.getId(),"Commentni tanlang:" + selectedUser.getName());
            sendMessage.replyMarkup(inlineKeyboardMarkup);
            telegramBot.execute(sendMessage);
            tgUser.setTgState(TgState.SHOW_COMMENTS);
        }
    }

    public static void acceptPostAndSenComment(TgUser tgUser, String data) {
        for (Post post : DB.POSTS) {
        if (post.getId().toString().equals(data)){
            tgUser.setSelectedPost(post);
        }
        }
        Post selectedPost = tgUser.getSelectedPost();
        if (selectedPost != null){
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            for (TgComment comment : DB.COMMENTS) {
                if (comment.getPostId().equals(selectedPost.getId())){
                    inlineKeyboardMarkup.addRow(
                            new InlineKeyboardButton(comment.getName()).callbackData("comments")
                    );
                }
            }
            SendMessage sendMessage = new SendMessage(tgUser.getId(),"Commentni tanlang:"+ tgUser.getSelectedPost().getTitle());
            sendMessage.replyMarkup(inlineKeyboardMarkup);
            telegramBot.execute(sendMessage);
            tgUser.setTgState(TgState.CONTINUE);
        }
    }

    public static void acceptCommentAndSenInformation(TgUser tgUser, String data) {
        SendMessage sendMessage =  new SendMessage(tgUser.getId(),
                "Bu comment " + tgUser.getSelectedUser().getName() + "ning \n" +
                        tgUser.getSelectedPost().getTitle() + " postiga tegishli."
                );
        telegramBot.execute(sendMessage);
        tgUser.setTgState(TgState.END);
    }
}
