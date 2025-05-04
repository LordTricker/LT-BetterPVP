package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

/**
 * Wersja zgodna z&nbsp;Minecraft&nbsp;1.19.2 – bez API Buildera dla {@link ButtonWidget}.
 */
public class MainSettingsScreen extends Screen {
    private boolean animationsEnabled;
    private boolean targetingEnabled;

    private ButtonWidget animationsToggleButton;
    private ButtonWidget animationsEditButton;

    private ButtonWidget offhandToggleButton;
    private ButtonWidget offhandEditButton;

    private ButtonWidget targetingToggleButton;
    private ButtonWidget targetingEditButton;

    private ButtonWidget attackTutorButton;
    private ButtonWidget attackTutorEditButton;

    private ButtonWidget armorToggleButton;
    private ButtonWidget armorEditButton;

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
        targetingEnabled  = ModSettings.targetingEnabled;

        rowHeight = 25;
        int btnHeight = 20;

        /* 8 wierszy - 0 tutor, 1 sword, 2 offhand, 3 cursor, 4 armor, 5 autojump, 6 bobbing, 7 shake */
        int totalLines       = 8;
        int totalBlockHeight = totalLines * rowHeight;
        this.startY          = (this.height - totalBlockHeight) / 2;

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int groupLeft       = (this.width - totalGroupWidth) / 2;
        int buttonX         = groupLeft + labelAreaWidth + spacing;
        int y               = startY;

        /* --- Attack‑delay tutor ----------------------------------- */
        attackTutorButton = new ButtonWidget(
                buttonX,
                y,
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
                y,
                20,
                btnHeight,
                Text.of("..."),
                button -> this.client.setScreen(new AttackDelayTutorEditorScreen(this))
        );
        addDrawableChild(attackTutorEditButton);

        /* --- Sword animation -------------------------------------- */
        y += rowHeight;
        animationsToggleButton = new ButtonWidget(
                buttonX,
                y,
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
                y,
                20,
                btnHeight,
                Text.of("..."),
                button -> this.client.setScreen(new AnimationEditorScreen(this))
        );
        addDrawableChild(animationsEditButton);
        animationsEditButton.active = animationsEnabled;

        /* --- Off‑hand animation ----------------------------------- */
        y += rowHeight;
        offhandToggleButton = new ButtonWidget(
                buttonX,
                y,
                80,
                btnHeight,
                Text.of(getToggleDisplay(ModSettings.offhandAnimationEnabled)),
                button -> {
                    ModSettings.offhandAnimationEnabled = !ModSettings.offhandAnimationEnabled;
                    button.setMessage(Text.of(getToggleDisplay(ModSettings.offhandAnimationEnabled)));
                    offhandEditButton.active = ModSettings.offhandAnimationEnabled;
                }
        );
        addDrawableChild(offhandToggleButton);

        offhandEditButton = new ButtonWidget(
                buttonX + 80,
                y,
                20,
                btnHeight,
                Text.of("..."),
                button -> this.client.setScreen(new OffHandAnimationEditorScreen(this))
        );
        addDrawableChild(offhandEditButton);
        offhandEditButton.active = ModSettings.offhandAnimationEnabled;

        /* --- Cursor ESP ------------------------------------------- */
        y += rowHeight;
        targetingToggleButton = new ButtonWidget(
                buttonX,
                y,
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
                y,
                20,
                btnHeight,
                Text.of("..."),
                button -> this.client.setScreen(new TargetEditorScreen(this))
        );
        addDrawableChild(targetingEditButton);
        targetingEditButton.active = targetingEnabled;

        /* --- Armor status ----------------------------------------- */
        y += rowHeight;
        armorToggleButton = new ButtonWidget(
                buttonX,
                y,
                80,
                btnHeight,
                Text.of(getToggleDisplay(ModSettings.armorStatusEnabled)),
                button -> {
                    ModSettings.armorStatusEnabled = !ModSettings.armorStatusEnabled;
                    button.setMessage(Text.of(getToggleDisplay(ModSettings.armorStatusEnabled)));
                    armorEditButton.active = ModSettings.armorStatusEnabled;
                }
        );
        addDrawableChild(armorToggleButton);

        armorEditButton = new ButtonWidget(
                buttonX + 80,
                y,
                20,
                btnHeight,
                Text.of("..."),
                button -> this.client.setScreen(new ArmorStatusEditorScreen(this))
        );
        addDrawableChild(armorEditButton);
        armorEditButton.active = ModSettings.armorStatusEnabled;

        /* --- Minecraft‑owe przełączniki (auto‑jump, bobbing, shake) */
        MinecraftSettingsWidget mc = new MinecraftSettingsWidget();
        y += rowHeight;
        mc.initWidgets(buttonX, y, buttonAreaWidth, btnHeight, rowHeight);
        for (ButtonWidget b : mc.getWidgets()) {
            addDrawableChild(b);
        }

        /* --- Save & quit ------------------------------------------ */
        ButtonWidget saveBtn = new ButtonWidget(
                (this.width - 100) / 2,
                this.height - 30,
                100,
                btnHeight,
                Text.of("Save and Quit"),
                button -> {
                    ModSettings.animationsEnabled = animationsEnabled;
                    ModSettings.targetingEnabled  = targetingEnabled;
                    ModSettings.save();
                    this.close();
                }
        );
        addDrawableChild(saveBtn);
    }

    /* --------------------------------------------------------------------- */

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

        this.textRenderer.draw(matrices, "Attack delay tutor:", labelX, startY + 0 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Sword Animation:",     labelX, startY + 1 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "OffHand Animation:",   labelX, startY + 2 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Cursor ESP:",          labelX, startY + 3 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Armor status:",        labelX, startY + 4 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Auto Jump:",           labelX, startY + 5 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "View Bobbing:",        labelX, startY + 6 * rowHeight + 5, 0xFFFFFF);
        this.textRenderer.draw(matrices, "Screen Shake:",        labelX, startY + 7 * rowHeight + 5, 0xFFFFFF);
    }

    private void drawCenteredTextLocal(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.draw(matrices, text, (float) x, (float) y, color);
    }
}