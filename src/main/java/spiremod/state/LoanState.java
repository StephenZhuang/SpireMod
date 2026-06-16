package spiremod.state;

import com.megacrit.cardcrawl.characters.AbstractPlayer;

public final class LoanState {
    public static final int LOAN_STEP = 100;
    public static final int MAX_DEBT = 500;

    private static int currentDebt = 0;

    private LoanState() {
    }

    public static void reset() {
        currentDebt = 0;
    }

    public static int getCurrentDebt() {
        return currentDebt;
    }

    public static boolean canBorrow() {
        return currentDebt < MAX_DEBT;
    }

    public static boolean hasDebt() {
        return currentDebt > 0;
    }

    public static boolean canRepay(AbstractPlayer player) {
        return hasDebt() && player != null && player.gold >= LOAN_STEP;
    }

    public static boolean borrow(AbstractPlayer player) {
        if (player == null || !canBorrow()) {
            return false;
        }

        player.gainGold(LOAN_STEP);
        player.displayGold = player.gold;
        currentDebt += LOAN_STEP;
        return true;
    }

    public static boolean repay(AbstractPlayer player) {
        if (!canRepay(player)) {
            return false;
        }

        player.loseGold(LOAN_STEP);
        player.displayGold = player.gold;
        currentDebt -= LOAN_STEP;
        return true;
    }
}
