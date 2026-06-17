package spiremod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.io.*;
import java.lang.reflect.Method;

/**
 * 贷款状态持久化补丁：
 * - 存档时将债务写入 companion 文件
 * - 读档时从 companion 文件恢复债务
 */
public class LoanSavePatch {

    private static final String LOAN_FILE_NAME = "loanstate.dat";

    /**
     * 存档后写入贷款状态
     */
    @SpirePatch(
        clz = SaveHelper.class,
        method = "saveIfAppropriate"
    )
    public static class SaveLoanPatch {
        public static void Postfix(SaveFile.SaveType saveType) {
            int debt = spiremod.state.LoanState.getCurrentDebt();
            String dir = getSaveDir();
            if (dir == null) return;

            File file = new File(dir, LOAN_FILE_NAME);
            if (debt <= 0) {
                // 无债务时删除文件，避免残留
                if (file.exists()) {
                    file.delete();
                }
                return;
            }
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(String.valueOf(debt));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读档后恢复贷款状态
     */
    @SpirePatch(
        clz = AbstractDungeon.class,
        method = "loadSave"
    )
    public static class LoadLoanPatch {
        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            String dir = getSaveDir();
            if (dir == null) return;

            File file = new File(dir, LOAN_FILE_NAME);
            if (!file.exists()) {
                spiremod.state.LoanState.reset();
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    int debt = Integer.parseInt(line.trim());
                    spiremod.state.LoanState.setCurrentDebt(debt);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                spiremod.state.LoanState.reset();
            }
        }
    }

    /**
     * 通过反射获取 SaveHelper.getSaveDir()（该方法为 private）
     */
    private static String getSaveDir() {
        try {
            Method method = SaveHelper.class.getDeclaredMethod("getSaveDir");
            method.setAccessible(true);
            return (String) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
