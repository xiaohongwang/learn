package com.learn.hadoop.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DFSInputStream;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class HdfsLearn {

    private static Configuration conf;
    private static FileSystem fs;

    private static Set<String> files = new HashSet<>();

    /**
     * 根据配置文件获取HDFS操作对象
     * 本地有配置文件，直接获取配置文件（core-site.xml，hdfs-site.xml）
     */
//    static {
//        conf = new Configuration();
//        // hdfs访问路径  namenode节点
//        conf.set("fs.defaultFS", "hdfs://192.168.236.128:9000");
//        //本地文件系统 LocalFileSystem     hdfs文件系统 DistributedFileSystem
//        conf.set("fs.hdfs.impl","org.apache.hadoop.hdfs.DistributedFileSystem");
//        try {
//            fs = FileSystem.get(conf);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 本地无需hadoop系统，读取远程配置文件
     */
    static {
        conf = new Configuration();
        String hdfsUser = "aaa";
        URI hdfsUri = null;
        try {
            hdfsUri = new URI("hdfs://192.168.236.128:9000");
            fs = FileSystem.get(hdfsUri, conf, hdfsUser);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建文件夹
     * @param filePath
     */
    public static void createFile(String filePath){
        Path path = new Path(filePath);
        try {
            boolean flag =  fs.exists(path);
            if (!flag){
                fs.mkdirs(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件 文件夹
     * @param filePath
     * @param flag
     */
    public static void delete(String filePath, boolean flag){
        Path path = new Path(filePath);
        try {
            fs.delete(path, flag);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件夹递归遍历
     * 通常使用HDFS文件系统的listStatus(path)来获取改定路径的子路径
     * 注意：
     *  1. 有些文件夹是空的,仅做是否为文件的判断会有问题，同时判断文件长度
     * @param filePath
     */
    public static void recursiveHdfsPath(Path filePath){
        try {
            FileStatus[] fileStatuses = fs.listStatus(filePath);
            if (fileStatuses.length == 0){
                files.add(filePath.toString());
            }else {
                Arrays.stream(fileStatuses).forEach(fileStatus -> {
                    if (fileStatus.isFile()){
                        files.add(fileStatus.getPath().toString());
                    }else {
                        recursiveHdfsPath(fileStatus.getPath());
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重命名
     * @param oldFilePath
     * @param newFilePath
     */
    public static void rename(String oldFilePath, String newFilePath){
        Path oldPath = new Path(oldFilePath);
        Path newPath = new Path(newFilePath);
        try {
            fs.rename(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件信息判断
     * @param filePath
     */
    public static void checkFile(String filePath){
        Path path = new Path(filePath);
        try {
            System.out.println("===exists===" + fs.exists(path));
            System.out.println("===isDirectory===" +  fs.isDirectory(path));
            System.out.println("===isFile===" + fs.isFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取配置信息
     */
    public static void showAllConf(){
        Iterator<Map.Entry<String,String>> it = conf.iterator();
        while(it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            System.out.println(entry.getKey()+"=" + entry.getValue());
        }
    }

    /**
     * 文件下载
     *  注意下载的路径的最后一个地址是下载的文件名
     *  copyToLocalFile(Path local,Path hdfs)
     *  下载命令中的参数是没有任何布尔值的，如果添加了布尔，意味着这是moveToLocalFile()
     * @param hdfsFilePath
     * @param localFilePath
     */
    public static void getFileFromHDFS(String hdfsFilePath, String localFilePath){
        Path HDFSPath = new Path(hdfsFilePath);
        Path localPath = new Path(localFilePath);
        try {
            fs.copyToLocalFile(HDFSPath, localPath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件的上传
     *   注意事项同文件的下载
     *   注意如果上传的路径不存在会自动创建
     *   如果存在同名的文件，会覆盖
     * @param hdfsFilePath
     * @param localFilePath
     */
    public static void uploadFileToHDFS(String hdfsFilePath, String localFilePath){
        boolean pathExists = false;
        // 如果上传的路径不存在会创建
        // 如果该路径文件已存在，就会覆盖
        Path localPath = new Path(localFilePath);
        Path hdfsPath = new Path(hdfsFilePath);
        try {
            fs.copyFromLocalFile(localPath,hdfsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * hdfs之间文件的复制
     * 使用FSDataInputStream来打开文件open(Path p)
     * 使用FSDataOutputStream开创建写到的路径create(Path p)
     * 使用 IOUtils.copyBytes(FSDataInputStream,FSDataOutputStream,int buffer,Boolean isClose)来进行具体的读写
     * 说明：
     *  1.java中使用缓冲区来加速读取文件，这里也使用了缓冲区，但是只要指定缓冲区大小即可，不必单独设置一个新的数组来接受
     *  2.最后一个布尔值表示是否使用完后关闭读写流。通常是false，如果不手动关会报错的
     */
    public static void copyFileBetweenHDFS(String inFilePath, String outFilePath){
        Path inPath = new Path(inFilePath);
        Path outPath = new Path(outFilePath);
        FSDataInputStream hdfsIn = null;
        FSDataOutputStream hdfsOut = null;

        try {
             hdfsIn = fs.open(inPath);
             hdfsOut = fs.create(outPath);
            IOUtils.copyBytes(hdfsIn, hdfsOut,1024*1024,false);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                hdfsOut.close();
                hdfsIn.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public static void main(String[] args) {

        String hdfsFilePath = "/user/api/test/upload";
        String localFilePath = "E:\\hdfsdata\\upload.txt";
//        uploadFileToHDFS(hdfsFilePath, localFilePath);
        getFileFromHDFS(hdfsFilePath, localFilePath);
//        copyFileBetweenHDFS(hdfsFilePath, "/user/api/upload.txt");
//        showAllConf();

        String filePath = "/user/api/aaa";
//        checkFile(filePath);

       createFile(filePath);
//        delete(filePath, false);
        String oldFilePath = "/user/api/data/";
        String newFilePath = "/user/api/learn/";
//        rename(oldFilePath, newFilePath);
        Path path = new Path(filePath);
//        recursiveHdfsPath(path);

//        files.stream().forEach(file ->{
//            System.out.println(file);
//        });
    }
}
