package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

public class AttackDelayTutorEditorScreen extends Screen {

    private final Screen parentScreen;
    private ButtonWidget soundToggleButton;
    private ButtonWidget textToggleButton;
    private ButtonWidget backButton;

    public AttackDelayTutorEditorScreen(Screen parentScreen) {
        super(Text.literal("Edit - Attack Delay Tutor Settings"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 3;

        soundToggleButton = ButtonWidget.builder(
                Text.of("Sound: " + (ModSettings.attackDelayTutorSoundEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.attackDelayTutorSoundEnabled = !ModSettings.attackDelayTutorSoundEnabled;
                    button.setMessage(Text.of("Sound: " + (ModSettings.attackDelayTutorSoundEnabled ? "ON" : "OFF")));
                }
        ).dimensions(centerX - 75, startY, 150, 20).build();
        addDrawableChild(soundToggleButton);

        textToggleButton = ButtonWidget.builder(
                Text.of("Text: " + (ModSettings.attackDelayTutorTextEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.attackDelayTutorTextEnabled = !ModSettings.attackDelayTutorTextEnabled;
                    button.setMessage(Text.of("Text: " + (ModSettings.attackDelayTutorTextEnabled ? "ON" : "OFF")));
                }
        ).dimensions(centerX - 75, startY + 30, 150, 20).build();
        addDrawableChild(textToggleButton);

        backButton = ButtonWidget.builder(
                Text.of("Save and Back"),
                button -> {
                    ModSettings.save();
                    this.client.setScreen(parentScreen);
                }
        ).dimensions(centerX - 50, this.height - 40, 100, 20).build();
        addDrawableChild(backButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawCenteredText(context, this.title, 10, 0xFFFFFF);
    }

    private void drawCenteredText(DrawContext context, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        context.drawText(this.textRenderer, text, x, y, color, false);
    }
}
