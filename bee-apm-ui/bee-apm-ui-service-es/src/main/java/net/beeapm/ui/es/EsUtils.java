package net.beeapm.ui.es;

import lombok.extern.slf4j.Slf4j;
import net.beeapm.ui.common.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.frameworkset.elasticsearch.handler.ElasticSearchMapResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author yuanlong.chen
 * @date 2021/04/13
 */
@Component
@Slf4j
public class EsUtils {
    private static String DATE_FORMAT_DAY = "yyyy.MM.dd";
    private static BBossESStarter starter;

    @Autowired
    public void setBBossESStarter(BBossESStarter starter) {
        EsUtils.starter = starter;
    }

    public static ClientInterface getClient() {
        return starter.getRestClient();
    }

    public static ClientInterface getClient(String mapperPath) {
        return starter.getConfigRestClient(mapperPath);
    }

    public static Long countAll(String indexPrefix, Long startTime, Long endTime, boolean ignoreUnavailable) {
        String queryAll = "{\"query\": {\"match_all\": {}}}";
        String indices = buildIndices(indexPrefix, startTime, endTime);
        StringBuilder path = new StringBuilder(indices);
        path.append("/_count");
        if (ignoreUnavailable) {
            path.append("?ignore_unavailable=true");
        }
        MapRestResponse searchResult = getClient().executeRequest(path.toString(), queryAll, new ElasticSearchMapResponseHandler());
        return searchResult.getCount();
    }

    /**
     * 当天总数
     * @param indexPrefix
     * @return
     */
    public static Long countAll(String indexPrefix) {
        return countAll(indexPrefix, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public static Long countAll(String indexPrefix, Long startTime, Long endTime) {
        return countAll(indexPrefix, startTime, endTime, true);
    }

    /**
     * 根据时间,生成多个索引
     *
     * @param indexPrefix 索引前缀
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return
     */
    public static String buildIndices(String indexPrefix, Long startTime, Long endTime) {
        Date beginDay = DateUtils.setTime(new Date(startTime), 0, 0, 0, 0);
        Date endDay = DateUtils.setTime(new Date(endTime), 23, 59, 59, 999);
        StringJoiner indicesJoiner = new StringJoiner(",");
        indicesJoiner.add(indexPrefix + DateFormatUtils.format(beginDay, DATE_FORMAT_DAY));
        while (true) {
            beginDay = DateUtils.addDays(beginDay, 1);
            if (beginDay.getTime() <= endDay.getTime()) {
                indicesJoiner.add(indexPrefix + DateFormatUtils.format(endDay, DATE_FORMAT_DAY));
            } else {
                break;
            }
        }
        return indicesJoiner.toString();
    }

    public static String buildIndices(String indexPrefix) {
        return buildIndices(indexPrefix, System.currentTimeMillis(), System.currentTimeMillis());
    }

}
