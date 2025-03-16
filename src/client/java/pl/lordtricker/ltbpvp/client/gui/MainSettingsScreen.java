package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

/**
 * Ekran główny – Animacja Miecza (toggle + edycja), Targeting (toggle + edycja),
 * widget ustawień Minecrafta (AutoJump, ViewBobbing, ScreenShake, Full Bright).
 */
public class MainSettingsScreen extends Screen {

    private boolean animationsEnabled;
    private boolean targetingEnabled;

    private ButtonWidget animationsEditButton;
    private ButtonWidget targetingEditButton;

    private int rowHeight;
    private final int labelAreaWidth = 120;
    private final int buttonAreaWidth = 100;
    private final int spacing = 10;

    private int startY;

    public MainSettingsScreen() {
        super(Text.literal("LT-BetterPVP Settings"));
    }

    @Override
    protected void init() {
        animationsEnabled = ModSettings.animationsEnabled;
        targetingEnabled = ModSettings.targetingEnabled;

        rowHeight = 25;
        int btnHeight = 20;

        int totalLines = 5;
        int totalBlockHeight = totalLines * rowHeight;

        this.startY = (this.height - totalBlockHeight) / 2;

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int groupLeft = (this.width - totalGroupWidth) / 2;
        int buttonX = groupLeft + labelAreaWidth + spacing;

        ButtonWidget animationsToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(animationsEnabled)),
                btn -> {
                    animationsEnabled = !animationsEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(animationsEnabled)));
                    animationsEditButton.active = animationsEnabled;
                }
        ).dimensions(buttonX, startY, 80, btnHeight).build();
        addDrawableChild(animationsToggleButton);

        animationsEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> {
                    assert this.client != null;
                    this.client.setScreen(new AnimationEditorScreen(this));
                }
        ).dimensions(buttonX + 80, startY, 20, btnHeight).build();
        addDrawableChild(animationsEditButton);

        if (!animationsEnabled) {
            animationsEditButton.active = false;
        }

        ButtonWidget targetingToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(targetingEnabled)),
                btn -> {
                    targetingEnabled = !targetingEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(targetingEnabled)));
                    targetingEditButton.active = targetingEnabled;
                }
        ).dimensions(buttonX, startY + rowHeight, 80, btnHeight).build();
        addDrawableChild(targetingToggleButton);

        targetingEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> {
                    assert this.client != null;
                    this.client.setScreen(new TargetEditorScreen(this));
                }
        ).dimensions(buttonX + 80, startY + rowHeight, 20, btnHeight).build();
        addDrawableChild(targetingEditButton);

        if (!targetingEnabled) {
            targetingEditButton.active = false;
        }

        MinecraftSettingsWidget mcSettingsWidget = new MinecraftSettingsWidget();
        int mcSettingsY = startY + 2 * rowHeight;
        mcSettingsWidget.initWidgets(buttonX, mcSettingsY, buttonAreaWidth, btnHeight, rowHeight);

        for (ButtonWidget b : mcSettingsWidget.getWidgets()) {
            addDrawableChild(b);
        }

        int centerX = this.width / 2;
        int saveBtnWidth = 100;
        int saveBtnX = centerX - saveBtnWidth / 2;

        ButtonWidget saveButton = ButtonWidget.builder(
                Text.of("Save and Quit"),
                btn -> {
                    ModSettings.animationsEnabled = animationsEnabled;
                    ModSettings.targetingEnabled = targetingEnabled;
                    ModSettings.save();
                    this.close();
                }
        ).dimensions(saveBtnX, this.height - 30, saveBtnWidth, btnHeight).build();
        addDrawableChild(saveButton);
    }

    private String getToggleDisplay(boolean value) {
        return value ? "ON" : "OFF";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);

        drawCenteredTextLocal(context, this.title, 10, 0xFFFFFF);

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int labelX = (this.width - totalGroupWidth) / 2;

        context.drawText(this.textRenderer, "Sword Animation:", labelX, startY + 5, 0xFFFFFF, false);
        context.drawText(this.textRenderer, "Cursor ESP:", labelX, startY + rowHeight + 5, 0xFFFFFF, false);

        context.drawText(this.textRenderer, "Auto Jump:", labelX, startY + 2 * rowHeight + 5, 0xFFFFFF, false);
        context.drawText(this.textRenderer, "View Bobbing:", labelX, startY + 3 * rowHeight + 5, 0xFFFFFF, false);
        context.drawText(this.textRenderer, "Screen Shake:", labelX, startY + 4 * rowHeight + 5, 0xFFFFFF, false);
    }

    /**
     * Metoda rysująca wycentrowany tekst na zadanym Y.
     */
    private void drawCenteredTextLocal(DrawContext context, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        context.drawText(this.textRenderer, text, x, y, color, false);
    }
}
