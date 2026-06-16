package spiremod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import spiremod.state.LoanState;

@SpirePatch(
    clz = AbstractPlayer.class,
    method = "initializeClass"
)
public class GoldPatch {
    private static final int BONUS_GOLD = 200;

    public static void Postfix(
        AbstractPlayer __instance,
        String imgUrl,
        String shoulder2ImgUrl,
        String shoulderImgUrl,
        String corpseImgUrl,
        CharSelectInfo loadout,
        float hbX,
        float hbY,
        float hbW,
        float hbH,
        EnergyManager energy
    ) {
        LoanState.reset();
        __instance.gainGold(BONUS_GOLD);
        __instance.displayGold = __instance.gold;
    }
}
