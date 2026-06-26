package spiremod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.shop.ShopScreen;

@SpirePatch(
    clz = ShopScreen.class,
    method = "open"
)
public class ShopLoanPatch {
    private static final float BUTTON_WIDTH = 220.0f * Settings.scale;
    private static final float BUTTON_HEIGHT = 64.0f * Settings.scale;
    private static final float BUTTON_X = 140.0f * Settings.xScale;
    private static final float BUTTON_Y = 940.0f * Settings.yScale;

    private static final Color BUTTON_COLOR = new Color(0.15f, 0.18f, 0.12f, 0.92f);
    private static final Color BUTTON_HOVER_COLOR = new Color(0.28f, 0.32f, 0.22f, 0.98f);

    private static final String BUTTON_LABEL = "+100 金币";
    private static final String SUCCESS_MSG = "获得 100 金币";
    private static final int GOLD_AMOUNT = 100;

    private static Hitbox buttonHb;

    public static void Postfix(ShopScreen __instance) {
        if (buttonHb == null) {
            buttonHb = new Hitbox(BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        buttonHb.move(BUTTON_X, BUTTON_Y);
        buttonHb.unhover();
    }

    @SpirePatch(
        clz = ShopScreen.class,
        method = "update"
    )
    public static class UpdatePatch {
        public static void Postfix(ShopScreen __instance) {
            if (buttonHb == null) {
                return;
            }

            buttonHb.update();
            if (buttonHb.hovered && InputHelper.justClickedLeft) {
                buttonHb.clickStarted = true;
            }
            if (buttonHb.clicked || buttonHb.clickStarted && !InputHelper.isMouseDown) {
                buttonHb.clicked = false;
                buttonHb.clickStarted = false;
                handleClaimGold(__instance);
            }
        }
    }

    @SpirePatch(
        clz = ShopScreen.class,
        method = "render"
    )
    public static class RenderPatch {
        public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
            if (buttonHb == null) {
                return;
            }
            renderButton(sb, buttonHb, BUTTON_LABEL);
        }

        private static void renderButton(SpriteBatch sb, Hitbox hb, String label) {
            Color color = hb.hovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR;
            sb.setColor(color);
            sb.draw(
                ImageMaster.WHITE_SQUARE_IMG,
                hb.x,
                hb.y,
                hb.width,
                hb.height
            );
            sb.setColor(Color.WHITE);

            FontHelper.renderFontCentered(
                sb,
                FontHelper.buttonLabelFont,
                label,
                hb.cX,
                hb.cY + 8.0f * Settings.scale,
                Settings.CREAM_COLOR
            );
        }
    }

    private static void handleClaimGold(ShopScreen shopScreen) {
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractDungeon.player.gainGold(GOLD_AMOUNT);
        AbstractDungeon.player.displayGold = AbstractDungeon.player.gold;
        shopScreen.playBuySfx();
        shopScreen.createSpeech(SUCCESS_MSG);
    }
}
