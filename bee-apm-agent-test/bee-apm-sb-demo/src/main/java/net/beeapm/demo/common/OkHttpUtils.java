package net.beeapm.demo.common;

import com.alibaba.fastjson.JSON;
import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;
import okhttp3.*;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
public class OkHttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtils.class);
    private OkHttpClient client;
    private static final OkHttpUtils INST = new OkHttpUtils();

    public static OkHttpUtils instance() {
        return INST;
    }

    private void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS);
        client = builder.build();
    }

    public String post(String url, String body) throws Exception {
        LOGGER.info("============>{}, body={}", url, body);
        initOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body))
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public ResultVo post(RequestVo vo) {
        vo.setCounter(vo.getCounter() + 1);
        try {
            Thread.sleep(RandomUtils.nextInt(10, 1000));
        } catch (Exception e) {
        }
        if (vo.getCounter() < Config.maxCounter()) {
            try {
                vo.setMsg(vo.getMsg() + "->" + Config.instName());
                String body = post(Config.url(), JSON.toJSONString(vo));
                LOGGER.info("============>body={}", body);
                ResultVo result = JSON.parseObject(body, ResultVo.class);
                LOGGER.info("============>ResultVo={}", JSON.toJSONString(result));
                return result;
            } catch (Exception e) {
                LOGGER.error("发送HTTP请求失败",e);
            }
        }
        ResultVo result = ResultVo.OK(vo.getCounter(), vo.getMsg() + "->" + Config.instName());
        return result;
    }

    public static void main(String[] args) {
        RequestVo vo = new RequestVo();
        vo.setMsg("->");
        vo.setCounter(0);
        try {
            String s = OkHttpUtils.instance().post("http://localhost:8101/hello/sayHello", JSON.toJSONString(vo));
            System.out.println(s);
        }catch (Exception e){

        }
    }

}
