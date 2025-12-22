# Waynote

> 让每一次旅行都**有条理、有留档**；让每一张机票**更划算、更可控**；让每一笔花费**清清楚楚**。

Waynote 是一个基于 **Jetpack Compose** 的 Android 应用，面向用户提供「**旅行发现 × 行程规划 × 机票演示 × 内容收藏**」的一体化体验，帮助用户在**出发前、旅途中、行程后**持续管理与回顾每次旅行。

---

## 软件定位

Waynote 聚焦旅行全流程的轻量管理：

- **出发前**：浏览目的地、简单行程规划、机票搜索/下单演示（Mock 数据）。  
- **旅途中**：收藏目的地/社区帖子，保存行程模板。
- **行程后**：内容回顾、凭证整理、查看订单（用于报销/签证等）。

---

## 功能概览（当前实现）

- **Compose UI**：首页、目的地、社区、聊天、收藏夹、个人资料、机票、行程规划等页面。
- **首页**：横幅、快捷入口、热门目的地。
- **目的地**：详情页，收藏/取消收藏。
- **收藏夹**：目的地与社区帖子合集。
- **社区**：推荐/关注/活动，搜索，发帖/视频，帖子收藏。
- **消息与聊天**：示例对话，按联系人保存聊天记录。
- **机票与订单**：搜索/结果/支付演示流，订单列表。
- **行程规划与保存**：可编辑行程、样例模板（API 26+ 防护）。
- **个人资料**：头像选择、语言切换、订单/收藏入口、登出。
- **登录注册**：简易登录/注册与锁定逻辑，可跳过登录。

---

## 技术亮点

- **导航**：Compose Navigation + 路由常量统一管理。
- **持久化**：DataStore 保存登录、头像、收藏、订单、聊天记录、行程计划等。
- **多语言**：通过 CompositionLocal 支持英文/中文文案切换。
- **样例数据**：目的地、航班、社区帖子、行程模板，便于演示与测试。

---

## 环境要求

- Android Studio **Koala**（或近期版本）+ **AGP 8.x+**
- **JDK 17+**（配置 `JAVA_HOME`）
- 运行设备/模拟器 **API 26+**（行程规划需 Android 8.0+）

---

## 快速开始

在项目根目录：

```bash
./gradlew assembleDebug
````

或使用 Android Studio 直接运行目标 app。

若出现 `JAVA_HOME` 错误，请设置 JDK 路径，例如：

```bash
export JAVA_HOME=/path/to/jdk17
```

Windows 用户可在系统环境变量中设置 `JAVA_HOME` 指向 JDK 17 安装目录。

---

## 目录概览（常用路径）

* `app/src/main/java/com/example/waynote/WaynoteApp.kt` – 导航宿主与应用壳。
* `app/src/main/java/com/example/waynote/MainActivity.kt` – 入口 Activity。
* `app/src/main/java/com/example/waynote/navigation/` – 路由与 Tab 定义。
* `app/src/main/java/com/example/waynote/core/` – 本地化工具。
* `app/src/main/java/com/example/waynote/data/` – 模型、持久化工具、样例数据。
* `app/src/main/java/com/example/waynote/ui/screens/` – 各功能界面（登录、首页、消息、聊天、社区、收藏、个人、行程、机票）。
* `app/src/main/java/com/example/waynote/ui/theme/` – 主题配置。

---

## 开发团队

* 冯先益
* 孟皓皓

---

## 技术支持

问题反馈、缺陷提交与需求建议，请提交 GitHub Issues：
[https://github.com/android-app-development-course/2025-Autumn-Aberdeen-13-Waynote/issues](https://github.com/android-app-development-course/2025-Autumn-Aberdeen-13-Waynote/issues)

---

## 开源许可证

本项目基于 Apache License 2.0 开源：
[https://github.com/android-app-development-course/2025-Autumn-Aberdeen-13-Waynote/blob/main/LICENSE](https://github.com/android-app-development-course/2025-Autumn-Aberdeen-13-Waynote/blob/main/LICENSE)

---

## 说明与声明

* 本项目主要用于课程与学习目的。
* 附带的素材与样例数据仅用于演示。
* 我们重视用户隐私与数据最小化原则；涉及任何第三方服务的接入将遵循相应协议与法律法规。

```
```
