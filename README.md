# SpireMod

杀戮尖塔（Slay the Spire）轻量级客户端 Mod，基于 ModTheSpire + SpirePatch，不依赖 BaseMod。

## 功能

### 开局增益

每次新开一局自动获得：

| 类型 | 内容 | 说明 |
|------|------|------|
| 金币 | +200 | 在角色基础金币上额外增加 |
| 遗物 | Membership Card | 商店永久半价 |
| 遗物 | Omamori | 抵挡前 2 次负面效果 |
| 遗物 | Black Star | 精英怪掉落 2 个遗物 |
| 遗物 | Molten Egg | 获得攻击牌时自动升级 |
| 遗物 | Toxic Egg | 获得技能牌时自动升级 |
| 遗物 | Frozen Egg | 获得能力牌时自动升级 |
| 遗物 | Face of Cleric | 战斗后最大生命 +1 |
| 遗物 | Ssserpent Head | 进入 ? 房间 +50 金币 |
| 遗物 | Shovel | 休息点可挖掘随机遗物 |
| 钥匙 | Ruby / Emerald / Sapphire Key | 解锁 Neow 三色宝箱 |

### 商店金币按钮

商店界面左上角「+100 金币」按钮，点击即得 100 金币，无次数限制，无需还款。

## 技术栈

- **语言**：Java 8
- **构建**：Gradle
- **框架**：ModTheSpire + SpirePatch（纯 patch，不依赖 BaseMod）
- **运行环境**：杀戮尖塔 Steam 版（macOS）

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
├── docs/                          # 文档（见 docs/README.md）
├── scripts/
│   └── build-mod.sh               # 构建脚本
└── build.gradle                   # Gradle 构建配置
```

## 快速开始

```bash
# 编译并输出 jar 到游戏 mods 目录
./gradlew jar

# 或使用构建脚本
bash scripts/build-mod.sh
```

构建产物会自动放到 ModTheSpire 的 `mods/` 目录，通过 ModTheSpire 启动游戏即可加载。

详细开发指南见 [docs/development.md](docs/development.md)。

## 文档

- [文档索引](docs/README.md)
- [产品需求文档 (PRD)](docs/superpowers/specs/2026-06-17-spiremod-prd.md)
- [开发指南](docs/development.md)
- [变更日志](CHANGELOG.md)
