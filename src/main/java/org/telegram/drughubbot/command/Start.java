package org.telegram.drughubbot.command;

import org.telegram.drughubbot.service.Emoji;

import java.util.HashMap;
import java.util.Map;

public class Start extends Command {

    @Override
    public String getMessage() {
        StringBuilder commandList = new StringBuilder();
        commandList.append("Пожалуйста отправь свое местоположение, я определю город..." + Emoji.METRO + "\n");

        return commandList.toString();
    }

    @Override
    public String answer() {

        StringBuilder commandList = new StringBuilder();
        commandList.append("Используйте команду /help для вывода доступных действий\n");

        return commandList.toString();
    }
}
