package org.telegram.drughubbot.command;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Command {

    private volatile static ConcurrentHashMap<Class<?>, Object> mapInstances = new ConcurrentHashMap<>();

    protected Map<String, String> buttons = new HashMap<>();

    abstract public String answer();

    abstract public String getMessage();

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        T object = (T) mapInstances.get(clazz);
        if (object == null) {
            try {
                object = clazz.newInstance();
            } catch (IllegalAccessException | InstantiationException
                    | SecurityException ex) {
                throw new RuntimeException(ex);
            }
            T oldObject = (T) mapInstances.putIfAbsent(clazz, object);
            if (oldObject != null)
                return oldObject;
        }

        return object;
    }

    public static void forgetInstance(Class<?> clazz) {
        mapInstances.remove(clazz);
    }

    public Map<String, String> getButtons() {
        return buttons;
    }

    public ReplyKeyboard getKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> commands = new ArrayList<>();

        for (Map.Entry<String, String> button : getButtons().entrySet()) {
            String description = button.getValue();

            KeyboardRow commandRow = new KeyboardRow();
            commandRow.add(description);
            commands.add(commandRow);
        }

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        replyKeyboardMarkup.setKeyboard(commands);

        return replyKeyboardMarkup;
    }

}
