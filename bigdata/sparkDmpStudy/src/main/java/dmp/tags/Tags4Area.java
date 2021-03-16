package dmp.tags;

import dmp.beans.Logs;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Tags4Area {

    /**
     * 打地域标签
     * @param logs
     * @return
     */
    public Map  makeTags(Logs logs){
        Map<String,Integer> areaMap = new HashMap<String,Integer>();

        if (StringUtils.isNotEmpty(logs.getProvincename())) {
            areaMap.put("ZP" + logs.getProvincename() , 1);
        }
        if (StringUtils.isNotEmpty(logs.getCityname())) {
            areaMap.put("ZC" + logs.getCityname() , 1);
        }

        return areaMap;
    }
}
