# SpireMod 文档

## 开发文档

- [开发指南](development.md) — 环境准备、构建、部署、测试流程

## 规格文档（specs）

需求与设计文档，遵循**文档先行**原则：修改代码前必须先更新 specs。

| 文档 | 状态 | 说明 |
|------|------|------|
| [PRD v1.1](specs/2026-06-17-spiremod-prd.md) | **当前有效** | 产品需求文档，功能清单与技术架构 |
| [轻量级设计](specs/2026-06-15-spiremod-lightweight-design.md) | 参考 | 初始架构设计，Hook 点与项目结构 |
| [商店贷款设计](specs/2026-06-16-shop-loan-and-heart-penalty-design.md) | **已废弃** | 贷款/还款/心脏惩罚设计，已被 PRD v1.1 取代 |

## 项目规则

位于 `.qoder/rules/` 目录：

- `docs-first.md` — 文档先行规则：改代码前必须先更新 specs
