package net.beeapm.agent.reporter.okhttp;

import net.beeapm.agent.config.ConfigUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yuan on 2017/11/21.
 */
public class OkHttpHelper {
    private static OkHttpHelper instance;
    private static OkHttpClient client;
    private static final String keyPrefix = "reporter.okhttp.";

    private static List<String> urlList;
    private static AtomicInteger counter = new AtomicInteger(0);

    private OkHttpHelper() {
        int connectTimeout = ConfigUtils.me().getInt(keyPrefix+"connectTimeout",20);
        int writeTimeout = ConfigUtils.me().getInt(keyPrefix+"writeTimeout",10);
        int readTimeout = ConfigUtils.me().getInt(keyPrefix+"readTimeout",10);
        client = new OkHttpClient
                .Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
        urlList = ConfigUtils.me().getList(keyPrefix+"url");

    }

    public static OkHttpHelper getInstance(){
        if(null == instance){
            instance = new OkHttpHelper();
        }
        return instance;
    }

    public  String parseContentType(Map<String,String> header){
        if(header == null || header.isEmpty()){
            return "application/json; charset=utf-8";
        }
        if(header.containsKey("Content-Type")){
            return header.get("Content-Type");
        }else if(header.containsKey("content-type")){
            return header.get("content-type");
        }else if(header.containsKey("contentType")){
            return header.get("contentType");
        }
        return "application/json; charset=utf-8";
    }

    /**
     * 默认json请求，其它类型请在header里添加Content-Type
     * @param uri
     * @param parameters
     * @param header
     * @param content
     * @param timeout
     * @param charset
     * @return
     */
    public String post(String uri, Map<String,String> parameters, Map<String,String> header, String content, Integer timeout, String charset){
        try {
            if(content == null){
                content = "";
            }
            OkHttpClient myClient = client;
            String contentType = parseContentType(header);

            RequestBody body = RequestBody.create(MediaType.parse(contentType), content);
            uri = parseUrlParams(uri, parameters, charset);

            Request.Builder builder = new Request.Builder().post(body).url(uri);
            if(header != null && !header.isEmpty()){
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    if(StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            if(timeout != null && timeout > 0 && timeout != myClient.readTimeoutMillis() / 1000){
                myClient = myClient.newBuilder().readTimeout(timeout,TimeUnit.SECONDS).build();
            }

            Response response = myClient.newCall(builder.build()).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new RuntimeException("Unexpected code " + response);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public  String parseUrlParams(String uri,Map<String,String> parameters,String charset) throws  Exception{
        if(null!=parameters&&!parameters.isEmpty()){
            String split = "?";
            if(uri.contains("?")){
                split = "&";
            }
            for(Map.Entry<String,String> entry:parameters.entrySet()){
                uri+=(split + URLEncoder.encode(entry.getKey(),charset)+"="+URLEncoder.encode(entry.getValue(),charset));
                split = "&";
            }
        }
        return uri;
    }

    public String getUrl(){
        int i = counter.incrementAndGet() % urlList.size();
        return urlList.get(i);
    }

    public String post(String body){
        return post(getUrl(),null,null,body,null,null);
    }

}
