package spiremod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MerchantWrathPower extends AbstractPower {
    public static final String POWER_ID = "spiremod:MerchantWrath";
    private static final String POWER_NAME = "商人的愤怒";
    private static final int HP_LOSS_PER_TURN = 10;

    public MerchantWrathPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = POWER_NAME;
        this.owner = owner;
        this.amount = HP_LOSS_PER_TURN;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
        this.canGoNegative = false;
        Texture img = ImageMaster.WHITE_SQUARE_IMG;
        this.img = img;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new LoseHPAction(this.owner, this.owner, this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = "回合开始时失去 " + this.amount + " 点生命。";
    }
}
