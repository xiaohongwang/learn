[参考地址](https://mp.weixin.qq.com/s/Zs8en3T8TxCMbxGWHkDwBw)

[StringJoiner](https://mp.weixin.qq.com/s/P9QGM-7IXAcWviSopK_-Hw)

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
- 3、字符串变量 StringBuffer 线程安全 append 使用synchronized 修饰
- 4、字符串变量 StringBuilder 非线程安全
```
StringBuffer 与StringBuilder 继承自同一个父类AbstractStringBuilder，内部封装了一个字符数组，默认大小为16
使用count标识字符数组中使用得字符个数    当字符数组不够时，会进行扩容 -- 扩容过程中，是关注线程安全的地方
```
- StringUtils.join  同样通过StringBuilder来实现  擅长处理字符串数组或者列表的拼接。

### 2、效率比较
```
StringBuilder < StringBuffer < concat <  + < StringUtils.join
```

### jdk8中 StringJoiner   同样通过StringBuilder来实现, 提供了StringJoiner来丰富Stream的用法
~~~
StringJoiner是java.util包中的一个类，用于构造一个由分隔符分隔的字符序列（可选），并且可以从提供的前缀开始并以提供的后缀结尾
~~~
```
 StringJoiner stringJoiner = new StringJoiner(",","[","]");//  delimiter 分隔符  prefix 前缀 suffix 后缀
 
 //流式处理
 String result = list.stream().collect(Collectors.joining(" && ","[","]"));

```

```
日常开发选择：
    1、如果只是简单的字符串拼接，考虑直接使用"+"即可。
   
   2、如果是在for循环中进行字符串拼接，考虑使用StringBuilder和StringBuffer。
   
   3、如果是通过一个集合（如List）进行字符串拼接，则考虑使用StringJoiner。
   
   4、如果是对一组数据进行拼接，则可以考虑将其转换成Stream，并使用StringJoiner处理。
   
   5、类型实现toString,使用Stringjoiner进行处理
```



## 二、字符串创建
```
面试：
Q1：String s = new String("hollis");定义了几个对象。
Q2：如何理解String的intern方法？
```
- 1、方法区中有一块区域是运行时常量池，存储编译期生成的各种字面量和符号引用。














