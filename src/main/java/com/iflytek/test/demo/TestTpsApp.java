package com.iflytek.test.demo;

import com.alibaba.fastjson.JSON;
import com.iflytek.test.demo.dto.RequestDto;
import com.iflytek.test.demo.util.CommUtils;
import com.iflytek.test.demo.util.IdWorker;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
@Component
public class TestTpsApp implements CommandLineRunner {

    //单个文件测试
    private final static String AUDIO_PATH = "D:\\16k.wav";
    private  CookieStore cookieStore;
    private  HttpClientContext context;
    @Override
    public void run(String... args) throws Exception {
        File file = new File(AUDIO_PATH);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        String id = IdWorker.unique();
        byte[] bits = new byte[12800];
        int len = 0;
        while ((len = bis.read(bits)) != -1) {
            if (len == 12800) {
                doPost(bits,0,id);
            } else {
                byte[] temp_bits = new byte[len];
                System.arraycopy(bits, 0, temp_bits, 0, len);
                doPost(temp_bits,0,id);

            }


        }
        byte[] end = new byte[0];
        doPost(end,1,id);

    }

    private void doPost(byte [] bytes, int islast,String id) {
        String frame = Base64.encodeBase64String(bytes);
        System.out.println(frame);
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://172.18.82.4:8082/api/v1/business/iat");
        RequestDto dto = new RequestDto();
        dto.setFrame(frame);
        dto.setIdx(1);
        dto.setSampleRate(16);
        dto.setSid(id);
        dto.setIslast(islast);
        String jsonString = JSON.toJSONString(dto);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        if(null != CommUtils.sid.get(id)){
            String route = (String) CommUtils.sid.get(id);
            httpPost.addHeader(new BasicHeader("Cookie", route));
        }
        try {
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            if( null == CommUtils.sid.get(id)){
                Header[] headers=  response.getHeaders("Set-Cookie");
                if(headers !=null){
                  String value = headers[0].getValue();
                  CommUtils.sid.put(id,value);
                }
            }
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine().getStatusCode());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
            if(islast ==1){
                CommUtils.sid.remove(id);
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
