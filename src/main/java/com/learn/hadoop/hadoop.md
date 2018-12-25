# 分布式系统基础框架Hadoop = HDFS + MapReduce
## HDFS 分布式文件系统 海量数据存储
## MapReduce 分布式计算 海量数据分析
## yarn 资源管理调度

### HDFS 分布式文件系统
#### 1、设计思想：分而治之——将大文件、大批量文件，分布式存放在大量独立的服务器上，以便于采取分而治之的方式对海量数据进行运算分析。
#### 2、HDFS一般是用来“一次写入，多次读取”，不适合做实时交互性很强的事情，不适合存储大量小文件
#### 3、基本原理
 - hdfs存储的文件被切分为数据块存储在若干个datanode的服务器上
 - hdfs提供一个统一的目录树来定位hdfs中的文件
 - 每个数据块在hdfs集群保存多个备份(hdfs-site.xml  dfs.replication 默认3份)
#### 4、相关概念
###### block 数据块
   - 文件上传hdfs需进行分块，一般为128MB。不建议修改。块太小：寻址时间占比过高。块太大：Map任务数太少，作业执行速度变慢
###### packet
   - packet是第二大的单位，它是client端向DataNode，或DataNode的PipLine之间传数据的基本单位，默认64KB
###### chunk
   -  chunk是最小的单位，它是client向DataNode，或DataNode的PipLine之间进行数据校验的基本单位，默认512Byte，因为用作校验，故每个chunk需要带有4Byte的校验位。所以实际每个chunk写入packet的大小为516Byte。由此可见真实数据与校验值数据的比值约为128 : 1。（即512 : 4)
###### namenode  元数据节点
  - 1、整个文件系统的管理节点
  - 2、接收用户的操作请求
  - 3、维护了一个hdfs的目录树及hdfs目录结构与文件真实存储位置的映射关系 比如那个数据块存储在那个服务器上的那个路径下， 这些信息由namenode节点存储，namenode节点不存储真正的数据信息
  - 4、namenode运行时，它维护的数据存在内存中,也可以持久化到磁盘上
<img src="./namenode.jpg" />

