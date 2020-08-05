package com.classtime.springcloud.entities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class LotteryTicket {
    public static void main(String[] args) {
        System.out.println("您所选的7位数字:");
        int []CustCode =new int [7] ;
        CustCode[0] = 1;
        CustCode[1] = 13;
        CustCode[2] = 5;
        CustCode[3] = 17;
        CustCode[4] = 19;
        CustCode[5] = 21;
        CustCode[6] = 19;
        int len = CustCode.length;
        int codeCount = 0;
        while(codeCount<len) {
            System.out.print(CustCode[codeCount++] + " ");
        }
        System.out.println();
        Select(CustCode,getNum()) ;
    }
    private static int[] getNum(){

        System.out.println("选取中奖号码：...");
        int []Result = new int [7] ;
        List<Integer> nums = new ArrayList<>();
        for (int i=0;i<=37;i++){
            nums.add(i);
        }
        Random random = new Random () ;
        for(int i =0;i <7;i++) {
            int numRandom = random.nextInt();
            int numRandom36 = numRandom % (nums.size()-1);
            int tmp =  1 + Math.abs(numRandom36);
            Result[i] = nums.get(tmp);
            nums.remove(tmp);
        }
        int len= 0;
        while(len<7) {
            System.out.print(Result[len++] + " ");
        }
        System.out.println();
        return Result;
    }
    static int RightNum = 0 ;
    private static void Select(int []CustCode ,int []Resault){
        int Count = 7;
        for(int i =0;i<Count;i++){
            for(int j =0;j<Count;j++) {
                if(CustCode[i] ==Resault[j] )
                    RightNum++ ;
            }
        }
        OutPutValue(RightNum) ;
    }
    private static void OutPutValue(int RightNum) {
        switch(RightNum){
            case 0:System.out.println("没中"); break;
            case 1:System.out.println("中一个号"); break;
            case 2:System.out.println("中二个"); break;
            case 3:System.out.println("中三个"); break;
            case 4:System.out.println("中四个"); break;
            case 5:System.out.println("中五个"); break;
            case 6:System.out.println("中六个"); break;
            case 7:System.out.println("中七个"); break;
            default :System.out.print("异常");
        }

    }
}
