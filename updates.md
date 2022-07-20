
## alpha0.1.9(2022-07-20/2)
- 添加了screen从json的加载
- 添加了screen的xml格式和dtd文件
- 主菜单加入了splashtext
- 添加FAML和相关的解析类包
- Grass3d可以设置窗口位置和大小了
- 重新支持了FXAA
- 添加了lang文件映射
- 修改了部分lang键


## alpha0.1.8(2022-07-20)
- 加入了modloader
- 加入了模型解析
- 加入了实体模型
- 实装世界格式
- 删除多余代码
- 加入语言系统
- 加入splashtext
- 添加玩家动画（未解析）
- 主要针对技术更新，因此能说的部分不多 xd

## alpha0.1.5(2022-07-10)
- 加入了input-handler
- 加入了全屏
- 加入双击按键检测
- 删除多余代码
- 添加了screen的输入控制器回调
- 整理screen基类
- 整理debugscreen
- 双击空格切换飞行，而不再使用z
- 加入疾跑

## alpha-0.1.4（2022-07-08）
### 世界
- 目前客户端维度在本地运行
- 添加了loadingticket和loadchunkradius
- blocks相关的操作移动到接口IBlockAccess
- 实体相关的操作移动至接口IEntityAccess
- 实体可以主动加载区块了
- 添加了实体的存储格式
### API
- 添加了mod和plugins的基本定义
- 两者可以获取platforminfo了
- 实体和方块的注册不再能够静态访问了
### 渲染
- 增加了对fxaa的支持
- 增加了垂直同步
- 渲染速度提升54%
- 修复了云的渲染错误
- 修复了天空盒子的混合错误
### 架构
- 现在utils，grass3D，游戏内容（CubeCraft-Contents）不再位于CubeCraft命名空间内
- 游戏内容未来会以mod形式出现，游戏本身将只保留占位符方块
### 网络
- 现在网络系统不再需要发送空包了
- 初始化频道的地址和端口可以被更改了
- 重写了抽象层
- 客户端可以主动发包了
### 其他更新
- 添加了timer和timertask（没有实现）
- 添加了zh_cn.lang和lang文件的解析器
