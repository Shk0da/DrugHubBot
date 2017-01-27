package org.telegram.drughubbot.command;

import org.telegram.drughubbot.service.Emoji;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Map;

public class Top extends Command {

    public Top() {
        buttons.put("/order ОПИСАНИЕ", "оставить заявку");
        buttons.put("/message НИК", "связаться с продавцом");
        buttons.put("/like НИК", "оценить работу драгдиллера");
        buttons.put("/unlike НИК", "опозорить драгдиллера");
    }

    @Override
    public Map<String, String> getButtons() {
        return this.buttons;
    }

    @Override
    public String getMessage() {
        return Emoji.CHART.toString();
    }

    @Override
    public String answer() {
        return getMessage();
    }

    @Override
    public ReplyKeyboard getKeyboard() {
        return super.getKeyboard();
    }
}
