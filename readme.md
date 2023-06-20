# 安装说明

- [点击此处安装](https://gitee.com/kdykdykdy/tomado/raw/master/Tomado.apk)

# 项目说明

这是一个前后端分离项目

前端负责实现客户端的相关操作以及UI设计。后端实现具体的业务。两者之间通过接口交互。

前端的代码目前已经有了大致的雏形：

- 代码都在com.whu.tomado中。分别包括：
  - network：负责与后端交互的接口（目前仅包含用于测试网络连接的类）
  - pojo：具体的实体类
  - ui：包括各种与ui相关的类
    - activity：目前只包含了主界面。
    - adaptor：负责列表（数组）这种结构的数据
    - fragment：负责具体的页面
    - viewmodel：用于保存和管理与UI相关的数据的类
- 而设计的ui则在res/layout中，通过xml文件设计ui

目前的前端已经基本实现了包括页面切换，输入等功能。后续扩展可以参考已有代码。

后端代码需要使用springboot架构，目前只是一个空架子，需要结合具体的业务实现。

# 注意事项

- 目前前端通过ipv4地址访问后端代码，ipv4地址在res/values/strings.xml中的server_url中，在具体访问时需要按照实际情况进行修改。
- 在Server/src/main/resources/application.properties中需要将username和password改为本机MySQL的用户名密码，且要创建名为tomado的database，若端口被占用，可修改server.port

- 如果不清楚某些代码的作用，请交给chatGPT，它比我更清楚(doge)