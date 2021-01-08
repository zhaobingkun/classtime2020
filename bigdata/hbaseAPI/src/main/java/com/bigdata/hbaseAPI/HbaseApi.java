package com.bigdata.hbaseAPI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseApi {
/*
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

 */

    //获取Configuration对象
    public static Configuration conf;
    static{
        //使用HBaseConfiguration的单例方法实例化
        //HBaseConfiguration：封装了hbase集群所有的配置信息（最终代码运行所需要的各种环境）
        conf = HBaseConfiguration.create();
        //此处不能写ip，会报错。必须写hostname ，win本地调试还是要改一下hosts文件
        conf.set("hbase.zookeeper.quorum", "hadoop31:2181,hadoop32:2181,hadoop33:2181");
    }
    public static void main(String[] args) throws Exception {
        HbaseApi hbaseApi = new HbaseApi();
        //boolean isExit = hbaseApi.isTableExist("student");
        //System.out.println("isExit"+isExit);
        //hbaseApi.createTable("children","id","name","sex");
        //hbaseApi.createTable("children_01","children_info");
        //hbaseApi.dropTable("student");

        //hbaseApi.addData("children_01", "002", "children_info", "name", "xxx");//注意增加数据，存在就是修改，不存在就是增加
        //hbaseApi.addData("children_01", "002", "children_info", "id", "20");
        //hbaseApi.addData("children_01", "002", "children_info", "sex", "女");
        //hbaseApi.getAllData("children_01");
        //hbaseApi.getRowQualifierData("children_01", "001", "children_info", "name");
        //hbaseApi.getRowData("children_01","001");
        //hbaseApi.deleteRowsData("children_01","001","002");
        hbaseApi.getAllData("children_01");

    }


    //判断表是否存在
    public boolean isTableExist(String tableName) {
        //创建HBaseAdmin对象
        try {
            Connection connection = ConnectionFactory.createConnection(conf);
            HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
            System.out.println(admin.tableExists("student"));
            return admin.tableExists(tableName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //创建表
    public void createTable(String tableName,String... columnFamily) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        if (admin.tableExists(tableName)){
            System.out.println("table： " + tableName + "表已经存在！");
        }
        else{


        //******* 创建表属性对象,表名需要转字节类型 *******
        //HTableDescriptor：所有列簇的信息（一到多个HColumnDescriptor）
        HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(tableName));
        //创建多个列簇
        for(String cf : columnFamily){
            descriptor.addFamily(new HColumnDescriptor(cf));

        }
        //创建表
        admin.createTable(descriptor);
        System.out.println("table： " + tableName + "创建成功！");
        }
    }
    //删除表 先disable 再drop（delete）
    public  void dropTable(String tableName) throws Exception{
        Connection connection = ConnectionFactory.createConnection(conf);
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        if(admin.tableExists(tableName)) {
            //先disable
            admin.disableTable(tableName);
            //再delete
            admin.deleteTable(tableName);
        }
        else{
            System.out.println("表不存在");
        }
    }
    //向表中插入数据
    public  void addData(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException{
        //创建HTable对象
        //HTable：封装了整个表的所有的信息（表名，列簇的信息），提供了操作该表数据所有的业务方法。
        HTable hTable = new HTable(conf, tableName);
        //向表中插入数据
        Put put = new Put(Bytes.toBytes(rowKey));
        //向Put对象中组装数据
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        hTable.put(put);
        hTable.close();
        System.out.println("插入数据成功");
    }

    //获取所有数据，也就是获取所有行
    public  void getAllData(String tableName) throws IOException{
        //创建HTable对象
        //HTable：封装了整个表的所有的信息（表名，列簇的信息），提供了操作该表数据所有的业务方法。
        HTable hTable = new HTable(conf, tableName);

        //得到用于扫描region的对象scan
        //Scan： 封装查询信息，和get有一点不同，Scan可以设置Filter
        Scan scan = new Scan();
        //使用HTable得到resultcanner实现类的对象
        ResultScanner resultScanner = hTable.getScanner(scan);

        for(Result result : resultScanner){
            //Cell：封装了Column的所有的信息：Rowkey、column qualifier、value、时间戳
            Cell[] cells = result.rawCells();
            for(Cell cell : cells){
                System.out.println("行信息: " + Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.println("列簇: " + Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("列: " + Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("值: " + Bytes.toString(CellUtil.cloneValue(cell)));
                System.out.println();
            }
        }

    }



    //获取某行指定的数据，比如指定某个列簇的某个列限定符
    public  void getRowQualifierData(String tableName, String rowKey, String family, String qualifier) throws IOException{
        //创建HTable对象
        //HTable：封装了整个表的所有的信息（表名，列簇的信息），提供了操作该表数据所有的业务方法。
        HTable htable = new HTable(conf,tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier));
          Result result = htable.get(get);
        //Cell：封装了Column的所有的信息：Rowkey、column qualifier、value、时间戳
          for(Cell cell:result.rawCells()){
              System.out.println(Bytes.toString(result.getRow()));
              System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
              System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
              System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
          }


    }

    public  void getRowData(String tableName, String rowKey) throws IOException{
        //创建HTable对象
        //HTable：封装了整个表的所有的信息（表名，列簇的信息），提供了操作该表数据所有的业务方法。
        HTable hTable = new HTable(conf,tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = hTable.get(get);
        //循环获取查询到的信息
        //Cell：封装了Column的所有的信息：Rowkey、column qualifier、value、时间戳
        for(Cell cell:result.rawCells()){
            System.out.println(Bytes.toString(result.getRow()));
            System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println(cell.getTimestamp());
        }





    }

    //删除数据
    public  void deleteRowsData(String tableName, String... rows) throws IOException{
        //创建HTable对象
        //HTable：封装了整个表的所有的信息（表名，列簇的信息），提供了操作该表数据所有的业务方法。
        HTable hTable = new HTable(conf,tableName);
        List<Delete> deleteList =  new ArrayList<Delete>();
        //循环添加要删除的行
        for(String row:rows){
            Delete delete = new Delete(Bytes.toBytes(row));
            deleteList.add(delete);
        }
        hTable.delete(deleteList);
        hTable.close();
        System.out.println("删除完成");
    }

}
