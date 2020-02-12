package com.iflytek.test.demo;

import com.alibaba.fastjson.JSON;
import com.iflytek.test.demo.dto.HttpClientResult;
import com.iflytek.test.demo.dto.RequestDto;
import com.iflytek.test.demo.util.ByteUtils;
import com.iflytek.test.demo.util.HttpClientUtils;
import com.iflytek.test.demo.util.IdWorker;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class TestTpsApp implements CommandLineRunner {

    //单个文件测试
    private final static String AUDIO_PATH = "E:\\demo\\src\\main\\resources\\123.wav";
    @Override
    public void run(String... args) throws Exception {
        File file = new File(AUDIO_PATH);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bits = readStream(bis);
        doPost(bits);
//        int len = 0;
//        while ((len = bis.read(bits)) != -1) {
//            if (len == 1280) {
//                doPost(bits);
//            } else {
//                byte[] temp_bits = new byte[len];
//                System.arraycopy(bits, 0, temp_bits, 0, len);
//                doPost(temp_bits);
//
//            }
//
//
//        }

    }

    private void doPost(byte [] bytes) {
        String frame = Base64.encodeBase64String(bytes);
        System.out.println(frame);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://localhost:8081/api/v1/business/iat");
        RequestDto dto = new RequestDto();
        dto.setFrame(frame);
        dto.setIdx(1);
        dto.setSampleRate(16);
        dto.setSid(IdWorker.unique());
        dto.setIsLast(1);
        String jsonString = JSON.toJSONString(dto);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        try {
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine().getStatusCode());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
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

}
