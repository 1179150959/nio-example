package com.phei.netty61.dev;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class PerformTestUserInfo {

    public static void main(String[] args) throws IOException {

        UserInfo info = new UserInfo();
        info.buildUserID( 100 ).buildUserName("Welcome to Netty");
        int loop = 1000000;

        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;

        long startTime = System.currentTimeMillis();

        for ( int i = 0; i < loop ; i++ ) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream( bos );
            os.writeObject( info );
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
            bos.close();
        }

        long endTime = System.currentTimeMillis();
        System.out.println( "the jdk serializable cost time is : " + ( endTime - startTime ) + " ms" );
        System.out.println( "-------------------------------------------------------------------------" );

        ByteBuffer buffer = ByteBuffer.allocate( 1024 );
        startTime = System.currentTimeMillis();

        for ( int i = 0 ; i < loop; i++ ) {
            byte[] b = info.codeC();
        }
        endTime = System.currentTimeMillis();
        System.out.println( "the byte array serializable cost time is : " + ( endTime - startTime ) + " ms" );


    }

    /*
    * 业界主流的编码框架：Protobuf、jackson、XStream、Serializable、hession、hession2、hession2压缩、Thrift（facebook）
    * 1、Google 的 Protobuf 特点：
    * 结构化数据存储格式（xml、json）
    * 高效的编解码性能
    * 语言无关、平台无关扩展性好
    * 官方支持 C++ 、java 、python 三种语言
    *
    *thrift 由五部分组成：
    * 语言系统及IDL编译器：负责由用户给定的IDL文件生成相应语言的接口代码
    * TProtocol:RPC的协议层，可选多种不同的对象序列环方式，如json和Binary
    * TTransport:RPC的传输层，同样可以选择不同的传输层实现，如socket、NIO、MemoryBuffer等。
    * TProcessor:作为协议层和用户提供的服务实现之间的纽带负责调用服务实现的接口
    * TServer: 聚合TProtocol、TTransport和 TProcessor 等对象
    *与Protobuf比较类似的是，Thrift通过IDL描述接口和数据结构定义，他支持8中java 进本类型、map、set和list,支持可选和必选定义
    * thrift 比较典型的编码方式：通用的二进制编解码、压缩的二进制编解码、优化的可选字段压缩编解码
    *
    * JBoos Marshalling 是一个java对象序列化API包,修正了jdk自带序列化包的文件有何java 序列化兼容
    *
    * */

}
