package dmp.tags;

import dmp.beans.Logs;

import java.util.HashMap;
import java.util.Map;

public class Tags4Local {
    /**
     * 打广告位标签
     * @param logs
     * @return
     */
    public Map makeTags (Logs logs){
        Map<String,Integer> localMap = new HashMap<String,Integer>();
        if(logs.getAdspacetype()<10){
            localMap.put("LC0"+logs.getAdspacetype()+"",1);
        }
        else if(logs.getAdspacetype()>=10){
            localMap.put("LC"+logs.getAdspacetype()+"",1);
        }


        return localMap;
    }

}
