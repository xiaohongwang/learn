# Ehcache 参数配置

 |参数|说明|
 |:---:|:---:|
 |maxElementsInMemory|缓存中允许创建的最大对象数|
 |eternal|缓存中对象是否为永久的. true 对象用不过期 如果是，钝化时间\超时时间设置将被忽略|
 |timeToLiveSeconds|单位 秒 生存时间。元素从构建到消亡的最大时间间隔值，如果该值是0就意味着元素可以停顿无穷长的时间|
 |timeToIdleSeconds|单位 秒 钝化时间，也就是在一个元素消亡之前，两次访问时间的最大时间间隔值。如果该值是 0 就意味着元素可以停顿无穷长的时间|
 ||同时设置 生存、钝化时间时，时间较短者生效||