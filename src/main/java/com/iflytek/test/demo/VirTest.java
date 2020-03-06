package com.iflytek.test.demo;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class VirTest extends WebSocketClient {

    private Logger logger = LoggerFactory.getLogger(VirTest.class);
    private final static String AUDIO_PATH1= "D:\\16k.wav";
    private final static String AUDIO_PATH2= "D:\\8k16bit_long.wav";
    private static final String END_IDENTITY = "{\"end\": true}";

    public VirTest (String url) throws URISyntaxException {
        super(new URI(url));
    }
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
       logger.info("连接websocket");
    }

    @Override
    public void onMessage(String s) {
        logger.info("等待返回信息。。。");
        logger.info("onMessage{}",s);
        logger.info("已返回信息{}",s);


    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("onClose{}",s);
    }

    @Override
    public void onError(Exception e) {
        logger.info("onError{}",e.getMessage());
    }
    public static void main(String[] args) {

        try {
            VirTest client = new VirTest("ws://172.31.161.81:8088/ws");
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                System.out.println("还没有打开");
            }
            System.out.println("建立websocket连接");
            doPost(client,AUDIO_PATH1);
            doPost(client,AUDIO_PATH2);
            //doPost(client,AUDIO_PATH2);
            }catch (Exception e){

            }
    }

    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static void doPost(VirTest client,String path){

        try {
            File file = new File(path);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] bits = new byte[5120];
            int len = 0;
            while ((len = bis.read(bits)) != -1) {
                if (len == 5120) {
                    client.send(bits);
                } else {
                    byte[] temp_bits = new byte[len];
                    System.arraycopy(bits, 0, temp_bits, 0, len);
                    client.send(temp_bits);

                }
            }
            client.send(END_IDENTITY.getBytes());
    }catch (Exception e){

        }


}
}


