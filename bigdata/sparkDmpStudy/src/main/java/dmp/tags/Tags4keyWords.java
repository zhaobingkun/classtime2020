package dmp.tags;

import dmp.beans.Logs;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Tags4keyWords {
    /**
     * 打关键字标签
     *
     * @param logs
     * @return
     */
    public static Map makeTags(Logs logs) {
        Map<String, Integer> keyWordsMap = new HashMap<String, Integer>();
        if (StringUtils.isNotEmpty(logs.getKeywords())) {
            String[] fields = logs.getKeywords().split("|");
            for (String field : fields) {
                if (field.length() >= 3 && field.length() <= 8) {
                    keyWordsMap.put("K" + field.replace(":", ""), 1);
                }
            }
        }
        return keyWordsMap;
    }
}
