# SpireMod

一个轻量级杀戮尖塔 Mod。每次新开一局时自动获得：

- `+200` 金币
- `Membership Card`
- `Courier`
- `Black Star`
- `Molten Egg`
- `Toxic Egg`
- `Frozen Egg`

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
