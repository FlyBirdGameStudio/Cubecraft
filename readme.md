# Cubecraft
一个简单的minecraft高仿  
by [FlyBirdStudio](http://sunrisestudio.top)

### 简介：
Cubecraft是一个简单的Minecraft高仿，使用java，lwjgl开发。不同于传统作品的还原度和游戏性，这个游戏将更加注重框架化和高扩展性。其存在的目的是用于更好的制作Minecraft高仿或其他而创同人作品，同时也作为我们的技术平台。如果能做到数据兼容的话，这个平台将在以后为mc社区的内容创作服务（希望还是要有的）
    
由于我们对世界结构过度的重写（例如64位整数之类的奇怪特性），cubecraft暂时无法同minecraft进行任何数据兼容。我们将来可能会（尽力）进行对minecraft的数据兼容（难如登天啊淦）。（另外cubecraft的架构已经基本稳定，如果有人想写个数据兼容的mod话，欢迎）

### 使用：
##### 自己编译
- 直接fork项目，将文件作为gradle项目导入至你的IDE。
- 注意java版本要选17或以上
- 然后你就可以开始自己改造
##### 客户端游玩：
- 下载客户端jar文件，随便放在哪里（注意：这个文件夹会自动识别为游戏文件夹）
- 写个随意的cmd脚本例如java -jar
- 启动游戏，好好玩吧
##### 服务端部署（coming s∞n）：
- 下载服务端jar文件，随便放在哪里（注意：这个文件夹会自动识别为游戏文件夹）
- 写个随意的cmd脚本例如java -jar
- 启动服务器，默认端口为11451
##### mod开发（coming s∞n）：
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
- Netty
- Oshi
- s1f4j
- JNA

### introduction
Cubecraft is a simple Minecraft high imitation developed with java and lwjgl. Different from the reducibility and gameplay of traditional works, this game will pay more attention to framing and high scalability. The purpose of its existence is to better produce Minecraft high imitation or other works of the same people, and it also serves as our technical platform. If data compatibility can be achieved, this platform will serve for the content creation of the mc community in the future (I hope it will still exist)


Due to our excessive rewriting of the world structure (such as 64 bit integers and other strange features), cubecraft is temporarily unable to make any data compatibility with minecraft. In the future, we may (as far as possible) implement data compatibility with minecraft. (In addition, the architecture of cubecraft has been basically stable. If anyone wants to write a data compatible mod, welcome.)

### Use:
##### Compile yourself
-Direct fork project, and import the file into your IDE as a gradle project.
-Note that the java version should be 17 or above
-Then you can start your own transformation
##### Client play:
-Download the client jar file and put it anywhere (Note: this folder will be automatically identified as the game folder)
-Write a random cmd script, such as "java -jar"
-Start the game, have fun
##### Server side deployment (coming s∞n):
-Download the server jar file and put it anywhere (note: this folder will be automatically identified as the game folder)
-Write a random cmd script, such as java jar
-Start the server. The default port is 11451
##### Mod development (coming s∞n):
-Use the cubecraft client and all dependent libraries as project references
-Note: mod can be installed at the client/server side at the same time.
-See wiki for more information

### Contributors:
- GrassBlock2022：Main development
- CubeVlmu：aided design
- tuboshu，BDS： game test
- Zegute：technical help

### Special thanks:
MojangStudio and Microsoft did not sue us:)

At the same time, thanks to the makers of third-party java libraries (sorry for not covering all of them):
- Lwjgl
- Netty
- Oshi
- s1f4j
- JNA




