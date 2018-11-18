package net.beeapm.ui.es;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.common.ConfigHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yuan on 2018/11/11.
 */
public class EsJestClient {
    private static JestClient jestClient ;
    private static EsJestClient inst;
    private static final Logger LOGGER = LoggerFactory.getLogger(EsJestClient.class);
    public static EsJestClient inst(){
        if(jestClient == null){
            synchronized (EsJestClient.class){
                if(jestClient == null){
                    inst = new EsJestClient();
                    initJestClient();
                }
            }
        }
        return inst;
    }

    public JestClient getJestClient(){
        return jestClient;
    }

    private static HttpClientConfig.Builder buildConfig(HttpClientConfig.Builder builder){
        Properties properties = ConfigHolder.getProperties(BeeConst.KEY_ELASTICSEARCH,true);
        properties.remove(BeeConst.KEY_ES_HTTP_URL);
        properties.remove(BeeConst.KEY_ES_PASSWORD);
        properties.remove(BeeConst.KEY_ES_USERNAME);
        Iterator iterator = properties.entrySet().iterator();
        while (iterator.hasNext()){
            try {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                Method method = builder.getClass().getMethod(entry.getKey(), entry.getValue().getClass());
                if(method != null){
                    builder = (HttpClientConfig.Builder)method.invoke(builder,entry.getValue());
                }
            }catch (Exception e){
                LOGGER.error("",e);
            }
        }
        return builder;

    }

    private static void initJestClient(){
        String[] urls = ConfigHolder.getProperty(BeeConst.KEY_ELASTICSEARCH + "." + BeeConst.KEY_ES_HTTP_URL).split(",");
        HttpClientConfig.Builder builder = new  HttpClientConfig.Builder(Arrays.asList(urls));
        String userName = ConfigHolder.getProperty(BeeConst.KEY_ELASTICSEARCH + "." + BeeConst.KEY_ES_USERNAME);
        String password = ConfigHolder.getProperty(BeeConst.KEY_ELASTICSEARCH + "." + BeeConst.KEY_ES_PASSWORD);
        if(StringUtils.isNotBlank(userName)){
            builder = builder.defaultCredentials(userName,password);
        }
        try {
            builder = builder.sslSocketFactory(sslConnectionSocketFactory());
            builder = buildConfig(builder);

        }catch (Exception e){
            LOGGER.error("",e);
        }
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(builder.build());
        jestClient = factory.getObject();
    }

    public SearchResult search(String[] indices, String[] types, String queryString) throws IOException{
        LOGGER.debug("queryString={}",queryString);
        Search.Builder builder = new Search.Builder(queryString);
        if(indices != null && indices.length>0){
            builder = builder.addIndices(Arrays.asList(indices));
        }
        if(types != null && types.length>0){
            builder = builder.addTypes(Arrays.asList(types));
        }
        Search search = builder.build();
        SearchResult res = jestClient.execute(search);
        LOGGER.debug("SearchResult={}", res.getJsonString());
        return res;
    }

    private static SSLConnectionSocketFactory sslConnectionSocketFactory(){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            // 获取分层tls/ssl连接
            return new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        }catch (Exception e){
            LOGGER.error("",e);
        }
        return null;
    }
}
