package spiremod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import spiremod.state.LoanState;

import java.io.File;
import java.lang.reflect.Method;

@SpirePatch(
    clz = AbstractPlayer.class,
    method = "initializeClass"
)
public class GoldPatch {
    private static final int BONUS_GOLD = 200;
    private static final String LOAN_FILE_NAME = "loanstate.dat";

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
        deleteLoanFile();
        __instance.gainGold(BONUS_GOLD);
        __instance.displayGold = __instance.gold;
    }

    /**
     * 新开一局时删除上局残留的贷款存档文件，防止旧债务污染新局
     */
    private static void deleteLoanFile() {
        try {
            Method method = SaveHelper.class.getDeclaredMethod("getSaveDir");
            method.setAccessible(true);
            String dir = (String) method.invoke(null);
            if (dir != null) {
                File file = new File(dir, LOAN_FILE_NAME);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
