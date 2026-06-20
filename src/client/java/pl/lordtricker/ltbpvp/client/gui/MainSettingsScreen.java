package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;

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

    private ButtonWidget lowFireToggleButton;
    private ButtonWidget lowFireEditButton;

    private ButtonWidget bobberToggleButton;
    private ButtonWidget bobberEditButton;

    private ButtonWidget autoJumpButton;
    private ButtonWidget bobViewButton;
    private ButtonWidget damageTiltButton;
    private ButtonWidget cooldownTimerButton;
    private ButtonWidget cooldownTimerEditButton;
    private ButtonWidget distanceCycleButton;

    private int rowHeight;
    private final int labelAreaWidth = 148;
    private final int buttonAreaWidth = 140;
    private final int spacing = 10;

    private int startY;
    private int compactLabelY;
    private int compactButtonY;
    private int compactLeft;
    private final int compactButtonWidth = 70;
    private final int compactGap = 6;

    private final int toggleWidth = 110;
    private final int editWidth = 30;

    public MainSettingsScreen() {
        super(Text.literal("LT-BetterPVP Settings"));
    }

    @Override
    protected void init() {
        animationsEnabled = CoreSettings.animationsEnabled;
        targetingEnabled  = CoreSettings.targetingEnabled;

        rowHeight = 25;
        int btnHeight = 20;
        int compactBlockHeight = 44;
        int compactExtraGap = 8;

        /* 8 rows + compact block: 0 tutor, 1 sword, 2 offhand, 3 cursor, 4 armor, 5 lowfire, 6 bobber, 7 cooldown */
        int standardRows    = 8;
        int totalBlockHeight = standardRows * rowHeight + compactBlockHeight + compactExtraGap;
        this.startY = (this.height - totalBlockHeight) / 2;

        int totalGroupWidth = labelAreaWidth + spacing + buttonAreaWidth;
        int groupLeft       = (this.width - totalGroupWidth) / 2;
        int buttonX         = groupLeft + labelAreaWidth + spacing;
        int y               = startY;

        /* --- Attack-delay tutor ----------------------------------- */
        attackTutorButton = ButtonWidget.builder(
                Text.of(CoreSettings.attackDelayTutorEnabled ? "ON" : "OFF"),
                btn -> {
                    CoreSettings.attackDelayTutorEnabled = !CoreSettings.attackDelayTutorEnabled;
                    btn.setMessage(Text.of(CoreSettings.attackDelayTutorEnabled ? "ON" : "OFF"));
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(attackTutorButton);

        attackTutorEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new AttackDelayTutorEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
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
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(animationsToggleButton);

        animationsEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new AnimationEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(animationsEditButton);
        animationsEditButton.active = animationsEnabled;

        /* --- Off-hand animation ----------------------------------- */
        y += rowHeight;
        offhandToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(CoreSettings.offhandAnimationEnabled)),
                btn -> {
                    CoreSettings.offhandAnimationEnabled = !CoreSettings.offhandAnimationEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(CoreSettings.offhandAnimationEnabled)));
                    offhandEditButton.active = CoreSettings.offhandAnimationEnabled;
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(offhandToggleButton);

        offhandEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new OffHandAnimationEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(offhandEditButton);
        offhandEditButton.active = CoreSettings.offhandAnimationEnabled;

        /* --- Cursor ESP ------------------------------------------- */
        y += rowHeight;
        targetingToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(targetingEnabled)),
                btn -> {
                    targetingEnabled = !targetingEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(targetingEnabled)));
                    targetingEditButton.active = targetingEnabled;
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(targetingToggleButton);

        targetingEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new TargetEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(targetingEditButton);
        targetingEditButton.active = targetingEnabled;

        /* --- Armor status ----------------------------------------- */
        y += rowHeight;
        armorToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(CoreSettings.armorStatusEnabled)),
                btn -> {
                    CoreSettings.armorStatusEnabled = !CoreSettings.armorStatusEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(CoreSettings.armorStatusEnabled)));
                    armorEditButton.active = CoreSettings.armorStatusEnabled;
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(armorToggleButton);

        armorEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new ArmorStatusEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(armorEditButton);
        armorEditButton.active = CoreSettings.armorStatusEnabled;

        /* --- Low fire -------------------------------------------- */
        y += rowHeight;
        lowFireToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(CoreSettings.lowFireEnabled)),
                btn -> {
                    CoreSettings.lowFireEnabled = !CoreSettings.lowFireEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(CoreSettings.lowFireEnabled)));
                    lowFireEditButton.active = CoreSettings.lowFireEnabled;
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(lowFireToggleButton);

        lowFireEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new LowFireEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(lowFireEditButton);
        lowFireEditButton.active = CoreSettings.lowFireEnabled;

        /* --- Fishing bobber ------------------------------------- */
        y += rowHeight;
        bobberToggleButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(CoreSettings.fishingBobberEnabled)),
                btn -> {
                    CoreSettings.fishingBobberEnabled = !CoreSettings.fishingBobberEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(CoreSettings.fishingBobberEnabled)));
                    bobberEditButton.active = CoreSettings.fishingBobberEnabled;
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(bobberToggleButton);

        bobberEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new FishingBobberEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(bobberEditButton);
        bobberEditButton.active = CoreSettings.fishingBobberEnabled;

        /* --- Cooldown timer ------------------------------------ */
        y += rowHeight;
        cooldownTimerButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(CoreSettings.cooldownTimerEnabled)),
                btn -> {
                    CoreSettings.cooldownTimerEnabled = !CoreSettings.cooldownTimerEnabled;
                    btn.setMessage(Text.of(getToggleDisplay(CoreSettings.cooldownTimerEnabled)));
                    cooldownTimerEditButton.active = CoreSettings.cooldownTimerEnabled;
                }
        ).dimensions(buttonX, y, toggleWidth, btnHeight).build();
        addDrawableChild(cooldownTimerButton);

        cooldownTimerEditButton = ButtonWidget.builder(
                Text.of("..."),
                btn -> this.client.setScreen(new CooldownTimerEditorScreen(this))
        ).dimensions(buttonX + toggleWidth, y, editWidth, btnHeight).build();
        addDrawableChild(cooldownTimerEditButton);
        cooldownTimerEditButton.active = CoreSettings.cooldownTimerEnabled;

        /* --- Compact Minecraft toggles --------------------------- */
        y += rowHeight + compactExtraGap;
        compactLabelY = y;
        compactButtonY = y + 12;
        compactLeft = (this.width - (compactButtonWidth * 4 + compactGap * 3)) / 2;

        MinecraftClient client = MinecraftClient.getInstance();

        boolean autoJump = client.options.getAutoJump().getValue();
        autoJumpButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(autoJump)),
                btn -> {
                    boolean current = client.options.getAutoJump().getValue();
                    client.options.getAutoJump().setValue(!current);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        ).dimensions(compactLeft, compactButtonY, compactButtonWidth, btnHeight).build();
        addDrawableChild(autoJumpButton);

        boolean bobView = client.options.getBobView().getValue();
        bobViewButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(bobView)),
                btn -> {
                    boolean current = client.options.getBobView().getValue();
                    client.options.getBobView().setValue(!current);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        ).dimensions(compactLeft + compactButtonWidth + compactGap, compactButtonY, compactButtonWidth, btnHeight).build();
        addDrawableChild(bobViewButton);

        double tiltValue = client.options.getDamageTiltStrength().getValue();
        boolean tiltEnabled = tiltValue > 0.0;
        damageTiltButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(tiltEnabled)),
                btn -> {
                    double curr = client.options.getDamageTiltStrength().getValue();
                    boolean currEnabled = (curr > 0.0);
                    double newVal = currEnabled ? 0.0 : 1.0;
                    client.options.getDamageTiltStrength().setValue(newVal);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(newVal > 0.0)));
                }
        ).dimensions(compactLeft + (compactButtonWidth + compactGap) * 2, compactButtonY, compactButtonWidth, btnHeight).build();
        addDrawableChild(damageTiltButton);

        distanceCycleButton = ButtonWidget.builder(
                Text.of(getDistanceDisplay()),
                btn -> {
                    cycleDistanceDisplay();
                    btn.setMessage(Text.of(getDistanceDisplay()));
                }
        ).dimensions(compactLeft + (compactButtonWidth + compactGap) * 3, compactButtonY, compactButtonWidth, btnHeight).build();
        addDrawableChild(distanceCycleButton);

        /* --- Save & quit ------------------------------------------ */
        ButtonWidget saveBtn = ButtonWidget.builder(
                Text.of("Save and Quit"),
                btn -> {
                    CoreSettings.animationsEnabled = animationsEnabled;
                    CoreSettings.targetingEnabled  = targetingEnabled;
                    CoreSettings.save();
                    this.close();
                }
        ).dimensions((this.width - 100) / 2, this.height - 30, 100, btnHeight).build();
        addDrawableChild(saveBtn);
    }

    private String getToggleDisplay(boolean value) {
        return value ? "ON" : "OFF";
    }

    private String getDistanceDisplay() {
        return CoreSettings.distanceHudEnabled ? CoreSettings.distanceDisplayMode.getLabel() : "OFF";
    }

    private void cycleDistanceDisplay() {
        if (!CoreSettings.distanceHudEnabled) {
            CoreSettings.distanceHudEnabled = true;
            CoreSettings.distanceDisplayMode = pl.lordtricker.ltbpvp.core.enums.DistanceDisplayMode.ENTITY_ONLY;
            return;
        }
        switch (CoreSettings.distanceDisplayMode) {
            case ENTITY_ONLY -> CoreSettings.distanceDisplayMode = CoreSettings.distanceDisplayMode.next();
            case ALL -> {
                CoreSettings.distanceHudEnabled = false;
                CoreSettings.distanceDisplayMode = pl.lordtricker.ltbpvp.core.enums.DistanceDisplayMode.ENTITY_ONLY;
            }
        }
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
        ctx.drawText(this.textRenderer, "Low Fire:",            labelX, startY + 5 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Fishing Bobber:",      labelX, startY + 6 * rowHeight + 5, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Cooldown Timer:",      labelX, startY + 7 * rowHeight + 5, 0xFFFFFF, false);

        ctx.drawText(this.textRenderer, "Auto Jump:", compactLeft, compactLabelY, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "View Bobbing:", compactLeft + compactButtonWidth + compactGap, compactLabelY, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Screen Shake:", compactLeft + (compactButtonWidth + compactGap) * 2, compactLabelY, 0xFFFFFF, false);
        ctx.drawText(this.textRenderer, "Distance HUD:", compactLeft + (compactButtonWidth + compactGap) * 3, compactLabelY, 0xFFFFFF, false);
    }

    private void drawCenteredTextLocal(DrawContext ctx, Text text, int y, int color) {
        int w = this.textRenderer.getWidth(text);
        int x = (this.width - w) / 2;
        ctx.drawText(this.textRenderer, text, x, y, color, false);
    }
}
