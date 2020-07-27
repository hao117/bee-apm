package net.beeapm.server.core.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.server.core.common.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 处理filebeat采集的日志
 *
 * @author yuan
 * @date 2020/07/27
 */
@BeePlugin(type = BeePluginType.HANDLER, name = "filebeat")
public class FileBeatStreamHandler extends AbstractStreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(FileBeatStreamHandler.class);
    private static final String DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String LOG_TYPE = "logfile";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FMT);

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void doInit() throws Exception {
        logger.debug("init FileBeatStreamHandler.......");
    }

    @Override
    public void handle(Stream stream) throws Exception {
        Object obj = stream.getSource();
        if (obj.getClass().isArray()) {
            JSONObject[] array = (JSONObject[]) obj;
            for (int i = 0; i < array.length; i++) {
                array[i] = convertData(array[i]);
            }
            stream.setSource(array);
        } else {
            JSONObject jsonObject = (JSONObject) obj;
            jsonObject = convertData(jsonObject);
            stream.setSource(jsonObject);
        }
    }

    private JSONObject convertData(JSONObject obj) {
        String type = (String) JSONPath.eval(obj, "$.fields.type");
        if (!LOG_TYPE.equals(type)) {
            return obj;
        }
        JSONObject result = new JSONObject();
        result.put("type", type);
        result.put("ip", JSONPath.eval(obj, "$.fields.ip"));
        result.put("app", JSONPath.eval(obj, "$.fields.app"));
        result.put("port", JSONPath.eval(obj, "$.fields.port"));
        result.put("message", obj.getString("message"));
        result.put("path", JSONPath.eval(obj, "$.log.file.path"));
        try {
            result.put("time", DATE_FORMAT.parse(obj.getString("@timestamp")));
        } catch (Exception e) {
            logger.error("日期格式转换异常:日期{}", obj.getString("@timestamp"));
            result.put("time", new Date());
        }
        return result;
    }

}
