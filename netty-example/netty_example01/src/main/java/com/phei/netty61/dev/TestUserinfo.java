package com.phei.netty61.dev;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//java 序列化代码编码测试
public class TestUserinfo {

    /*
    * 评判一个编码框架优劣因素
    *
    * 是否支持跨语言，支持的语言种类是否丰富
    * 编码后的码流大小
    * 编解码的性能
    * 类库是否小巧，API实用是否方便
    * 使用者需要手工开发的工作量和难度
    *
    * */
    public static void main(String[] args) throws IOException {

        UserInfo info = new UserInfo();
        info.buildUserID( 100 ).buildUserName("Welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream( bos );

        os.writeObject( info );
        os.flush();
        os.close();

        byte[] b = bos.toByteArray();

        System.out.println( "the jdk serializable length is " + b.length );
        b.clone();

        System.out.println( "------------------------------------------" );

        System.out.println( "the byte array serializable length is : " + info.codeC().length );


    }

}
