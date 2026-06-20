package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.logic.CooldownTimerLogic;

public class CooldownTimerEditorScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget defaultColorField;
    private TextFieldWidget shieldColorField;
    private String errorMessage = "";

    private static final int W = 150;
    private static final int H = 20;
    private static final int SPACE = 30;

    public CooldownTimerEditorScreen(Screen parent) {
        super(Text.literal("Edit - Cooldown Timer"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = (this.height - 4 * SPACE) / 2;

        defaultColorField = new TextFieldWidget(
                this.textRenderer,
                cx - W / 2,
                y,
                W,
                H,
                Text.literal("Default color")
        );
        defaultColorField.setMaxLength(7);
        defaultColorField.setText(CooldownTimerLogic.formatHexColor(CoreSettings.cooldownTimerColor));
        addDrawableChild(defaultColorField);

        y += SPACE;
        shieldColorField = new TextFieldWidget(
                this.textRenderer,
                cx - W / 2,
                y,
                W,
                H,
                Text.literal("Shield color")
        );
        shieldColorField.setMaxLength(7);
        shieldColorField.setText(CooldownTimerLogic.formatHexColor(CoreSettings.shieldCooldownTimerColor));
        addDrawableChild(shieldColorField);

        y += SPACE + 5;
        ButtonWidget reset = ButtonWidget.builder(Text.of("Reset"), b -> {
            CoreSettings.cooldownTimerColor = CooldownTimerLogic.DEFAULT_COLOR;
            CoreSettings.shieldCooldownTimerColor = CooldownTimerLogic.DEFAULT_SHIELD_COLOR;
            defaultColorField.setText(CooldownTimerLogic.formatHexColor(CoreSettings.cooldownTimerColor));
            shieldColorField.setText(CooldownTimerLogic.formatHexColor(CoreSettings.shieldCooldownTimerColor));
            errorMessage = "";
        }).dimensions(cx - W / 2, y, W, H).build();
        addDrawableChild(reset);

        ButtonWidget save = ButtonWidget.builder(Text.of("Save"), b -> {
            int defaultColor = CooldownTimerLogic.parseHexColor(defaultColorField.getText(), -1);
            int shieldColor = CooldownTimerLogic.parseHexColor(shieldColorField.getText(), -1);
            if (defaultColor == -1 || shieldColor == -1) {
                errorMessage = "Use HEX format, e.g. #FF0000";
                return;
            }
            CoreSettings.cooldownTimerColor = defaultColor;
            CoreSettings.shieldCooldownTimerColor = shieldColor;
            this.client.setScreen(parent);
        }).dimensions(cx - 50, this.height - 30, 100, H).build();
        addDrawableChild(save);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);

        int titleWidth = this.textRenderer.getWidth(this.title);
        ctx.drawText(this.textRenderer, this.title, (this.width - titleWidth) / 2, 10, 0xFFFFFFFF, true);

        int labelX = this.width / 2 - W / 2;
        ctx.drawText(this.textRenderer, "Default:", labelX, defaultColorField.getY() - 10, 0xFFFFFFFF, true);
        ctx.drawText(this.textRenderer, "Shield:", labelX, shieldColorField.getY() - 10, 0xFFFFFFFF, true);

        if (!errorMessage.isEmpty()) {
            int errorWidth = this.textRenderer.getWidth(errorMessage);
            ctx.drawText(this.textRenderer, errorMessage, (this.width - errorWidth) / 2, this.height - 55, 0xFFFF5555, true);
        }
    }
}
