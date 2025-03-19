package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

public class MainSettingsScreen extends Screen {

    private boolean animationsEnabled;
    private boolean targetingEnabled;

    private ButtonWidget animationsEditButton;
    private ButtonWidget targetingEditButton;
    private ButtonWidget attackTutorButton;
    private ButtonWidget attackTutorEditButton;

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

        int totalLines = 6;
        int totalBlockHeight = totalLines * rowHeight;
        this.startY = (this.height - totalBlockHeight) / 2;

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int groupLeft = (this.width - totalGroupWidth) / 2;
        int buttonX = groupLeft + labelAreaWidth + spacing;

        int tutorButtonY = startY;
        attackTutorButton = ButtonWidget.builder(
                Text.of(ModSettings.attackDelayTutorEnabled ? "ON" : "OFF"),
                btn -> {
                    ModSettings.attackDelayTutorEnabled = !ModSettings.attackDelayTutorEnabled;
                    btn.setMessage(Text.of(ModSettings.attackDelayTutorEnabled ? "ON" : "OFF"));
                }
        ).dimensions(buttonX, tutorButtonY, 80, btnHeight).build();
        addDrawableChild(attackTutorButton);

        attackTutorEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> {
                    assert this.client != null;
                    this.client.setScreen(new AttackDelayTutorEditorScreen(this));
                }
        ).dimensions(buttonX + 80, tutorButtonY, 20, btnHeight).build();
        addDrawableChild(attackTutorEditButton);

        int line1Y = startY + rowHeight; // Sword Animation
        ButtonWidget animationsToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(animationsEnabled)),
                btn -> {
                    animationsEnabled = !animationsEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(animationsEnabled)));
                    animationsEditButton.active = animationsEnabled;
                }
        ).dimensions(buttonX, line1Y, 80, btnHeight).build();
        addDrawableChild(animationsToggleButton);

        animationsEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> {
                    assert this.client != null;
                    this.client.setScreen(new AnimationEditorScreen(this));
                }
        ).dimensions(buttonX + 80, line1Y, 20, btnHeight).build();
        addDrawableChild(animationsEditButton);

        if (!animationsEnabled) {
            animationsEditButton.active = false;
        }

        int line2Y = startY + 2 * rowHeight; // Cursor ESP
        ButtonWidget targetingToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(targetingEnabled)),
                btn -> {
                    targetingEnabled = !targetingEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(targetingEnabled)));
                    targetingEditButton.active = targetingEnabled;
                }
        ).dimensions(buttonX, line2Y, 80, btnHeight).build();
        addDrawableChild(targetingToggleButton);

        targetingEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> {
                    assert this.client != null;
                    this.client.setScreen(new TargetEditorScreen(this));
                }
        ).dimensions(buttonX + 80, line2Y, 20, btnHeight).build();
        addDrawableChild(targetingEditButton);

        if (!targetingEnabled) {
            targetingEditButton.active = false;
        }

        MinecraftSettingsWidget mcSettingsWidget = new MinecraftSettingsWidget();
        int mcSettingsY = startY + 3 * rowHeight;
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextLocal(matrices, this.title, 10, 0xFFFFFF);

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int labelX = (this.width - totalGroupWidth) / 2;

        this.textRenderer.draw(matrices, "Attack delay tutor:", labelX, startY + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Sword Animation:", labelX, startY + rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Cursor ESP:", labelX, startY + 2 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Auto Jump:", labelX, startY + 3 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "View Bobbing:", labelX, startY + 4 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Screen Shake:", labelX, startY + 5 * rowHeight + 5, 0xFFFFFF);
    }

    private void drawCenteredTextLocal(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.draw(matrices, text, (float) x, (float) y, color);
    }
}
