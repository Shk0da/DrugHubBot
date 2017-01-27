package org.telegram.drughubbot.command;

import org.telegram.drughubbot.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

public class Help extends Command {

    private DatabaseManager.ModelUser modelUser;

    public Help()
    {
        buttons.put("/chat", "попиздеть со мной =)");
        buttons.put("/top", "топ продавцов");
        buttons.put("/addme", "зарегистрироваться как продавец");
    }

    @Override
    public Map<String, String> getButtons() {

        Map<String, String> buttons = new HashMap<>();

        if (modelUser != null && modelUser.getIsDealer() == 1) {
            buttons.put("/orderlist", "посмотреть доступные заявки");
            buttons.put("/reset", "я передумал быть пордавцом");
            buttons.put("/info", "информация");
        } else {
            buttons = this.buttons;
        }

        return buttons;
    }

    public void setModelUser(DatabaseManager.ModelUser modelUser) {
        this.modelUser = modelUser;
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
