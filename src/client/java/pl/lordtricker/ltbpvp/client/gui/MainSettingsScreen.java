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
        super(Text.of("LT-BetterPVP Settings"));
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

        int tutorButtonY = startY;
        attackTutorButton = new ButtonWidget(
                buttonX,
                tutorButtonY,
                80,
                btnHeight,
                Text.of(ModSettings.attackDelayTutorEnabled ? "ON" : "OFF"),
                button -> {
                    ModSettings.attackDelayTutorEnabled = !ModSettings.attackDelayTutorEnabled;
                    button.setMessage(Text.of(ModSettings.attackDelayTutorEnabled ? "ON" : "OFF"));
                }
        );
        addDrawableChild(attackTutorButton);

        attackTutorEditButton = new ButtonWidget(
                buttonX + 80,
                tutorButtonY,
                20,
                btnHeight,
                Text.of("..."),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(new AttackDelayTutorEditorScreen(this));
                }
        );
        addDrawableChild(attackTutorEditButton);

        int line1Y = startY + rowHeight; // Sword Animation
        ButtonWidget animationsToggleButton = new ButtonWidget(
                buttonX,
                line1Y,
                80,
                btnHeight,
                Text.of(getToggleDisplay(animationsEnabled)),
                button -> {
                    animationsEnabled = !animationsEnabled;
                    button.setMessage(Text.of(getToggleDisplay(animationsEnabled)));
                    animationsEditButton.active = animationsEnabled;
                }
        );
        addDrawableChild(animationsToggleButton);

        animationsEditButton = new ButtonWidget(
                buttonX + 80,
                line1Y,
                20,
                btnHeight,
                Text.of("..."),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(new AnimationEditorScreen(this));
                }
        );
        addDrawableChild(animationsEditButton);

        if (!animationsEnabled) {
            animationsEditButton.active = false;
        }

        int line2Y = startY + 2 * rowHeight; // Cursor ESP
        ButtonWidget targetingToggleButton = new ButtonWidget(
                buttonX,
                line2Y,
                80,
                btnHeight,
                Text.of(getToggleDisplay(targetingEnabled)),
                button -> {
                    targetingEnabled = !targetingEnabled;
                    button.setMessage(Text.of(getToggleDisplay(targetingEnabled)));
                    targetingEditButton.active = targetingEnabled;
                }
        );
        addDrawableChild(targetingToggleButton);

        targetingEditButton = new ButtonWidget(
                buttonX + 80,
                line2Y,
                20,
                btnHeight,
                Text.of("..."),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(new TargetEditorScreen(this));
                }
        );
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
        ButtonWidget saveButton = new ButtonWidget(
                saveBtnX,
                this.height - 30,
                saveBtnWidth,
                btnHeight,
                Text.of("Save and Quit"),
                button -> {
                    ModSettings.animationsEnabled = animationsEnabled;
                    ModSettings.targetingEnabled = targetingEnabled;
                    ModSettings.save();
                    this.close();
                }
        );
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
    }

    private void drawCenteredTextLocal(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.draw(matrices, text, (float) x, (float) y, color);
    }
}
