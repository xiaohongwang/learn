## 1、SpringBoot测试
[参考资料](https://blog.csdn.net/sz85850597/article/details/80427408)
~~~
maven依赖
  <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-test</artifactId>
          <scope>test</scope>
      </dependency>
~~~
## 1、Controller 单元测试
- 可以不必启动工程就能测试这些接口
- MockMvc实现了对Http请求的模拟，能够直接使用网络的形式，转换到Controller的调用，这样可以使得测试速度快、不依赖网络环境，而且提供了一套验证的工具，这样可以使得请求的验证统一而且很方便