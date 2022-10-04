# Cubecraft
一个简单的minecraft高仿  
by [FlyBirdStudio](http://sunrisestudio.top)

### 简介：
Cubecraft是一个简单的Minecraft高仿，使用java，lwjgl开发。不同于传统作品的还原度和游戏性，这个游戏将更加注重框架化和高扩展性。其存在的目的是用于更好的制作Minecraft高仿或其他而创同人作品，同时也作为我们的技术平台。如果能做到数据兼容的话，这个平台将在以后为mc社区的内容创作服务（希望还是要有的）
   
我们欢迎各位使用我们的框架来开发各类mod，或者是魔改全部框架来重写一个游戏（如果有的话，三生有幸），但请注意：请不要用我们的内容（仅包含源代码）进行商业行为（例如倒卖），（但很显然mod，扩展包和重写过的作品不在此列）。如果因为使用我们的框架二创导致出现知识产权问题，我们概不（也没能力）负责。
    
由于我们对世界结构过度的重写（例如三维地图，64位整数之类的奇怪特性），cubecraft暂时无法同minecraft进行任何数据兼容。我们将来可能会（尽力）进行对minecraft的数据兼容（难如登天啊淦）。（另外cubecraft的架构已经基本稳定，如果有人想写个数据兼容的mod话，欢迎）

### 架构：
游戏分为客户端和服务端两部分，其共享cubecraft包下的其他子包。
本地游戏时启动内置服务端。
##### 客户端：
- client_main：渲染，输入
- client_web：客户端udp网络
- client_version_check：任务线程，版本检查
- client_chunk：线程池，用于地形区块绘制加速
##### 服务端：
- server_main：调度
- server_dim：线程池，更新各个维度逻辑
- server_io：服务端io
- server_net：服务端udp网络
- server_light：线程池，服务端光照引擎

### 使用：
##### 客户端游玩：
- 下载客户端jar文件，随便放在哪里（注意：这个文件夹会自动识别为游戏文件夹）
- 写个随意的cmd脚本例如java -jar
- 启动游戏，好好玩吧
##### 服务端部署：
- 下载服务端jar文件，随便放在哪里（注意：这个文件夹会自动识别为游戏文件夹）
- 写个随意的cmd脚本例如java -jar
- 启动服务器，默认端口为25568
##### mod开发：
- 将cubecraft客户端和全部的依赖库作为项目的引用
- 注意：mod可在客户端/服务端同时安装。
- 更多信息请看wiki

### 贡献者：
- 草方块 主要开发
- CubeVlmu 辅助设计
- 土拨鼠，bds 游戏测试
- zegute 技术帮助

### 特别感谢：
MojangStudio 和 Microsoft 没有起诉我们 ：）

同时，感谢第三方java库的制作者们（并未包含全部，抱歉）：
- Lwjgl
- Netty（弃用）
- Oshi
- s1f4j
- JNA

### 游戏效果图

![效果图](resources/resource/textures/gui/bg.png)