###### SecondaryNameNode 检查点节点
[参考资料](https://www.cnblogs.com/chenyaling/p/5521464.html)
   - 1、NameNode只有在重启时，edit logs改动才会合并到fsimage文件上。NameNode运行很长时间后，edit logs文件会变大，如何管理这个文件是个挑战
   - 2、NameNode重启会花费很长时间
   - 3、SecondaryNameNode作用: 合并NameNode的edit logs到fsimage文件中
<img src="./Secondary NameNode.jpg" />

###### datanode 数据节点
  - 数据块存储位置：[dfs.datanode.data.dir]/current/BP-2127682388-192.168.236.128-1545038875953/current/finalized
  - 1、真正存储文件的节点
  - 2、客户端或namenode的调度存储检索数据，且同时定期向namenoede发送他们所储存块(block)的列表
### hdfs配置 hdfs-site.xml
   - [root@hadoop01 file]# hadoop fs -appendToFile 1.txt /user/api/test/upload.txt
    2018-12-18 15:25:30,292 WARN hdfs.DataStreamer: DataStreamer Exception
    java.io.IOException: Failed to replace a bad datanode on the existing pipeline due to no more good datanodes being available to try. (Nodes: current=[DatanodeInfoWithStorage[192.168.236.128:9866,DS-e7f40634-2722-45d9-b867-f5efd0cd3cc3,DISK]], original=[DatanodeInfoWithStorage[192.168.236.128:9866,DS-e7f40634-2722-45d9-b867-f5efd0cd3cc3,DISK]]). The current failed datanode replacement policy is DEFAULT, and a client may configure this via 'dfs.client.block.write.replace-datanode-on-failure.policy' in its configuration.
        at org.apache.hadoop.hdfs.DataStreamer.findNewDatanode(DataStreamer.java:1304)
        at org.apache.hadoop.hdfs.DataStreamer.addDatanode2ExistingPipeline(DataStreamer.java:1372)
        at org.apache.hadoop.hdfs.DataStreamer.handleDatanodeReplacement(DataStreamer.java:1598)
        at org.apache.hadoop.hdfs.DataStreamer.setupPipelineInternal(DataStreamer.java:1499)
        at org.apache.hadoop.hdfs.DataStreamer.setupPipelineForAppendOrRecovery(DataStreamer.java:1481)
        at org.apache.hadoop.hdfs.DataStreamer.run(DataStreamer.java:720)
    appendToFile: Failed to replace a bad datanode on the existing pipeline due to no more good datanodes being available to try. (Nodes: current=[DatanodeInfoWithStorage[192.168.236.128:9866,DS-e7f40634-2722-45d9-b867-f5efd0cd3cc3,DISK]], original=[DatanodeInfoWithStorage[192.168.236.128:9866,DS-e7f40634-2722-45d9-b867-f5efd0cd3cc3,DISK]]). The current failed datanode replacement policy is DEFAULT, and a client may configure this via 'dfs.client.block.write.replace-datanode-on-failure.policy' in its configuration.
[解决方案](https://blog.csdn.net/dub_lys/article/details/50585376)
```
<property>
  <name>dfs.client.block.write.replace-datanode-on-failure.policy</name>
  <value>NEVER</value>
</property>
```
### HDFS 的Shell操作
[参考资料](https://www.cnblogs.com/hezhiyao/p/7627060.html)

|命令|解释|
|:---:|:---:|
|hdfs dfs -df -h /|查看磁盘空间||
|hadoop fs -ls [path] or hdfs dfs -ls [path]|查看指定路径的当前目录结构|
|hadoop fs -ls -R [path] or hdfs dfs -ls -R [path]|递归查看指定路径的目录结构|
|hadoop fs -du [path] or hdfs dfs -du [path]|统计目录下各文件大小|
|hadoop fs -du -s [path] or hdfs dfs -du -s [path]|汇总统计目录下文件大小|
|hadoop fs -count [path] or hdfs dfs -count [path]|统计文件(夹)数量|4  1   6 / 分别为文件夹数量 、文件数量、 文件总大小信息|
|hadoop fs -cp [source] [destinct] or hdfs dfs -cp [source] [destinct]|hdfs间文件复制|
|hadoop fs -mv [source] [destinct] or hdfs dfs -mv [source] [destinct]|hdfs间文件移动|
|hadoop fs -rm -r [path] or hdfs dfs -rm -r [path]|删除文件、文件夹|
|hadoop fs -put [local] [hdfs] or hadoop fs -copyFromLocal [local] [hdfs]|上传文件|
|hadoop fs -get [path] or hdfs dfs -get [path] or hadoop fs -copyToLocal [path]|下载文件|
|hadoop fs -getmerge [hdfs] [local]|合并下载hdfs文件内容到本地，但是会覆盖本地文件内容|
|hadoop fs -cat [path]|查看文件内容|
|hadoop fs -mkdir [path]|创建文件夹||
|hadoop fs -setrep [rep] [path]|修改文件副本|
|hadoop fs -stat '%b %n %o %r %Y' [path]|%b %n %o %r %Y 依次表示文件大小、文件名称、块大小、副本数、访问时间|
|hadoop fs -tail -f [path]|查看文件尾部内容 -f文件内容变化，能自动显示||
|hadoop fs -appendToFile [local] [hdfs]|追加信息到文件结尾||
### Java 操作 HDFS文件
[参考资料](https://www.cnblogs.com/yanghuabin/p/7628088.html)
#### 1、获取HDFS文件操作系统
```
    /**
     * 根据配置文件获取HDFS操作对象
     * 本地有配置文件，直接获取配置文件（core-site.xml，hdfs-site.xml）
     */
    static {
        conf = new Configuration();
        // hdfs访问路径
        conf.set("fs.defaultFS", "hdfs://192.168.236.135:9000");
        //本地文件系统 LocalFileSystem     hdfs文件系统 DistributedFileSystem
        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
        try {
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地无需hadoop系统，读取远程配置文件
     */
    static {
        conf = new Configuration();
        String hdfsUser = "root";
        URI hdfsUri = null;
        try {
            hdfsUri = new URI("hdfs://192.168.236.135:9000");
            fs = FileSystem.get(hdfsUri, conf, hdfsUser);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
#### 对于hdfs的操作都是通过FileSystem文件系统操作，通过path对象做操作 new path(filePath)

   |文件操作|api|解释说明|
   |:--------:|:------:|:------:|
   |创建文件夹|fs.mkdirs(path);|即使路径存在，它也可以创建路径 mkdir -p|
   |删除文件|fs.delete(path, true);|true 级联删除  false 删除文件或空文件夹|
   |重命名文件|fs.rename(oldPath, newPath);||
   |文件存在|fs.exists(path)||
   |目录文件|fs.isDirectory(path)||
   |文件|fs.isFile(path)||
   |获取配置信息|Iterator<Map.Entry<String,String>> it = conf.iterator();||
   |文件上传|fs.copyFromLocalFile(localPath,hdfsPath);|方法重载 可设置是否删除本地文件(默认false)，覆盖hdfs文件 (true),设置上传多个文件|
   |文件下载|fs.copyToLocalFile(HDFSPath, localPath);||
   |hdfs间文件复制|FSDataInputStream hdfsIn = fs.open(inPath);  FSDataOutputStream hdfsOut = fs.create(outPath);IOUtils.copyBytes(hdfsIn, hdfsOut,1024*1024,false);||

