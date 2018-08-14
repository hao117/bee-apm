package net.beeapm.demo.service;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * Created by yuan on 2018/8/2.
 */
public class HttpClient4xService {
    public int sendRequest(){
        try{
            HttpClient client=new DefaultHttpClient();
            HttpEntity reqEntity = EntityBuilder.create().setText("Hello Tom!").build();
            HttpPost httppost = new HttpPost("http://127.0.0.1:8080/http?method=send");
            httppost.setEntity(reqEntity);
            HttpResponse response = client.execute(httppost);
            System.out.println("===========send===>Response Status : "+response.getStatusLine());
            HttpEntity respEntity = response.getEntity();
            if(respEntity!=null){
                System.out.println("===========send===>Response length : "+respEntity.getContentLength());
                System.out.println("===========send===>Response body : "+ EntityUtils.toString(respEntity));
            }else{
                System.out.println("nothing to out print");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
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
