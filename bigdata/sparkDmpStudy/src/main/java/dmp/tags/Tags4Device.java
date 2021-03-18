package dmp.tags;

import dmp.beans.Logs;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Tags4Device {
    public static Map makeTags(Logs logs, Map<String, String> deviceDicMap) {
        Map<String, Integer> deviceMap = new HashMap<String, Integer>();

        //操作系统标签
        //client 设备类型 （1：android 2：ios 3：wp）如果获取不到就是4类型，4就是其他的
        String os = deviceDicMap.get(logs.getClient().toString());
        if ("".equals(os) || os == null) {
            os = deviceDicMap.get("4");
            deviceMap.put(os, 1);
        }

        //联网方式标签
        //networkmannername 联网方式名称，如果没有就给NETWORKOTHER代表 其他
        String netWork = deviceDicMap.get(logs.getNetworkmannerid().toString());
        if ("".equals(netWork) || netWork == null) {
            netWork = deviceDicMap.get("NETWORKOTHER");
            deviceMap.put(netWork, 1);
        }

        //运营商的标签
        String isp = deviceDicMap.get(logs.getIspname());
        if ("".equals(isp) || isp == null) {
            isp = deviceDicMap.get("OPERATOROTHER");
            deviceMap.put(isp, 1);
        }

        return deviceMap;
    }
}
