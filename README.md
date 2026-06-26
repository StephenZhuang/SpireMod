# SpireMod

一个轻量级杀戮尖塔 Mod（基于 ModTheSpire，不依赖 BaseMod）。

## 功能

### 开局增益

每次新开一局时自动获得（读档不重复发放）：

- `+200` 金币
- `Membership Card`（会员卡，商店永久半价）
- `Omamori`（御守，抵挡前 2 次负面效果）
- `Black Star`（黑星，精英怪掉落 2 个遗物）
- `Molten Egg`（熔岩蛋，获得攻击牌时自动升级）
- `Toxic Egg`（剧毒蛋，获得技能牌时自动升级）
- `Frozen Egg`（冰冻蛋，获得能力牌时自动升级）
- `Face of Cleric`（教士面容，战斗后最大生命 +1）
- `Ssserpent Head`（蛇首遗物，进入 ? 房间 +50 金币）
- `Shovel`（铲子，休息点可挖掘随机遗物）
- `Ruby Key` / `Emerald Key` / `Sapphire Key`（三把钥匙）

### 商店金币按钮

商店左上角提供「+100 金币」按钮，点击即获得 100 金币，无次数限制，无需还款。

## 构建

当前仓库额外提供了一个不依赖 Gradle 的本地构建脚本：

```bash
./scripts/build-mod.sh
```

默认会把生成的 `SpireMod.jar` 输出到 `SlayTheSpire.app/Contents/Resources/mods/`。

## 可选路径覆盖

如果你的 Steam 或创意工坊路径不同，可以通过环境变量覆盖：

```bash
STS_JAR="/path/to/desktop-1.0.jar" \
MTS_JAR="/path/to/ModTheSpire.jar" \
MODS_DIR="/path/to/SlayTheSpire/mods" \
./scripts/build-mod.sh
```

## Gradle

仓库里也保留了 `build.gradle`，后续如果你想补 `gradle wrapper` 或接入更完整的构建流程，可以继续沿用。

## Mac 路径说明

在这台机器上，`ModTheSpire` 读取的是相对路径 `mods/`，实际落点对应到：

`SlayTheSpire.app/Contents/Resources/mods/`

不是外层的：

`SlayTheSpire/mods/`
