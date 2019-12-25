package net.beeapm.server.store.mysql;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import net.beeapm.server.core.common.ConfigHolder;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/11/17/017.
 */
public class MysqlUtils {
    private static Logger logger = LoggerFactory.getLogger(MysqlUtils.class);
    private static DBPoolConnection dbPoolConnection = null;
    private static DruidPooledConnection druidPooledConnection = null;
    private static PreparedStatement preparedStatement = null;



    public static DBPoolConnection inst(){
        if(dbPoolConnection == null){
            synchronized (MysqlUtils.class){
                if(dbPoolConnection == null){
                    dbPoolConnection = DBPoolConnection.getInstance();
                }
            }
        }
        return dbPoolConnection;
    }

    public static void insert(Object ... datas){
        try {
            druidPooledConnection = dbPoolConnection.getConnection();
            druidPooledConnection.setAutoCommit(false);
            preparedStatement = druidPooledConnection.prepareStatement("insert into test");
            preparedStatement.execute();
            druidPooledConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (druidPooledConnection != null){
                try {
                    druidPooledConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
