package spiremod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spiremod.powers.MerchantWrathPower;
import spiremod.state.LoanState;

@SpirePatch(
    clz = CorruptHeart.class,
    method = "usePreBattleAction"
)
public class HeartLoanPenaltyPatch {
    private static final int STAT_PENALTY = -10;

    public static void Postfix(CorruptHeart __instance) {
        if (!LoanState.hasDebt()) {
            return;
        }

        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            return;
        }

        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(player, __instance, new StrengthPower(player, STAT_PENALTY), STAT_PENALTY)
        );
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(player, __instance, new DexterityPower(player, STAT_PENALTY), STAT_PENALTY)
        );
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(player, __instance, new MerchantWrathPower(player))
        );
    }
}
