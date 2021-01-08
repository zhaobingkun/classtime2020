package com.bigdata.hdfs;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HdfsApi {



    static {
        try {
            // 设置 HADOOP_HOME 目录
            System.setProperty("hadoop.home.dir", "E:/download/dllwinutils");
            // 加载库文件
            System.load("E:/download/dllwinutils/hadoop.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }



    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {


        HdfsApi hdfsApi = new HdfsApi();
        //hdfsApi.getFileSystem1();
        //hdfsApi.getFileSystem2();
        //hdfsApi.listFiles();
        //hdfsApi.mkdirs();
        //hdfsApi.uploadFile();
        hdfsApi.downFile();
        //hdfsApi.mergeUplaod();
        //hdfsApi.mergeDown();
    }


    public  void getFileSystem1() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://192.168.148.134:8020");
        FileSystem fileSystem = FileSystem.get(conf);
        System.out.println(fileSystem);

    }

    public  void getFileSystem2() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://192.168.148.131:8020");
        FileSystem fileSystem = FileSystem.newInstance(conf);
        System.out.println(fileSystem);
    }

    public void listFiles() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.148.134:8020"), new Configuration(),"root");

        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"), true);
        while (iterator.hasNext()){
            LocatedFileStatus locatedFileStatus = iterator.next();
            System.out.println(locatedFileStatus);


        }

    }



    public void  mkdirs() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.148.131:8020"),new Configuration(),"root");
       // fileSystem.mkdirs(new Path("/aa/bb/cc"));
        //fileSystem.delete(new Path("/flowsumout"),true);
        fileSystem.close();
    }

    public  void uploadFile() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.148.131:8020"),new Configuration(),"root");
        fileSystem.copyFromLocalFile(new Path("E://奈学培训//大数据开发//test//d.txt"),new Path("/aa/bb"));
    }

    public  void downFile() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.148.139:8020"),new Configuration(),"root");
        //FSDataInputStream inputStream = fileSystem.open(new Path("/aa/bb/cc/hadoop-2.7.7.tar.gz"));
        FSDataInputStream inputStream = fileSystem.open(new Path("/flowout/part-r-00000"));
        //3、获取本地路径的输出流
        FileOutputStream outputStream = new FileOutputStream("D://floww.txt");

        //4、文件的拷贝
        IOUtils.copy(inputStream,outputStream);

        //5、关闭流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        fileSystem.close();

    }


    public void mergeUplaod() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.148.131:8020"),new Configuration(),"root");
        FSDataOutputStream outputStream = fileSystem.create(new Path("/aa/bigfile.txt"));
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());
        FileStatus[] fileStatus = localFileSystem.listStatus(new Path("E://奈学培训//大数据开发//test"));

        for (FileStatus status :fileStatus){
            FSDataInputStream inputStream = localFileSystem.open(status.getPath());
            IOUtils.copy(inputStream,outputStream);
            IOUtils.closeQuietly(inputStream);
        }
        IOUtils.closeQuietly(outputStream);
        localFileSystem.close();
        fileSystem.close();
    }


    public  void mergeDown() throws URISyntaxException, IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.148.131:8020"),new Configuration(),"root");
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());
        FSDataOutputStream outputStream = localFileSystem.create(new Path("E://bigtext.txt"),true);

        RemoteIterator<LocatedFileStatus>  listfiles = fileSystem.listFiles(new Path("/aa/bb"),true);

        while (listfiles.hasNext()){
            LocatedFileStatus status = listfiles.next();
            FSDataInputStream inputStream = fileSystem.open(status.getPath());
            IOUtils.copy(inputStream,outputStream);
            IOUtils.closeQuietly(inputStream);

        }
        IOUtils.closeQuietly(outputStream);
        localFileSystem.close();
        fileSystem.close();



    }

}
