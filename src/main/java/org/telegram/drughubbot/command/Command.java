package org.telegram.drughubbot.command;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command {

    public Map<String, String> buttons = new HashMap<>();

    abstract public String answer();
    abstract public String getMessage();

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
