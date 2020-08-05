package com.classtime.springcloud.entities;

import java.util.ArrayList;
import java.util.List;

public class leven {


    /**
     * 比较两个字符串的相识度
     * 核心算法：用一个二维数组记录每个字符串是否相同，如果相同记为0，不相同记为1，每行每列相同个数累加
     * 则数组最后一个数为不相同的总数，从而判断这两个字符的相识度
     *
     * @param str
     * @param target
     * @return
     */
    private static int compare(String str, String target) {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值
     */
    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */
    public static float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }

    public static void main(String[] args) {
        List<String> nameList = new ArrayList<String>();
        nameList.add("华夏银行北京上地支行");
        nameList.add("华夏银行北京永安支行");
        nameList.add("华夏银行北京亦庄支行");
        nameList.add("华夏银行北京姚家园支行");
        nameList.add("华夏银行北京学院路支行");
        nameList.add("华夏银行北京秀水支行");
        nameList.add("华夏银行北京新发地支行");
        nameList.add("华夏银行北京西直门支行");
        nameList.add("华夏银行北京五方桥支行");
        nameList.add("华夏银行北京魏公村支行");
        nameList.add("华夏银行北京望京支行");
        nameList.add("华夏银行北京万柳支行");
        nameList.add("华夏银行北京通州支行");
        nameList.add("华夏银行北京天通苑支行");
        nameList.add("华夏银行北京陶然支行");
        nameList.add("华夏银行北京四道口支行");
        nameList.add("华夏银行北京顺义支行");
        nameList.add("华夏银行北京首体支行");
        nameList.add("华夏银行北京世纪城支行");
        nameList.add("华夏银行北京石景山支行");
        nameList.add("华夏银行成都分行");
        nameList.add("华夏银行北京紫竹桥支行");
        nameList.add("华夏银行北京中轴路支行");
        nameList.add("华夏银行北京中关村支行");
        nameList.add("华夏银行北京知春支行");
        nameList.add("华夏银行北京长安支行");
        nameList.add("华夏银行北京玉泉路支行");
        nameList.add("交通银行北京望京支行");
        nameList.add("宁波银行股份有限公司北京望京支行");
        nameList.add("平安银行股份有限公司北京望京支行");
        nameList.add("上海浦东发展银行股份有限公司北京望京支行");
        nameList.add("新韩银行（中国）有限公司北京望京支行");
        nameList.add("兴业银行股份有限公司北京望京支行");
        nameList.add("招商银行股份有限公司北京望京支行");
        nameList.add("浙商银行股份有限公司北京望京支行");
        nameList.add("中国工商银行股份有限公司北京望京支行");
        float nums = 0.0f;
        float tmp = 0.0f;
        String bankName = "";

        String a = "华夏银行北京市望京支行";
        for (int i = 0; i < nameList.size(); i++) {
            System.out.println("相似度：" + nameList.get(i) + "=" + getSimilarityRatio(a, nameList.get(i)));
            tmp =getSimilarityRatio(a, nameList.get(i));
            if (nums < tmp) {
                nums=tmp;
                bankName = nameList.get(i);
            }

        }
        System.out.println(bankName);
    }

}
