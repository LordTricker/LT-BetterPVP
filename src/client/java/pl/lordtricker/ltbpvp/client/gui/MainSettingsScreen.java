package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

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
        super(Text.literal("LT-BetterPVP Settings"));
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
        attackTutorButton = ButtonWidget.builder(
                Text.of(ModSettings.attackDelayTutorEnabled ? "ON" : "OFF"),
                btn -> {
                    ModSettings.attackDelayTutorEnabled = !ModSettings.attackDelayTutorEnabled;
                    btn.setMessage(Text.of(ModSettings.attackDelayTutorEnabled ? "ON" : "OFF"));
                }
        ).dimensions(buttonX, y, 80, btnHeight).build();
        addDrawableChild(attackTutorButton);

        attackTutorEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new AttackDelayTutorEditorScreen(this))
        ).dimensions(buttonX + 80, y, 20, btnHeight).build();
        addDrawableChild(attackTutorEditButton);

        /* --- Sword animation -------------------------------------- */
        y += rowHeight;
        animationsToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(animationsEnabled)),
                btn -> {
                    animationsEnabled = !animationsEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(animationsEnabled)));
                    animationsEditButton.active = animationsEnabled;
                }
        ).dimensions(buttonX, y, 80, btnHeight).build();
        addDrawableChild(animationsToggleButton);

        animationsEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new AnimationEditorScreen(this))
        ).dimensions(buttonX + 80, y, 20, btnHeight).build();
        addDrawableChild(animationsEditButton);
        animationsEditButton.active = animationsEnabled;

        /* --- Off‑hand animation ----------------------------------- */
        y += rowHeight;
        offhandToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(ModSettings.offhandAnimationEnabled)),
                btn -> {
                    ModSettings.offhandAnimationEnabled = !ModSettings.offhandAnimationEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(ModSettings.offhandAnimationEnabled)));
                    offhandEditButton.active = ModSettings.offhandAnimationEnabled;
                }
        ).dimensions(buttonX, y, 80, btnHeight).build();
        addDrawableChild(offhandToggleButton);

        offhandEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new OffHandAnimationEditorScreen(this))
        ).dimensions(buttonX + 80, y, 20, btnHeight).build();
        addDrawableChild(offhandEditButton);
        offhandEditButton.active = ModSettings.offhandAnimationEnabled;

        /* --- Cursor ESP ------------------------------------------- */
        y += rowHeight;
        targetingToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(targetingEnabled)),
                btn -> {
                    targetingEnabled = !targetingEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(targetingEnabled)));
                    targetingEditButton.active = targetingEnabled;
                }
        ).dimensions(buttonX, y, 80, btnHeight).build();
        addDrawableChild(targetingToggleButton);

        targetingEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new TargetEditorScreen(this))
        ).dimensions(buttonX + 80, y, 20, btnHeight).build();
        addDrawableChild(targetingEditButton);
        targetingEditButton.active = targetingEnabled;

        /* --- Armor status ----------------------------------------- */
        y += rowHeight;
        armorToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(ModSettings.armorStatusEnabled)),
                btn -> {
                    ModSettings.armorStatusEnabled = !ModSettings.armorStatusEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(ModSettings.armorStatusEnabled)));
                    armorEditButton.active = ModSettings.armorStatusEnabled;
                }
        ).dimensions(buttonX, y, 80, btnHeight).build();
        addDrawableChild(armorToggleButton);

        armorEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new ArmorStatusEditorScreen(this))
        ).dimensions(buttonX + 80, y, 20, btnHeight).build();
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
        ButtonWidget saveBtn = ButtonWidget.builder(
                Text.of("Save and Quit"),
                btn -> {
                    ModSettings.animationsEnabled = animationsEnabled;
                    ModSettings.targetingEnabled  = targetingEnabled;
                    ModSettings.save();
                    this.close();
                }
        ).dimensions((this.width - 100) / 2, this.height - 30, 100, btnHeight).build();
        addDrawableChild(saveBtn);
    }

    /* --------------------------------------------------------------------- */

    private String getToggleDisplay(boolean value) {
        return value ? "ON" : "OFF";
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);

        drawCenteredTextLocal(ctx, this.title, 10, 0xFFFFFF);

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int labelX = (this.width - totalGroupWidth) / 2;

        ctx.drawText(this.textRenderer, "Attack delay tutor:", labelX, startY + 0 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Sword Animation:",     labelX, startY + 1 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "OffHand Animation:",   labelX, startY + 2 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Cursor ESP:",          labelX, startY + 3 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Armor status:",        labelX, startY + 4 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Auto Jump:",           labelX, startY + 5 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "View Bobbing:",        labelX, startY + 6 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Screen Shake:",        labelX, startY + 7 * rowHeight + 5, 0xFFFFFF, false);
    }

    private void drawCenteredTextLocal(DrawContext ctx, Text text, int y, int color) {
        int w = this.textRenderer.getWidth(text);
        int x = (this.width - w) / 2;
        ctx.drawText(this.textRenderer, text, x, y, color, false);
    }
}