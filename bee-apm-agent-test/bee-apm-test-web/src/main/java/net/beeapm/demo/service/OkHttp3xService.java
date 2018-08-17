package net.beeapm.demo.service;

import okhttp3.*;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class OkHttp3xService {
    public int sendRequest(){
        try{
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("ext/plain; charset=utf-8"), "Hello Tom!");
            String url = "http://127.0.0.1:8080/okhttp3x?method=send";
            okhttp3.Request.Builder builder = new Request.Builder().post(body).url(url);
            Response response = client.newCall(builder.build()).execute();
            if (response.isSuccessful()) {
                System.out.println("===============sendRequest=====response=>"+response.body().string());
            } else {
                throw new RuntimeException("Unexpected code " + response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    public int readRequestBody(HttpServletRequest request){
        return 0;
    }

    public int receiveMessage(HttpServletRequest request, HttpServletResponse response){
        try {
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()){
                String headerKey = headers.nextElement();
                System.out.println("============header===>"+headerKey + ":"+request.getHeader(headerKey));
            }
            System.out.println("============QueryString===>"+request.getQueryString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String body = IOUtils.toString(reader);
            System.out.println("=================>body:===>"+body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
