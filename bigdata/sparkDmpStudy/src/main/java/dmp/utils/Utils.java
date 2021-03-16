package dmp.utils;

import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.lang.StringUtils;
import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public  class Utils {

    public static void main(String[] args) {
        //System.out.println(Utils.fmtDate("2018-12-12 12:11:10"));
        //System.out.println(Utils.fmtTime("2018-12-12 12:11:10"));
        /***********************************************************************/
        //list 相加
        List<String> names = new ArrayList<>(Arrays.asList("张三", "李四", "王五"));
        List<Integer> ages = new ArrayList<>(Arrays.asList(24, 25, 27));
        List<String> ages2 = new ArrayList<>(Arrays.asList("24", "25", "27"));

        Map<String,Integer> amap = new HashMap<String,Integer>();
        Map<String,Integer> bmap = new HashMap<String,Integer>();
        Map<String,Integer> cmap = new HashMap<String,Integer>();
        amap.put("a",1);
        amap.put("b",2);
        amap.put("c",3);
        bmap.put("d",4);
        bmap.put("e",5);
        bmap.put("f",6);
        cmap.putAll(amap);
        cmap.putAll(bmap);
        System.out.println(cmap);


        List<Object> list1= cmap.entrySet().stream().map(et ->et.getKey()+"_"+et.getValue()).collect(Collectors.toList());
        list1.forEach(obj-> System.out.println(obj));
       //names.addAll(ages2);
        //System.out.println(names);

        /******************************************/
        List<Integer> list = Arrays.asList(1, 1, 1);

        //zip 拉链方法
        Stream<String> streamA = names.stream();
        Stream<Integer> streamB  = ages.stream();

        List<Tuple2<String,Integer>> zipped = StreamUtils.zip(streamA,
                streamB,
                (a, b) ->
                    new Tuple2<String,Integer>(a,b)
                )
                .collect(Collectors.toList());
        System.out.println(zipped);
        //方式一：java8的IntSream
        /*Stream<List> stream = IntStream
                .range(0, Math.min(names.size(), ages.size()))
                .mapToObj(i ->{
                    List<Tuple2<String,Integer>> b = new ArrayList<Tuple2<String, Integer>>();

                    Tuple2<String,Integer> a = new Tuple2<String,Integer>(names.get(i),ages.get(i));
                    b.add(a);
                    //System.out.println(a);
                    return b;
                });*/
        //遍历输出
        /*List<List> collect = stream.collect(Collectors.toList());

        for (int i=0;i<collect.size();i++){
            //System.out.println(collect.size());
            for(int j=0;j<collect.get(i).size();j++){
                System.out.println(collect.get(i).size());
                Tuple2<String,Integer> aa = (Tuple2<String, Integer>) collect.get(i).get(j);
                System.out.println(aa);
            }

        }*/

        //stream.forEach();



    }

    public static Integer parseInt(String s){
        if (StringUtils.isNotEmpty(s)){
            return Integer.parseInt(s);
        }
        else{
            return 0;
        }
    }

    public static Double parseDouble(String s){

        if (StringUtils.isNotEmpty(s)){
            return Double.parseDouble(s);
        }
        else{
            return 0.0;
        }

    }



    public static String fmtDate(String s){
        if (StringUtils.isNotEmpty(s))
        {
            String[] s1 = s.split(" ");
            if (s1.length>1) {
                return s1[0].replace("-","");
            }
            else{
                return "unknow";
            }

        }
        else{
            return "unknow";
        }

    }

    public static String fmtTime(String s){
        if (StringUtils.isNotEmpty(s))
        {
            String[] s1 = s.split(" ");
            if (s1.length>1) {
                return s1[1].substring(0,2);
            }
            else{
                return "unknow";
            }

        }
        else{
            return "unknow";
        }
    }
}
