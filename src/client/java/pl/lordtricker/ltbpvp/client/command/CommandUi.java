package pl.lordtricker.ltbpvp.client.command;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import java.net.URI;
import java.nio.file.Path;
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
        ClickEvent clickEvent = switch (action) {
            case RUN_COMMAND -> new ClickEvent.RunCommand(command);
            case SUGGEST_COMMAND -> new ClickEvent.SuggestCommand(command);
            case OPEN_URL -> {
                try {
                    yield new ClickEvent.OpenUrl(URI.create(command));
                } catch (IllegalArgumentException ex) {
                    yield null;
                }
            }
            case OPEN_FILE -> new ClickEvent.OpenFile(Path.of(command));
            case CHANGE_PAGE -> {
                try {
                    yield new ClickEvent.ChangePage(Integer.parseInt(command));
                } catch (NumberFormatException ex) {
                    yield null;
                }
            }
            case COPY_TO_CLIPBOARD -> new ClickEvent.CopyToClipboard(command);
            default -> null;
        };
        Style style = clickEvent == null ? Style.EMPTY : Style.EMPTY.withClickEvent(clickEvent);
        if (hoverText != null && !hoverText.isEmpty()) {
            style = style.withHoverEvent(new HoverEvent.ShowText(Text.literal(hoverText)));
        }
        out.setStyle(style);
        return out;
    }
}
