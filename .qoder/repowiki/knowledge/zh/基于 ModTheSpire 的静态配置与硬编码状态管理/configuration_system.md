该模组（Mod）的配置系统高度依赖于 **ModTheSpire (MTS)** 框架的标准约定，采用**静态元数据描述**与**代码内硬编码常量**相结合的方式。由于是客户端游戏模组，它没有传统后端应用中的 `application.properties`、环境变量或动态配置文件加载逻辑。

### 1. 核心配置方式
*   **模组元数据配置**：通过 `src/main/resources/ModTheSpire.json` 定义模组的唯一标识符 (`modid`)、名称、版本、作者以及依赖的游戏和框架版本。这是 MTS 框架识别和加载模组的入口配置。
*   **构建配置**：使用 `build.gradle` 管理编译依赖（指向本地的 SlayTheSpire 和 ModTheSpire jar 包）及资源打包规则。
*   **业务参数硬编码**：所有的游戏性数值（如开局金币数、贷款额度、债务上限、初始遗物列表）均直接定义在 Java 类的 `static final` 常量中（例如 `LoanState.LOAN_STEP`、`GoldPatch.BONUS_GOLD`）。

### 2. 关键文件与逻辑
*   **`src/main/resources/ModTheSpire.json`**：模组的“身份证”，定义了 `spiremod` 的基本信息。
*   **`src/main/java/spiremod/state/LoanState.java`**：作为状态管理中心，定义了借贷系统的核心常量（`LOAN_STEP = 100`, `MAX_DEBT = 500`）和内存状态（`currentDebt`）。
*   **`src/main/java/spiremod/patches/GoldPatch.java`**：配置了开局增益的具体内容，包括增加 200 金币以及在每局开始时清理旧的贷款存档文件 (`loanstate.dat`)。
*   **`src/main/java/spiremod/patches/RelicPatch.java`**：以硬编码列表的形式配置了开局自动获取的遗物（如会员卡、黑星等）以及解锁 Neow 宝箱所需的三把钥匙状态。

### 3. 架构与约定
*   **无外部运行时配置**：模组不提供用户可编辑的 `.cfg` 或 `.json` 配置文件来调整游戏平衡性。所有修改必须通过重新编译代码完成。
*   **状态持久化策略**：虽然大部分配置是静态的，但借贷状态（债务金额）需要通过存档机制持久化。目前代码中通过 `SaveHelper` 定位存档目录并操作 `loanstate.dat` 文件，体现了“配置即代码”与“状态即文件”的分离。
*   **补丁驱动初始化**：利用 `@SpireInitializer` 和 `@SpirePatch` 注解，在游戏特定的生命周期节点（如角色初始化 `initializeClass`）注入配置逻辑。

### 4. 开发者准则
*   **修改数值**：若需调整开局金币或借贷规则，应直接修改对应 Patch 类或 State 类中的 `private static final` 常量。
*   **新增遗物/增益**：在 `RelicPatch.java` 的 `Postfix` 方法中添加新的 `obtainIfMissing` 调用，并确保引用正确的遗物 ID。
*   **版本维护**：更新 `ModTheSpire.json` 中的 `version` 字段以跟踪模组迭代，同时确保 `sts_version` 和 `mts_version` 与目标环境兼容。