## 1、@PostConstruct 和 @PreConstruct 两个注解被用来修饰非静态得void方法，且这个方法不能抛出异常
### 1、@PostConstruct
 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的inti()方法。被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行
 ~~~
 1、可用来在spring项目启动加载一些全局数据，做一些系统得初始化工作  类似配置 init-method
 ~~~
 ```
 // 公司使用   加载字典数据
 @Component
 public class DictionaryController {

     @Autowired
     private  DictionaryService dictionaryService;
     @Autowired
     private AreaInfoService areaInfoService;

     public static List<Dictionary> dictionaries ;
     public static List<AreaInfo> areaInfo;

    @PostConstruct
    private   void initDictionary(){
         dictionaries= dictionaryService.selectAll();
         areaInfo=areaInfoService.selectAll();
    }

  }
 ```
 ~~~
 2、@PostConstruct修饰init方法，那么spring就会在该bean的依赖关系注入完成之后回调该方法
 ~~~
 ```
  @Resource
     PostConstructController constructController;
     public LoginController(){
         System.out.println("===创建LoginController对象==");
     }
 // @PostConstruct修饰init方法，那么spring就会在该bean的依赖关系注入完成之后回调该方法  在constructController注入完成后，执行init方法
     @PostConstruct
     public void init(){
         System.out.println("==调用LoginController的init方法== " + constructController.count);
     }

//      ===创建LoginController对象==
//      ==创建PostConstructController对象===
//      ==调用PostConstructController的init方法==
//      ==调用LoginController的init方法== 12
 ```
 ### 2、@PreConstruct
  被@PreDestroy修饰的方法会在服务器卸载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的destroy()方法。被@PreDestroy修饰的方法会在destroy()方法之后运行，在Servlet被彻底卸载之前
  ~~~
  类似配置 destory-method
  ~~~


