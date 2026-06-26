该项目采用 **Gradle** 作为主要的构建工具，同时提供了一套基于 Shell 脚本的轻量级手动构建方案，以适应不同的开发环境需求。

### 1. 构建系统架构
- **主构建工具 (Gradle)**: 使用 `build.gradle` 定义项目结构。配置了 Java 8 工具链，通过 `compileOnly` 依赖引入《杀戮尖塔》(Slay the Spire) 和 `ModTheSpire` 的核心 JAR 包。构建产物会自动打包并部署到本地游戏的 `mods` 目录中。
- **备用构建脚本 (Shell)**: `scripts/build-mod.sh` 提供了一个不依赖 Gradle 环境的纯手动构建流程。它直接使用 `javac` 进行编译，并使用 `jar` 命令打包。该脚本支持通过环境变量 (`STS_JAR`, `MTS_JAR`, `MODS_DIR`) 灵活指定游戏路径。

### 2. 关键文件与配置
- **`build.gradle`**: 核心构建配置文件。定义了版本号 (`0.1.0`)、Java 版本以及针对 macOS 默认 Steam 路径的自动探测逻辑。在打包阶段会校验依赖文件是否存在，并自动创建目标目录。
- **`scripts/build-mod.sh`**: 跨平台兼容的构建脚本（目前主要针对 macOS/Linux 路径风格）。它执行清理、编译、资源拷贝和打包的全流程。
- **`settings.gradle`**: 定义根项目名称为 `SpireMod`。
- **`src/main/resources/ModTheSpire.json`**: Mod 的元数据文件，会被直接包含在最终的 JAR 包根目录下。

### 3. 开发者规范与约定
- **路径配置**: 默认配置针对 macOS 系统下的 Steam 安装路径。如果开发环境不同（如 Windows 或 Linux），必须通过 Gradle 属性 (`-PstsJar=...`) 或 Shell 环境变量覆盖默认路径。
- **构建输出**: 无论是通过 `gradle jar` 还是 `./scripts/build-mod.sh`，最终生成的 `SpireMod.jar` 都会直接输出到游戏的 `mods` 文件夹，方便即时测试。
- **编码规范**: Gradle 任务中明确指定了 `UTF-8` 编码，确保源码中的中文注释或字符串处理正确。
- **版本管理**: 当前版本硬编码在 `build.gradle` 中 (`version = '0.1.0'`)，建议在发布前根据功能迭代更新此版本号。