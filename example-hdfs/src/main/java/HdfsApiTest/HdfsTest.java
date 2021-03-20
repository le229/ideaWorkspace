package HdfsApiTest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class HdfsTest {

    private static Configuration conf = null;
    private static FileSystem fs = null;

    /**
     * 初始化方法，用户和hdfs集群建立连接
     *
     */
    @Before
    public void connect2HDFS() throws IOException {
        //设置客户端身份，具体权限在hdfs上进行操作
        System.setProperty("HADOOP_USER_NAME","root");
        //创建配置对象实例
        conf = new Configuration();
        //设置操作的文件按系统是hdfs，并指定hdfs操作地址。
        conf.set("fs.defaultFS","hdfs://192.168.237.129:8020");
        //创建FileSystem对象实例
        fs = FileSystem.get(conf);
    }

    /**
     * 创建文件夹
     * @throws IOException
     */
    @Test
    public void mkdir() throws IOException {
        Path path = new Path("/testmakedir");
        if(!fs.exists(path)){
            fs.mkdirs(path);
        }
    }

    /**
     * 上传文件
     * @throws IOException
     */
    @Test
    public void putFile2HDFS() throws IOException {
        //创建本低文件路径
        Path path = new Path("D:\\aaa.txt");
        //创建hdfs上传路径
        Path fsP = new Path("/testmakedir/aaa.txt");
        //上传
        fs.copyFromLocalFile(path,fsP);
    }

    /**
     * 下载文件
     */
    @Test
    public void getFile2Local() throws IOException {
        //源路径：hdfs
        Path src = new Path("/testmakedir/aaa.txt");
        //目标路径
        Path dst = new Path("D:\\bbb.txt");
        //文件下载动作
        fs.copyToLocalFile(src,dst);
    }

    @After
    public void close(){
        //首先判断文件系统实例是否为null，如果不是null，进行关闭动作
        if(fs != null){
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
