package dmp.tags;

import dmp.beans.Logs;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Tags4App {

    /**
     * 给app打标签
     * @param logs
     * @return
     */
    public static Map makeTags(Logs logs,Map<String,String> appDict){
        Map<String,Integer> appMap = new HashMap<String,Integer>();
        String appName = "";
        if(StringUtils.isNotEmpty(logs.getAppid())){
            appName=appDict.get(logs.getAppid());
        }
        if(StringUtils.isNotEmpty(appName) && !"".equals(appName)){
            appMap.put("APP"+appName,1);
        }
        return  appMap;
    }
}
