package dmp.tools;



import dmp.beans.Logs;

import java.util.Arrays;
import java.util.List;

public class ReportUtils {
    /**
     * 统计请求数
     *
     * @param
     * @return 总请求数,有效请求,广告请求
     */
    public static List calculateRequest(Logs logs){
        List<Integer> list = Arrays.asList(0, 0, 0);
        if (logs.getRequestmode()==1){
            if(logs.getProcessnode()==1){
                list = Arrays.asList(1, 0, 0);
            }
            else if(logs.getProcessnode()==2){
                list = Arrays.asList(1, 1, 0);
            }
            else if(logs.getProcessnode()==3){
                list = Arrays.asList(1, 1, 1);
            }
        }
        return list;
    }


    /**
     * 计算竞价数
     *
     * @param
     * @return 参与竞价数和竞价成功数
     */

    public static List calculateResponse(Logs logs){
        List<Integer> list = Arrays.asList(0, 0);
        if (logs.getAdplatformproviderid()>=1000 && logs.getIseffective()==1 && logs.getIsbilling()==1){
            if (logs.getIsbid()==1 && logs.getAdorderid() != 0){
                list = Arrays.asList(1, 0);
            }
            else if(logs.getIswin()==1){
                list = Arrays.asList(0, 1);
            }
        }

        return list;
    }


    /**
     * 计算展示量和点击量
     *
     * @param
     * @return 展示量和点击量
     */
    public static List calculateShowClick(Logs logs){
        List<Integer> list = Arrays.asList(0, 0);
        if(logs.getIseffective()==1){
            if(logs.getRequestmode()==2){
                list = Arrays.asList(1, 0);
            }
            else if(logs.getRequestmode()==3){
                list = Arrays.asList(0, 1);
            }
        }

        return list;
    }



    /**
     * 用于计算广告消费和广告成本
     * @param
     * @return 广告消费和广告成本
     */

    public static List calculateAdCost(Logs logs){
        List<Integer> list = Arrays.asList(0, 0);
        if(logs.getAdplatformproviderid()>=100000 && logs.getIseffective()==1 && logs.getIsbilling()==1
            && logs.getIswin()==1 && logs.getAdorderid()>=200000 && logs.getAdcreativeid()>=200000){
                Arrays.asList(logs.getWinprice()/1000, logs.getAdpayment()/1000);
            }

        return list;
    }


}
