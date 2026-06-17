package spiremod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BlackStar;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MembershipCard;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.relics.SsserpentHead;
import com.megacrit.cardcrawl.relics.ToxicEgg2;

@SpirePatch(
    clz = AbstractPlayer.class,
    method = "initializeStarterRelics"
)
public class RelicPatch {
    public static void Postfix(AbstractPlayer __instance) {
        obtainIfMissing(__instance, MembershipCard.ID);
        obtainIfMissing(__instance, Omamori.ID);
        obtainIfMissing(__instance, BlackStar.ID);
        obtainIfMissing(__instance, MoltenEgg2.ID);
        obtainIfMissing(__instance, ToxicEgg2.ID);
        obtainIfMissing(__instance, CultistMask.ID);
        obtainIfMissing(__instance, SsserpentHead.ID);
        obtainIfMissing(__instance, FrozenEgg2.ID);
    }

    private static void obtainIfMissing(AbstractPlayer player, String relicId) {
        if (player.hasRelic(relicId)) {
            return;
        }

        AbstractRelic relic = RelicLibrary.getRelic(relicId).makeCopy();
        relic.instantObtain(player, player.relics.size(), false);

        if (AbstractDungeon.relicsToRemoveOnStart != null) {
            AbstractDungeon.relicsToRemoveOnStart.add(relicId);
        }
    }
}
