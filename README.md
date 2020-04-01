# 智能容量规划挑战赛——工程篇

## 代码架构

src/client: 发送方  
src/server: 接收方   
doc: 文档

## 执行

服务端：java Main 1234   （1234为端口号）   
客户端：java -Xms3072m -Xmx3072m Main /liuchen/input_data.txt 39.108.67.176 1234 （/liuchen/input_data.txt为输入文件 39.108.67.176为服务器IP  1234为端口号 ）

## 思路
客户端：单线程读取，多线程计算结果并发送   
服务端：多线程接受数据，调整顺序写入文件

