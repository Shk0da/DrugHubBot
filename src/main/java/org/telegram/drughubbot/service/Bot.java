package org.telegram.drughubbot.service;

import org.telegram.drughubbot.Config;
import org.telegram.drughubbot.command.*;
import org.telegram.drughubbot.database.DatabaseManager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Bot extends TelegramLongPollingBot {

    private static Map<Long, DatabaseManager.ModelUser> registerUsers = new ConcurrentHashMap<>();
    private static final Config appConfig = Config.getInstance();
    private static final DatabaseManager database = DatabaseManager.getInstance();

    private static Map<Long, Integer> registerUserStatus = new ConcurrentHashMap<>();
    private static final int STATUS_NOT_WAITING = 0;
    private static final int STATUS_WAITING_CONTACT = 1;
    private static final int STATUS_WAITING_LOCATION = 2;
    private static final int STATUS_BOLTALKA = 3;

    @Override
    public String getBotUsername() {
        return appConfig.getAppName();
    }

    @Override
    public String getBotToken() {
        return appConfig.getAppApiKey();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();

        long userId = user.getId();

        if (!registerUserStatus.containsKey(userId)) {
            registerUserStatus.put(userId, STATUS_NOT_WAITING);
        }

        if (!registerUsers.containsKey(userId)) {
            DatabaseManager.ModelUser newModelUser = database.getUser(userId);

            if (newModelUser == null) {
                newModelUser = new DatabaseManager.ModelUser();
                newModelUser.setUserId(userId);
                database.saveUser(newModelUser);
            }

            Chat chat = message.getChat();
            String userName = chat.getUserName();
            if (userName == null || userName.isEmpty()) {
                userName = user.getFirstName() + " " + user.getLastName();
            }

            newModelUser.setName(userName);

            registerUsers.put(userId, newModelUser);
        }

        DatabaseManager.ModelUser modelUser = registerUsers.get(userId);

        switch (registerUserStatus.get(userId)) {
            case STATUS_WAITING_CONTACT:
                handlerContact(message, modelUser);
                break;
            case STATUS_WAITING_LOCATION:
                handlerLocation(message, modelUser);
                break;
            case STATUS_BOLTALKA:
                handlerBoltalka(message, modelUser);
                break;
            default:
                handlerMessage(message, modelUser);
        }
    }

    private void handlerBoltalka(Message message, DatabaseManager.ModelUser modelUser) {

        if (message == null || !message.hasText()) return;

        String command = message.getText().toLowerCase();

        switch (command) {
            case "stop":
            case "стоп":
            case "остановись":
                registerUserStatus.put(modelUser.getUserId(), STATUS_NOT_WAITING);
                sendMsg(message, "Окей, че)");
                break;

            case "заебал":
                registerUserStatus.put(modelUser.getUserId(), STATUS_NOT_WAITING);
                sendMsg(message, "Fuck Off Нига!");
                break;

            default:
                Boltalka boltalka = new Boltalka();
                sendMsg(message, boltalka.answer());
        }
    }

    private void handlerLocation(Message message, DatabaseManager.ModelUser modelUser) {
        Location location = message.getLocation();
        User user = message.getFrom();

        long userId = user.getId();

        if (message.hasLocation() && location != null) {
            String city = Geo.getCityNameByCoordinate(location.getLatitude(), location.getLongitude());

            modelUser.setCity(city);
            modelUser.setLocation(location.getLatitude() + ", " + location.getLongitude());
            registerUsers.put(userId, modelUser);
            database.saveUser(modelUser);

            sendMsg(message, "Спасибо, твой город: " + city);
            registerUserStatus.put(userId, STATUS_NOT_WAITING);
            Command help = new Help(modelUser);
            sendKeyboard(message, help.getKeyboard(), Emoji.INFORMATION_SOURCE.toString());
        } else {
            sendMsg(message, "Я не смогу работать, если не буду знать твой город, используй 'location'");
        }
    }

    private void handlerContact(Message message, DatabaseManager.ModelUser modelUser) {

        User user = message.getFrom();

        long userId = user.getId();
        Contact contact = message.getContact();

        if (contact != null) {

            modelUser.setPhone(contact.getPhoneNumber());
            registerUsers.put(userId, modelUser);
            database.saveUser(modelUser);

            sendMsg(message, "Спасибо, я обновил твои контакты");
            registerUserStatus.put(userId, STATUS_NOT_WAITING);
        }
    }

    private void handlerMessage(Message message, DatabaseManager.ModelUser modelUser) {

        if (message == null || !message.hasText()) return;

        String command = message.getText().toLowerCase();
        Command commandClass;

        switch (command) {
            case "/start":
                commandClass = new Start();
                sendMsg(message, "Привет, " + modelUser.getName() + "!");
                sendMsg(message, commandClass.answer());
                sendMsg(message, commandClass.getMessage());
                registerUserStatus.put(modelUser.getUserId(), STATUS_WAITING_LOCATION);
                break;

            case "/help":
            case "помощь":
                commandClass = new Help(modelUser);
                sendMsg(message, commandClass.answer());
                break;

            case "/top":
            case "топ продавцов":
                commandClass = new Top();
                sendKeyboard(message, commandClass.getKeyboard(), Emoji.CHART.toString());
                break;

            case "/chat":
            case "чат":
                registerUserStatus.put(modelUser.getUserId(), STATUS_BOLTALKA);
                sendMsg(message, "Останови меня как надоест " + Emoji.CRYING_FACE.toString());
                break;

            case "/addme":
            case "зарегистрироваться как продавец":
                //commandClass = new Dealer();
                break;

            default:
                sendMsg(message, "Сорян, я не знаю такой команды");
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendKeyboard(Message message, ReplyKeyboard keyBoard, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyBoard);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
