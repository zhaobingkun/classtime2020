package dmp.tags;

import dmp.beans.Logs;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;



public class Tags4Channel {
    /**
     * 打渠道标签
     * @param logs
     * @return
     */
    public Map makeTags(Logs logs){
        Map<String,Integer> channelMap = new HashMap<String,Integer>();
        if(StringUtils.isNotEmpty(logs.getChannelid())){
            channelMap.put("CN"+logs.getChannelid(),1);
        }
        return channelMap;

    }
}
