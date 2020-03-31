# 智能容量规划挑战赛——工程篇

## 代码架构

src/client: 发送方  
src/server: 接收方

## 版本一

发送端：读取一行，计算结果，发送一行   
接收端：接受一行，写入文件

耗时：296秒  
输出文件大小：173.34 MB

## 版本二
发送端：顺序读取，多线程计算结果并发送  
接收端：单线程接受

耗时：265秒  
输出文件大小：173.34 MB


## 版本三（toDO）
发送端：顺序读取，多线程计算结果并发送  
接收端：多线程接受，合并结果， 写入文件

