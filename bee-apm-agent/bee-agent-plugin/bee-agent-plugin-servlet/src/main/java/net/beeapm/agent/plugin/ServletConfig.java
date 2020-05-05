package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author yuan
 * @date 2018/09/19
 */
public class ServletConfig extends AbstractBeeConfig {
    private static ServletConfig config;
    private Boolean enable;
    private Boolean enableReqParam;
    private Boolean enableReqBody;
    private Boolean enableReqHeaders;
    private Boolean enableRespBody;
    private long spend;
    private List<String> urlSuffixExcludeList;

    public static ServletConfig me() {
        if (config == null) {
            synchronized (ServletConfig.class) {
                if (config == null) {
                    config = new ServletConfig();
                    BeeConfigFactory.me().registryConfig("servlet", config);
                }
            }
        }
        return config;
    }

    private ServletConfig() {
        initConfig();
    }

    @Override
    public void clear() {
        config = null;
    }

    @Override
    public void initConfig() {
        enable = ConfigUtils.me().getBoolean("plugins.servlet.enable", true);
        enableReqParam = enable & ConfigUtils.me().getBoolean("plugins.servlet.enableReqParam", false);
        enableReqBody = enable & ConfigUtils.me().getBoolean("plugins.servlet.enableReqBody", false);
        enableReqHeaders = enable & ConfigUtils.me().getBoolean("plugins.servlet.enableReqHeaders", false);
        enableRespBody = enable & ConfigUtils.me().getBoolean("plugins.servlet.enableRespBody", false);
        urlSuffixExcludeList = Arrays.asList(ConfigUtils.me().getStr("plugins.servlet.excludeUrlSuffix", "").split(","));
        //http入口的耗时要小于等于方法的耗时，否则会造成调用链断开
        long processSpend = ConfigUtils.me().getInt("plugins.process.spend", -1);
        spend = ConfigUtils.me().getInt("plugins.servlet.spend", -1);
        if (spend > processSpend) {
            spend = processSpend;
        }
    }

    public long getSpend() {
        return spend;
    }

    public Boolean isEnable() {
        return enable;
    }

    public Boolean isEnableReqParam() {
        return enableReqParam;
    }

    public Boolean isEnableReqBody() {
        return enableReqBody;
    }

    public Boolean isEnableReqHeaders() {
        return enableReqHeaders;
    }

    public Boolean isEnableRespBody() {
        return enableRespBody;
    }

    public Boolean isExcludeUrlSuffix(String url) {
        if (urlSuffixExcludeList == null || urlSuffixExcludeList.isEmpty()) {
            return false;
        }
        int size = urlSuffixExcludeList.size();
        for (int i = 0; i < size; i++) {
            if (url.endsWith(urlSuffixExcludeList.get(i))) {
                return true;
            }
        }
        return false;
    }
}
