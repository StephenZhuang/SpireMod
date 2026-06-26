# SpireMod 产品需求文档（PRD）

**版本**：1.1  
**最后更新**：2026-06-26  
**状态**：当前有效  

## 1. 产品概述

SpireMod 是一个轻量级杀戮尖塔（Slay the Spire）客户端 Mod，基于 ModTheSpire + SpirePatch 框架，不依赖 BaseMod。目标是为玩家提供开局资源增益和商店金币补给，提升游戏流畅度。

## 2. 功能清单

### 2.1 开局增益

每次新开一局游戏时，玩家自动获得以下物品（读档不重复发放）：

| 类型 | 内容 | 说明 |
|------|------|------|
| 金币 | +200 | 在角色基础金币上额外增加 |
| 遗物 | Membership Card（会员卡） | 商店永久半价 |
| 遗物 | Omamori（御守） | 抵挡前 2 次负面效果 |
| 遗物 | Black Star（黑星） | 精英怪掉落 2 个遗物 |
| 遗物 | Molten Egg（熔岩蛋） | 获得攻击牌时自动升级 |
| 遗物 | Toxic Egg（剧毒蛋） | 获得技能牌时自动升级 |
| 遗物 | Face of Cleric（教士面容） | 战斗后最大生命 +1 |
| 遗物 | Ssserpent Head（蛇首遗物） | 进入 ? 房间 +50 金币 |
| 遗物 | Frozen Egg（冰冻蛋） | 获得能力牌时自动升级 |
| 遗物 | Shovel（铲子） | 休息点可挖掘随机遗物 |
| 钥匙 | Ruby Key（红宝石钥匙） | 解锁 Neow 红色宝箱 |
| 钥匙 | Emerald Key（绿宝石钥匙） | 解锁 Neow 绿色宝箱 |
| 钥匙 | Sapphire Key（蓝宝石钥匙） | 解锁 Neow 蓝色宝箱 |

### 2.2 商店金币按钮

在商店界面左上角增加一个「+100 金币」按钮：

- **效果**：点击即获得 100 金币，无次数限制，无需还款
- **UI 位置**：按钮位于商店左上角（BUTTON_X=140, BUTTON_Y=940），不遮挡商店删牌区域
- **无债务机制**：不记录债务，不显示债务状态，无还款按钮

## 3. 技术架构

### 3.1 项目结构

```
SpireMod/
├── src/main/java/spiremod/
│   ├── SpireMod.java                  # @SpireInitializer 入口
│   └── patches/
│       ├── GoldPatch.java             # 开局金币+200
│       ├── RelicPatch.java            # 开局发放遗物与钥匙
│       └── ShopLoanPatch.java         # 商店 +100 金币按钮
├── src/main/resources/
│   └── ModTheSpire.json               # MTS 元信息清单
├── docs/                              # 设计文档与 PRD
├── scripts/
│   └── build-mod.sh                   # 构建脚本
└── build.gradle                       # Gradle 构建配置
```

### 3.2 核心模块

| 模块 | 文件 | Hook 目标 | 职责 |
|------|------|----------|------|
| 入口 | SpireMod.java | — | ModTheSpire 注册入口 |
| 金币增益 | GoldPatch.java | AbstractPlayer.initializeClass | 开局 +200 金币 |
| 遗物增益 | RelicPatch.java | AbstractPlayer.initializeStarterRelics | 开局发放遗物与三把钥匙 |
| 商店金币 | ShopLoanPatch.java | ShopScreen.open / update / render | 商店 +100 金币按钮 UI 与交互 |

### 3.3 依赖

- **ModTheSpire.jar**（运行时）— Patch 框架
- **desktop-1.0.jar**（编译期）— 杀戮尖塔游戏类
- 不依赖 BaseMod

## 4. 变更历史

| 日期 | 变更内容 |
|------|---------|
| 2026-06-15 | 初始版本：+200 金币、Membership Card、Courier、Black Star |
| 2026-06-16 | 新增 Molten Egg、Toxic Egg、Frozen Egg |
| 2026-06-16 | Courier 替换为 Omamori；新增商店贷款/还款系统；新增心脏战债务惩罚 |
| 2026-06-17 | 新增 Cultist Mask、Ssserpent Head 初始遗物；商店贷款按钮从右下角移至左上角 |
| 2026-06-17 | 修复：Cultist Mask 替换为 Face of Cleric；新增 LoanSavePatch 实现贷款状态存档/读档持久化 |
| 2026-06-17 | 修复：GoldPatch 新开一局时删除残留 loanstate.dat，防止上局债务污染新局 |
| 2026-06-23 | 新增开局获得红宝石、绿宝石、蓝宝石三把钥匙（Settings.hasRubyKey/hasEmeraldKey/hasSapphireKey） |
| 2026-06-24 | 新增 Shovel（铲子）开局遗物，休息点可挖掘随机遗物 |
| 2026-06-26 | 商店贷款系统简化为「+100 金币」按钮（无债务、无还款、无上限）；移除心脏战债务惩罚、MerchantWrathPower、LoanState、LoanSavePatch |

## 5. 设计约束

- 不修改普通战斗、事件或地图逻辑
- 不引入 BaseMod，不做完整本地化
- 按钮采用轻量绘制（Hitbox + WHITE_SQUARE_IMG），不使用复杂美术资源
