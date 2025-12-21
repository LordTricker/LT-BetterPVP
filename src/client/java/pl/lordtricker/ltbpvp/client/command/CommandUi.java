package pl.lordtricker.ltbpvp.client.command;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.util.ColorUtils;

public final class CommandUi {
    private CommandUi() {}

    public static MutableText colored(String text) {
        MutableText out = Text.empty();
        out.append(ColorUtils.translateColorCodes(text));
        return out;
    }

    public static MutableText clickable(String text, ClickEvent.Action action, String command, String hoverText) {
        MutableText out = colored(text);
        Style style = Style.EMPTY.withClickEvent(new ClickEvent(action, command));
        if (hoverText != null && !hoverText.isEmpty()) {
            style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(hoverText)));
        }
        out.setStyle(style);
        return out;
    }
}
