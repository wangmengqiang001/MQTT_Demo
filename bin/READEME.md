# MQTT 
## MQTT协议
- MQTT（Message Queuing Telemetry Transport，消息队列遥测传输协议），是一种基于发布/订阅（publish/subscribe）模式的“轻量级”通讯协议，该协议构建于TCP/IP协议上，由IBM在1999年发布。
  MQTT最大优点在于，用极少的代码和有限的带宽，为连接远程设备提供实时可靠的消息服务。
  作为一种低开销、低带宽占用的即时通讯协议，使其在物联网、小型设备、移动应用等方面有较广泛的应用。
## MQTT协议特点
- MQTT是一个基于客户端-服务器的消息发布/订阅传输协议。
- MQTT协议是轻量、简单、开放和易于实现的，这些特点使它适用范围非常广泛。在很多情况下，包括受限的环境中，如：机器与机器（M2M）通信和物联网（IoT）。
## MQTT Client
- publisher 和 subscriber 都属于 MQTT Client，之所以有发布者和订阅者这个概念，其实是一种相对的概念，就是指当前客户端是在发布消息还是在接收消息，发布和订阅的功能也可以由同一个 MQTT Client 实现。MQTT 客户端是运行 MQTT 库并通过网络连接到 MQTT 代理的任何设备（从微控制器到成熟的服务器）。例如，MQTT 客户端可以是一个非常小的、资源受限的设备，它通过无线网络进行连接并具有一个最低限度的库。基本上，任何使用 TCP/IP 协议使用 MQTT 设备的都可以称之为 MQTT Client。MQTT 协议的客户端实现非常简单直接，易于实施是 MQTT 非常适合小型设备的原因之一。MQTT 客户端库可用于多种编程语言。例如，Android、Arduino、C、C++、C#、Go、iOS、Java、JavaScript 和 .NET。        
## MQTT Broker
-  与 MQTT Client 对应的就是 MQTT Broker，Broker 是任何发布/订阅协议的核心，根据实现的不同，代理可以处理多达数百万连接的 MQTT Client。 Broker 负责接收所有消息，过滤消息，确定是哪个Client 订阅了每条消息，并将消息发送给对应的 Client，Broker 还负责保存会话数据，这些数据包括订阅的和错过的消息。Broker 还负责客户端的身份验证和授权。                                                                                                                     
## MQTT Connection
- MQTT 协议基于 TCP/IP。客户端和代理都需要有一个 TCP/IP 协议支持。 MQTT 连接始终位于一个客户端和代理之间。客户端从不直接相互连接。要发起连接，客户端向代理发送 CONNECT 消息。代理使用 CONNACK 消息和状态代码进行响应。建立连接后，代理将保持打开状态，直到客户端发送断开连接命令或连接中断。