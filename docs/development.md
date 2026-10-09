# 开发指南

## 环境准备

### 前置条件

- **Java 8**（项目使用 Java toolchain 8）
- **Gradle**（项目自带 Gradle Wrapper，无需单独安装）
- **杀戮尖塔 Steam 版**（macOS）
- **ModTheSpire**（通过 Steam Workshop 安装）

### 依赖路径（macOS 默认）

| 依赖 | 默认路径 |
|------|---------|
| 杀戮尖塔 jar | `~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/desktop-1.0.jar` |
| ModTheSpire jar | `~/Library/Application Support/Steam/steamapps/workshop/content/646570/1605060445/ModTheSpire.jar` |
| Mods 输出目录 | `~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/mods/` |

如果路径不同，可通过 Gradle 属性覆盖：

```bash
./gradlew jar \
  -PstsJar=/path/to/desktop-1.0.jar \
  -PmtsJar=/path/to/ModTheSpire.jar \
  -PmodsDir=/path/to/mods/
```

## 构建

```bash
# 编译并打包 jar，自动输出到 mods 目录
./gradlew jar

# 或使用构建脚本
bash scripts/build-mod.sh
```

构建产物：`SpireMod-0.1.0.jar`，位于游戏 mods 目录。

### 清理

```bash
./gradlew clean
```

## 部署

构建完成后，jar 已自动复制到 ModTheSpire 的 `mods/` 目录。

启动游戏：

1. 通过 Steam 启动 ModTheSpire（非直接启动杀戮尖塔）
2. 在 ModTheSpire 界面确认 SpireMod 已勾选
3. 点击 Run 进入游戏

## 测试

### 手动测试流程

1. 新开一局游戏
2. 验证开局增益：
   - 金币 = 角色基础金币 + 200（铁甲战士基础 99 → 299）
   - 遗物栏包含：Membership Card、Omamori、Black Star、Molten Egg、Toxic Egg、Frozen Egg、Face of Cleric、Ssserpent Head、Shovel
   - 已拥有三把钥匙（Ruby / Emerald / Sapphire）
3. 验证读档不重复发放
4. 进入商店，验证左上角「+100 金币」按钮可用
5. 点击按钮，验证金币 +100

### 常见验证点

| 场景 | 预期 |
|------|------|
| 新开局 | 获得所有开局遗物和金币 |
| 读档 | 不重复发放 |
| 商店按钮 | 点击 +100 金币，无上限 |
| 战斗后 | Face of Cleric 触发，最大生命 +1 |
| ? 房间 | Ssserpent Head 触发，+50 金币 |
| 休息点挖掘 | Shovel 可用 |

## 代码结构

### 核心 Patch 说明

| 文件 | Hook 目标 | 职责 |
|------|----------|------|
| `SpireMod.java` | — | `@SpireInitializer` 入口，向 MTS 注册 |
| `GoldPatch.java` | `AbstractPlayer.initializeClass` | 开局 +200 金币 |
| `RelicPatch.java` | `AbstractPlayer.initializeStarterRelics` | 开局发放遗物与三把钥匙 |
| `ShopLoanPatch.java` | `ShopScreen.open / update / render` | 商店 +100 金币按钮 UI 与交互 |

### 添加新功能的一般步骤

1. 在 `patches/` 下新建 `XxxPatch.java`
2. 使用 `@SpirePatch` 注解指定目标类和方法
3. 使用 `@Prefix`、`@Postfix` 或 `@Insert` 注入逻辑
4. ModTheSpire 会自动发现并加载，无需手动注册

## 调试技巧

- ModTheSpire 启动时会在控制台输出加载的 Mod 列表
- 如果 Mod 未加载，检查 jar 是否在正确的 `mods/` 目录
- 游戏日志位于 `~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/log/`
