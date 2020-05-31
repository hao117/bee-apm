package net.beeapm.agent.common;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * @author yuan
 * @date 2020/05/24
 */
public class ZkUtils {
    private static ZkClient zkClient;
    private static String ZK_PATH_ROOT = "/bee";
    private static String ZK_PATH_IDS = "/bee/ids";

    public static void init(String servers, int sessionTimeout, int connectionTimeout) {
        zkClient = new ZkClient(servers, sessionTimeout, connectionTimeout, new ZkSerializer() {
            @Override
            public byte[] serialize(Object data) throws ZkMarshallingError {
                if (data == null) {
                    return null;
                }
                try {
                    if (data instanceof String) {
                        return ((String) data).getBytes("utf-8");
                    }
                    return JSON.toJSONString(data).getBytes("utf-8");
                } catch (Exception e) {
                    throw new ZkMarshallingError("data serialize failed", e);
                }
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                try {
                    return new String(bytes, "utf-8");
                } catch (Exception e) {
                    throw new ZkMarshallingError("data deserialize failed", e);
                }
            }
        });
    }

    public static ZkClient getZkClient() {
        return zkClient;
    }

    public static void createBeeRootNode() {
        if (zkClient == null) {
            throw new RuntimeException("zk not initialized");
        }
        if (!zkClient.exists(ZK_PATH_ROOT)) {
            try {
                zkClient.createPersistent(ZK_PATH_ROOT);
            } catch (ZkNodeExistsException e1) {
                //忽略异常
            }
        }
        if (!zkClient.exists(ZK_PATH_IDS)) {
            try {
                zkClient.createPersistent(ZK_PATH_IDS);
            } catch (ZkNodeExistsException e1) {
                //忽略异常
            }
        }
    }

    public static boolean createIdNode(int id, String data) {
        if (zkClient == null) {
            throw new RuntimeException("zk not initialized");
        }
        String path = ZK_PATH_IDS + "/" + id;
        if (zkClient.exists(path)) {
            return false;
        }
        try {
            zkClient.createEphemeral(path, data);
            return true;
        } catch (ZkNodeExistsException e) {
            //忽略异常
            return false;
        } catch (Throwable e) {
            throw new RuntimeException("创建唯一标识ID节点失败", e);
        }
    }

}
