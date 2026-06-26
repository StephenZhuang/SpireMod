# SpireMod — 轻量级杀戮尖塔 Mod 设计文档

**日期**：2026-06-15  
**状态**：草案  
**方案**：纯 SpirePatch（不依赖 BaseMod）

## 概述

一个极简的杀戮尖塔内容 Mod。新开一局时，玩家自动获得：

- 金币 +200
- 会员卡（Membership Card，原版商店遗物，商店永久半价）
- 御守（Omamori，原版遗物，抵挡前 2 次负面效果）
- 黑星（Black Star，原版 Boss 遗物，精英怪掉 2 个遗物）
- 熔岩蛋（Molten Egg，获得攻击牌时自动升级）
- 剧毒蛋（Toxic Egg，获得技能牌时自动升级）
- 教士面容（Face of Cleric，战斗后最大生命 +1）
- 蛇首遗物（Ssserpent Head，进入 ? 房间 +50 金币）
- 冰冻蛋（Frozen Egg，获得能力牌时自动升级）
- 红宝石钥匙（Ruby Key，解锁 Neow 红色宝箱）
- 绿宝石钥匙（Emerald Key，解锁 Neow 绿色宝箱）
- 蓝宝石钥匙（Sapphire Key，解锁 Neow 蓝色宝箱）

以上均使用原版物品，无自定义遗物内容。商店金币按钮等扩展功能详见 `2026-06-17-spiremod-prd.md`。

## 项目结构

```
SpireMod/
├── src/main/java/spiremod/
│   ├── SpireMod.java              # @SpireInitializer 入口
│   └── patches/
│       ├── GoldPatch.java         # 开局金币 +200
│       ├── RelicPatch.java        # 开局发放遗物与钥匙
│       └── ShopLoanPatch.java     # 商店 +100 金币按钮
├── src/main/resources/
│   └── ModTheSpire.json           # MTS 元信息清单
└── build.gradle                   # Gradle 构建脚本
```

## 依赖

- **ModTheSpire**（必需）— 运行时 patch 框架
- **desktop-1.0.jar**（仅编译期）— 杀戮尖塔游戏类
- 不依赖 BaseMod

## 各组件说明

### SpireMod.java — 入口

标准 `@SpireInitializer` 类，负责向 ModTheSpire 注册 Mod。

### GoldPatch.java

- **Hook 目标**：角色初始化阶段（具体类名和方法签名在实现阶段通过反编译源码确认）
- **行为**：在玩家金币计数器上 +200
- **防护**：仅在新开一局时触发，读档时不触发

### RelicPatch.java

- **Hook 目标**：`AbstractPlayer.initializeStarterRelics`
- **行为**：向玩家遗物列表追加以下原版遗物（已拥有则跳过）：
  - `Membership Card`（会员卡）
  - `Omamori`（御守）— 替代原 Courier
  - `Black Star`（黑星）
  - `Molten Egg`（熔岩蛋，获得攻击牌时自动升级）
  - `Toxic Egg`（剧毒蛋，获得技能牌时自动升级）
  - `Face of Cleric`（教士面容，战斗后最大生命 +1）
  - `Ssserpent Head`（蛇首遗物，进入 ? 房间 +50 金币）
  - `Frozen Egg`（冰冻蛋，获得能力牌时自动升级）
- **行为（钥匙）**：在发放遗物后，将 `Settings.hasRubyKey`、`Settings.hasEmeraldKey`、`Settings.hasSapphireKey` 设为 `true`，使玩家开局即拥有三把钥匙
- **防护**：仅在新开一局时触发，通过 `obtainIfMissing` 检查避免遗物重复；钥匙为布尔标志位，重复设置无副作用

### ModTheSpire.json

标准 MTS 清单文件，包含 mod ID、名称、作者、描述和依赖列表。

## Hook 点候选

实现时需参考杀戮尖塔反编译源码确定精确的类名和方法签名。候选 hook 点：

- **金币**：`AbstractPlayer.gainGold(int)` 或 `AbstractPlayer.initializeGold()` 或角色构造函数
- **遗物**：`AbstractDungeon.initializeRelics()` 附近

## 测试

- 通过 ModTheSpire 启动游戏，新开一局
- 验证：金币在基础值上多了 200（如铁甲战士基础 99 → 299）
- 验证：遗物栏中包含会员卡、送货员、黑星
- 验证：读档时不会重复获得

## build.gradle

标准 ModTheSpire Mod 模板：
- 应用 `java` 插件
- `desktop-1.0.jar` 作为 compileOnly 依赖
- Jar 任务输出到 ModTheSpire 的 `mods/` 目录

## 本机 Mod 加载路径

在当前 Mac + Steam 安装环境下，`ModTheSpire` 读取的是相对路径 `mods/`。实际对应到：

`/Users/stephenzhuang/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/mods/`

不要放到外层目录：

`/Users/stephenzhuang/Library/Application Support/Steam/steamapps/common/SlayTheSpire/mods/`

本项目的 `scripts/build-mod.sh` 和 `build.gradle` 默认都应输出到 `SlayTheSpire.app/Contents/Resources/mods/`，避免 ModTheSpire 启动时找不到本地 Mod。
