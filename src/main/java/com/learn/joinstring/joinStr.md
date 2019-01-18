[参考地址](https://mp.weixin.qq.com/s/Zs8en3T8TxCMbxGWHkDwBw)
## 字符串 String是Java中一个不可变的类
```
阿里巴巴Java开发手册建议：循环体内，字符串的连接方式，使用 StringBuilder 的 append 方法进行扩展。而不要使用+
```
## 一、字符串拼接
### 1、字符串拼接方式
- 1、使用 + 拼接 ：字符串常量在拼接过程中，是将String转成了StringBuilder后，使用其append方法进行处理的。
```
    String name = "xiaohong";
    String fullInfo = "name:" + name;

    反编译：
        String name = "xiaohong";
        (new StringBuilder()).append("name:").append(name).toString();
```
~~~
1、不建议在循环体中使用 + 进行字符串拼接 每次循环会 new StringBuilder对象，浪费内存资源
~~~
- 2、String 类中方法 concat
- 3、字符串变量 StringBuffer 线程安全 append使用synchronized 修饰
- 4、字符串变量 StringBuilder 非线程安全
```
StringBuffer 与StringBuilder 继承自同一个父类AbstractStringBuilder，内部封装了一个字符数组，默认大小为16
使用count标识字符数组中使用得字符个数    当字符数组不够时，会进行扩容
```
- StringUtils.join  同样通过StringBuilder来实现 擅长处理字符串数组或者列表的拼接。

### 2、效率比较
```
StringBuilder < StringBuffer < concat <  + < StringUtils.join
```
## 二、字符串创建
```
面试：
Q1：String s = new String("hollis");定义了几个对象。
Q2：如何理解String的intern方法？
```
### 1、方法区中有一块区域是运行时常量池，存储编译期生成的各种字面量和符号引用。


