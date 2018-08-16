package net.beeapm.demo.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Httpclient3xService {
    public int sendRequest(){
        try{
            HttpClient client = new HttpClient();
            PostMethod httppost = new PostMethod("http://127.0.0.1:8080/httpclient3x?method=send");
            StringRequestEntity reqEntity = new StringRequestEntity("Hello Tom!", "text/plain","utf-8");
            httppost.setRequestEntity(reqEntity);
            int code = client.executeMethod(httppost);
            String response = new String(httppost.getResponseBodyAsString().getBytes("UTF-8"));
            if(response!=null){
                System.out.println("===========send===>Response body : "+ response);
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
