package org.telegram.drughubbot.command;

import org.telegram.drughubbot.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

public class Help extends Command {

    public Map<String, String> buttons = new HashMap<>();

    public Help() {
        buttons.put("/chat", "попиздеть со мной =)");
    }

    public Help(DatabaseManager.ModelUser modelUser) {

        this();

        if (modelUser.getIsDealer() == 1) {
            buttons.put("/orderlist", "посмотреть доступные заявки");
            buttons.put("/info", "информация");
        } else {
            buttons.put("/top", "топ продавцов");
            buttons.put("/addme", "зарегистрироваться как продавец");
        }
    }

    @Override
    public Map<String, String> getButtons() {
        return buttons;
    }

    @Override
    public String answer() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        StringBuilder commandList = new StringBuilder();

        for (Map.Entry<String, String> button : buttons.entrySet()) {
            String action = button.getKey();
            String description = button.getValue();

            commandList.append(action + " - " + description + "\n");
        }

        return commandList.toString();
    }

}
