package spiremod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.shop.ShopScreen;
import spiremod.state.LoanState;

@SpirePatch(
    clz = ShopScreen.class,
    method = "open"
)
public class ShopLoanPatch {
    private static final float BUTTON_WIDTH = 220.0f * Settings.scale;
    private static final float BUTTON_HEIGHT = 64.0f * Settings.scale;
    private static final float BUTTON_X = 1560.0f * Settings.xScale;
    private static final float BORROW_Y = 380.0f * Settings.yScale;
    private static final float REPAY_Y = 300.0f * Settings.yScale;
    private static final float STATUS_X = 1410.0f * Settings.xScale;
    private static final float STATUS_Y = 470.0f * Settings.yScale;

    private static final Color BUTTON_COLOR = new Color(0.15f, 0.18f, 0.12f, 0.92f);
    private static final Color BUTTON_HOVER_COLOR = new Color(0.28f, 0.32f, 0.22f, 0.98f);
    private static final Color BUTTON_DISABLED_COLOR = new Color(0.14f, 0.14f, 0.14f, 0.72f);

    private static final String BORROW_LABEL = "贷款";
    private static final String REPAY_LABEL = "还款";
    private static final String STATUS_FORMAT = "债务：%d / %d";
    private static final String BORROW_SUCCESS = "已贷款 100 金币";
    private static final String REPAY_SUCCESS = "已还款 100 金币";
    private static final String BORROW_LIMIT = "贷款已达上限";
    private static final String REPAY_FAIL = "金币不足，无法还款";
    private static final String FINAL_ACT_BLOCK = "最终幕无法贷款";

    private static Hitbox borrowHb;
    private static Hitbox repayHb;

    public static void Postfix(ShopScreen __instance) {
        if (borrowHb == null) {
            borrowHb = new Hitbox(BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        if (repayHb == null) {
            repayHb = new Hitbox(BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        borrowHb.move(BUTTON_X, BORROW_Y);
        repayHb.move(BUTTON_X, REPAY_Y);
        borrowHb.unhover();
        repayHb.unhover();
    }

    @SpirePatch(
        clz = ShopScreen.class,
        method = "update"
    )
    public static class UpdatePatch {
        public static void Postfix(ShopScreen __instance) {
            if (borrowHb == null || repayHb == null) {
                return;
            }

            if (shouldShowBorrowButton()) {
                borrowHb.update();
                if (borrowHb.hovered && InputHelper.justClickedLeft) {
                    borrowHb.clickStarted = true;
                }
                if (borrowHb.clicked || borrowHb.clickStarted && !InputHelper.isMouseDown) {
                    borrowHb.clicked = false;
                    borrowHb.clickStarted = false;
                    handleBorrow(__instance);
                }
            }

            if (shouldShowRepayButton()) {
                repayHb.update();
                if (repayHb.hovered && InputHelper.justClickedLeft) {
                    repayHb.clickStarted = true;
                }
                if (repayHb.clicked || repayHb.clickStarted && !InputHelper.isMouseDown) {
                    repayHb.clicked = false;
                    repayHb.clickStarted = false;
                    handleRepay(__instance);
                }
            }
        }
    }

    @SpirePatch(
        clz = ShopScreen.class,
        method = "render"
    )
    public static class RenderPatch {
        public static void Postfix(ShopScreen __instance, SpriteBatch sb) {
            if (borrowHb == null || repayHb == null) {
                return;
            }

            FontHelper.renderFontLeft(
                sb,
                FontHelper.buttonLabelFont,
                String.format(STATUS_FORMAT, LoanState.getCurrentDebt(), LoanState.MAX_DEBT),
                STATUS_X,
                STATUS_Y,
                Settings.GOLD_COLOR
            );

            if (shouldShowBorrowButton()) {
                renderButton(sb, borrowHb, BORROW_LABEL, canBorrowHere());
            }

            if (shouldShowRepayButton()) {
                renderButton(sb, repayHb, REPAY_LABEL, LoanState.canRepay(AbstractDungeon.player));
            }
        }

        private static void renderButton(SpriteBatch sb, Hitbox hb, String label, boolean enabled) {
            Color color = enabled
                ? (hb.hovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR)
                : BUTTON_DISABLED_COLOR;

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
                enabled ? Settings.CREAM_COLOR : Settings.QUARTER_TRANSPARENT_WHITE_COLOR
            );
        }
    }

    private static void handleBorrow(ShopScreen shopScreen) {
        if (isFinalActShop()) {
            fail(shopScreen, FINAL_ACT_BLOCK);
            return;
        }
        if (!LoanState.canBorrow()) {
            fail(shopScreen, BORROW_LIMIT);
            return;
        }
        if (!LoanState.borrow(AbstractDungeon.player)) {
            fail(shopScreen, BORROW_LIMIT);
            return;
        }

        shopScreen.playBuySfx();
        shopScreen.createSpeech(BORROW_SUCCESS);
    }

    private static void handleRepay(ShopScreen shopScreen) {
        if (!LoanState.canRepay(AbstractDungeon.player)) {
            fail(shopScreen, REPAY_FAIL);
            return;
        }
        if (!LoanState.repay(AbstractDungeon.player)) {
            fail(shopScreen, REPAY_FAIL);
            return;
        }

        shopScreen.playBuySfx();
        shopScreen.createSpeech(REPAY_SUCCESS);
    }

    private static void fail(ShopScreen shopScreen, String message) {
        shopScreen.playCantBuySfx();
        shopScreen.createSpeech(message);
    }

    private static boolean shouldShowBorrowButton() {
        return !isFinalActShop();
    }

    private static boolean shouldShowRepayButton() {
        return LoanState.hasDebt();
    }

    private static boolean canBorrowHere() {
        return !isFinalActShop() && LoanState.canBorrow();
    }

    private static boolean isFinalActShop() {
        return TheEnding.ID.equals(AbstractDungeon.id);
    }
}
