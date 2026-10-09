# 规格文档索引

本目录存放 SpireMod 的产品需求文档（PRD）和设计文档。

## 文档先行规则

所有功能变更（新增、修改、删除）必须**先更新本文档目录下的相关文档**，再修改代码。详见 `.qoder/rules/docs-first.md`。

## 当前有效文档

| 文档 | 类型 | 说明 |
|------|------|------|
| [2026-06-17-spiremod-prd.md](2026-06-17-spiremod-prd.md) | PRD | **当前有效**。产品需求文档 v1.1，包含完整功能清单、技术架构和变更历史 |
| [2026-06-15-spiremod-lightweight-design.md](2026-06-15-spiremod-lightweight-design.md) | 设计 | 初始架构设计文档，描述项目结构、Hook 点和依赖关系 |

## 已废弃文档

| 文档 | 废弃日期 | 替代方案 |
|------|---------|---------|
| [2026-06-16-shop-loan-and-heart-penalty-design.md](2026-06-16-shop-loan-and-heart-penalty-design.md) | 2026-06-26 | PRD v1.1 已将商店贷款简化为「+100 金币」按钮，移除债务/还款/心脏战惩罚 |

## 文档命名约定

格式：`YYYY-MM-DD-<主题>.md`

- PRD 文件名包含 `prd`
- 设计文档按主题命名
- 废弃文档保留原文件名，在文件头部标注废弃状态
