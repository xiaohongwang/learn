## SpringMVC注解
[参考资料](https://my.oschina.net/sluggarddd/blog/678603?fromerr=XhvpvVTi)
[参考资料](https://www.cnblogs.com/liaochong/p/spring_modelattribute.html#3546557)
### 1、 ModelAttribute
  - 运用在方法上 会在每一个@RequestMapping标注的方法前执行，如果有返回值，则自动将该返回值加入到ModelMap中 可节省controller层代码
  ```
  需要获取登陆用户信息  存在线程安全问题（文中有验证）
  protected User user;

  @ModelAttribute
  private void getUser(){
      user =  request.getSession() == null ? null : (User) request.getSession().getAttribute("user");
  }
  ```
  - 运用在方法上  如果有返回值，则自动将该返回值加入到ModelMap中( @ModelAttribute("data") 也可指定key值)
  ```
      protected Integer data;

      @ModelAttribute
      public Integer setData(){
          data = 1;
          return data;
      }

       @RequestMapping("/loginout")
          public String loginOut(Model model){//  key  integer  value 1
              logger.info("loginout");
              return "loginout success";
          }
  ```
  - 运用在方法参数上 从model获取数据
  ```
  @RequestMapping("/loginout")
  //    public String loginOut(Model model){
      public String loginOut(@ModelAttribute("data") Integer data){
          logger.info("loginout === {}",data);
          return "loginout success";
      }
  ```



## 2、SpringMVC在Controller层中注入request 线程安全问题  ）
- 方式一  方法中声明  无线程安全问题
```
     @RequestMapping(value = "/first", method = RequestMethod.POST)
     public String testA(@RequestBody String jsonData,HttpServletRequest request){
         logger.info(request.hashCode() + " === " + jsonData + "  header数据" + " === " + request.getHeader("num") );
         return "first";
     }
```
~~~
测试数据如下：
1155480936 === {"num":"93"}  header数据 === 93
375647002 === {"num":"97"}  header数据 === 97
522989211 === {"num":"1"}  header数据 === 1
2132373301 === {"num":"95"}  header数据 === 95
432825987 === {"num":"94"}  header数据 === 94
562289953 === {"num":"88"}  header数据 === 88
1377666931 === {"num":"92"}  header数据 === 92
1690617568 === {"num":"96"}  header数据 === 96
1702494177 === {"num":"90"}  header数据 === 90
1674603846 === {"num":"99"}  header数据 === 99
250425047 === {"num":"98"}  header数据 === 98
1755019434 === {"num":"89"}  header数据 === 89
605126606 === {"num":"87"}  header数据 === 87
564879582 === {"num":"91"}  header数据 === 91
2039904467 === {"num":"86"}  header数据 === 86
~~~
- 方式二 在BaseController 中使用 @Resource 注入request对象  无线程安全问题
```
    @Resource
    protected HttpServletRequest request;
```
~~~
测试数据如下：
    1252713736 == {"num":"98"}  header数据 == 98
    1252713736 == {"num":"53"}  header数据 == 53

   对于同一个controller,无论使用多少并发，request的hashcode始终是相同的  --    具体实现没有深挖
~~~
- 方式三  在BaseController 中使用 @ModelAttribute设置request 存在线程安全问题
```
//    @Resource
    protected HttpServletRequest request;

    @ModelAttribute
    private void setRequest(HttpServletRequest request){
        this.request = request;
    }
```
~~~
测试数据如下：
    2137098753 == {"num":"1"}  header数据 == 19
    2137098753 == {"num":"12"}  header数据 == 19
    2137098753 == {"num":"7"}  header数据 == 19
    2137098753 == {"num":"19"}  header数据 == 19
    2137098753 == {"num":"5"}  header数据 == 19
    2137098753 == {"num":"17"}  header数据 == 19
    2137098753 == {"num":"2"}  header数据 == 19
    2137098753 == {"num":"11"}  header数据 == 19
    2137098753 == {"num":"8"}  header数据 == 19
    2137098753 == {"num":"3"}  header数据 == 19
    2137098753 == {"num":"6"}  header数据 == 19
    2137098753 == {"num":"0"}  header数据 == 19
    2137098753 == {"num":"4"}  header数据 == 19
    2137098753 == {"num":"9"}  header数据 == 19
    184063398 == {"num":"14"}  header数据 == 14
    2129631598 == {"num":"15"}  header数据 == 15
    1770714364 == {"num":"10"}  header数据 == 10
    50692168 == {"num":"21"}  header数据 == 21
    50692168 == {"num":"20"}  header数据 == 21
    1702494177 == {"num":"22"}  header数据 == 22
    564879582 == {"num":"23"}  header数据 == 23
~~~
### 3、存在线程安全问题
```
    需要获取登陆用户信息   存在线程安全问题
    BaseController

    @Resource
    protected HttpServletRequest request;
    protected User user;
    @ModelAttribute
    private void getUser(){
        user =  request.getSession() == null ? null : (User) request.getSession().getAttribute("user");
    }

    LoginController

    @RequestMapping(value = "/login", method = RequestMethod.POST)
        public String login(){
           logger.info("login === request的头数据 {} ，用户数据信息{}", request.getHeader("num"), user);
           return "login success";
        }

    LoginControllerTest

     @Test
        public void login() throws Exception {
    //        mvc.perform(MockMvcRequestBuilders.post("/login")
    //                .contentType(MediaType.APPLICATION_JSON_UTF8)
    //                .content("1".getBytes()).header("num", 1).sessionAttr("user",1))
    //                .andExpect(MockMvcResultMatchers.status().isOk())
    //                .andDo(MockMvcResultHandlers.print());

            CountDownLatch countDownLatch = new CountDownLatch(100);
            for (int i = 0; i < 100; i++){
                Task task = new Task(i,"/login", countDownLatch, mvc);
                new Thread(task).start();
            }
    //       在此阻塞，知道计数减为0
            countDownLatch.await();
        }
```
~~~
测试数据：
login === request的头数据 50 ，用户数据信息96
login === request的头数据 2 ，用户数据信息96
login === request的头数据 98 ，用户数据信息96
login === request的头数据 28 ，用户数据信息96
~~~
~~~
采用ThreadLocal解决：
private static final ThreadLocal<Integer> localUser = new ThreadLocal<>();
    @ModelAttribute
    private void setUser(){
        logger.info("获取用户信息");
        localUser.set(request.getSession() == null ? null : (Integer) request.getSession().getAttribute("user"));
//        user = request.getSession() == null ? null : (Integer) request.getSession().getAttribute("user");
    }
    protected Integer getUser(){
        return localUser.get();
    }
~~~