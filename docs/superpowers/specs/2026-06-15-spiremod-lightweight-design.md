# SpireMod — 轻量级杀戮尖塔 Mod 设计文档

**日期**：2026-06-15  
**状态**：草案  
**方案**：纯 SpirePatch（不依赖 BaseMod）

## 概述

一个极简的杀戮尖塔内容 Mod。新开一局时，玩家自动获得：

- 金币 +200
- 会员卡（原版商店遗物，商店永久半价）
- 送货员（原版罕见遗物，商店自动补货）
- 黑星（原版 Boss 遗物，精英怪掉 2 个遗物）

以上四个效果均使用原版物品，无自定义内容。

## 项目结构

```
SpireMod/
├── src/main/java/spiremod/
│   ├── SpireMod.java              # @SpireInitializer 入口
│   └── patches/
│       ├── GoldPatch.java         # 开局金币 +200
│       └── RelicPatch.java        # 开局发放 3 个遗物
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

- **Hook 目标**：遗物发放阶段（具体类名和方法签名在实现阶段通过反编译源码确认）
- **行为**：向玩家遗物列表追加三个原版遗物实例：
  - `Membership Card`（会员卡）
  - `The Courier`（送货员）
  - `Black Star`（黑星）
- **防护**：仅在新开一局时触发，不在 Boss 遗物选择界面触发

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
