package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
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

        soundToggleButton = new ButtonWidget(
                centerX - 75,
                startY,
                150,
                20,
                Text.of("Sound: " + (ModSettings.attackDelayTutorSoundEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.attackDelayTutorSoundEnabled = !ModSettings.attackDelayTutorSoundEnabled;
                    button.setMessage(Text.of("Sound: " + (ModSettings.attackDelayTutorSoundEnabled ? "ON" : "OFF")));
                }
        );
        addDrawableChild(soundToggleButton);

        textToggleButton = new ButtonWidget(
                centerX - 75,
                startY + 30,
                150,
                20,
                Text.of("Text: " + (ModSettings.attackDelayTutorTextEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.attackDelayTutorTextEnabled = !ModSettings.attackDelayTutorTextEnabled;
                    button.setMessage(Text.of("Text: " + (ModSettings.attackDelayTutorTextEnabled ? "ON" : "OFF")));
                }
        );
        addDrawableChild(textToggleButton);

        backButton = new ButtonWidget(
                centerX - 50,
                this.height - 40,
                100,
                20,
                Text.of("Save and Back"),
                button -> {
                    ModSettings.save();
                    this.client.setScreen(parentScreen);
                }
        );
        addDrawableChild(backButton);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.title, 10, 0xFFFFFF);
    }

    private void drawCenteredText(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.drawWithShadow(matrices, text, (float) x, (float) y, color);
    }
}
