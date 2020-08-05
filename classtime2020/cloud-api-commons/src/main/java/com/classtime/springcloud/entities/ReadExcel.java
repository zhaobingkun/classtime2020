package com.classtime.springcloud.entities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadExcel {
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public ReadExcel(String filepath) {
        if (filepath == null) {
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();
        // 得到总行数
        sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < 2) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);
        }
        return content;
    }

    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     * @author laozhao
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case NUMERIC:// 如果当前Cell的Type为NUMERIC
                    cellvalue = cell.getNumericCellValue();
                    break;
                case FORMULA: {
                    // 判断当前的cell是否为Date
                    if (cell.toString().indexOf("&E") > 0) {
                        cellvalue = String.valueOf(cell.getStringCellValue());
                    } else if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {// 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                case ERROR:
                    cellvalue = "";
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }


    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        try {

/*

            String sourceName = "中国邮政储蓄并州路支行";
            String bankName ="中国邮政储蓄山西省太原市并州路支行";
            int ends= bankName.indexOf("中国邮政储蓄")+6;
            if(bankName.substring(ends,ends+2).equals("银行")){
                ends= ends=+8;
            }
            String subString1 = bankName.substring(0,ends);
            String subString2 = bankName.substring(ends,bankName.length());
            System.out.println("subString1="+subString1);
            System.out.println("subString2="+subString2);
            int wordnum = subString2.length();

            if(sourceName.indexOf(subString1)>=0){
                int stars = 0;
                while(wordnum > 1) {
                    int counts = sourceName.indexOf(subString2.substring(stars,subString2.length()));
                    if(counts>0) {
                        System.out.println(sourceName);
                        System.out.println(bankName);
                        break;
                    }
                    stars++;
                    wordnum--;
                    System.out.println("wordnum = "+subString2.substring(stars,subString2.length()));
                }
            }

*/


           /* String bankName ="成都农商银行四川省金堂支行";
            int ends = bankName.indexOf("银行");
            String subString = bankName.substring(0,ends+2);
            System.out.println(subString);
*/
            //String bankName ="防城港市防城区农村信用合作联社防城信用社";
            //中国邮政储蓄北京市西城区大市口支行
/*

            String bankName ="河南省宝丰县农村信用社道南分理处";
            int ends = bankName.indexOf("信用社");
            String subString = bankName.substring(0,ends+2);
            System.out.println(subString);
*/


            String allFilePath = "D:\\javas/all.xlsx";//有两个并列的表
            //String allFilePath = "/Users/apple/Desktop/all.xlsx";
            ReadExcel excelReader = new ReadExcel(allFilePath);
            Map<Integer, Map<Integer, Object>> allMap = new HashMap<Integer, Map<Integer, Object>>();
            allMap = excelReader.readExcelContent();


            String workFilePath = "D:\\javas/work.xlsx";//有两个并列的表
            //String workFilePath = "/Users/apple/Desktop/work.xlsx";
            excelReader = new ReadExcel(workFilePath);
            Map<Integer, Map<Integer, Object>> workMap = new HashMap<Integer, Map<Integer, Object>>();
            workMap = excelReader.readExcelContent();


            List<String> cityList = readJson.getJsonValue("D:\\javas\\city_bak.json");
/*            for(int i=0;i<cityList.size();i++){
                System.out.println(i+"=="+cityList.get(i));
            }*/

            List<String> provinceList = readJson.getJsonValue("D:\\javas\\province_bak.json");
/*            for(int i=0;i<provinceList.size();i++){
                System.out.println(i+"=="+provinceList.get(i));
            }*/


            List<String> areaList = readJson.getJsonValue("D:\\javas\\area.json");
/*
            Iterator entries = allMap.entrySet().iterator();
            int num = 0;
            while (entries.hasNext()) {
                num++;
                Map.Entry entry = (Map.Entry) entries.next();
                Integer key = (Integer) entry.getKey();
                Map value = (Map) entry.getValue();
                System.out.println("Key = " + key + ", Value = " + value);
            }*/

            leven lev = new leven();
            Iterator workentries = workMap.entrySet().iterator();
            int worknum = 0;
            int ends = 0;
            List<List> publicWork = new ArrayList<List>();
            List<String> notInPublicWork = new ArrayList<String>();
            while (workentries.hasNext()) {
                ends = 0;
                worknum++;
                Map.Entry entry = (Map.Entry) workentries.next();
                Integer key = (Integer) entry.getKey();
                Map value = (Map) entry.getValue();
                String bankName = (String) value.get(1);
                //暴力方法，直接遍历两个map，取到相似度最大的值。


                //先从大表取 匹配的 待查字符串的头串   如 农业银行开头等
                if (bankName.indexOf("银行") > 0) {
                    ends = bankName.indexOf("银行") + 2;
                } else if (bankName.indexOf("信用合作联社") > 0) {
                    ends = bankName.indexOf("信用合作联社") + 6;
                } else if (bankName.indexOf("信用社") > 0) {
                    ends = bankName.indexOf("信用社") + 3;
                } else if (bankName.indexOf("储蓄") > 0) {
                    ends = bankName.indexOf("储蓄") + 2;
                } else {
                    ends = 0;
                }
                //遍历省的list，找到对应的省
                String provinceName = "";
                //if (bankName.indexOf("省") > 0) {
                for (int i = 0; i < provinceList.size(); i++) {
                    if (bankName.indexOf(provinceList.get(i)) > 0) {
                        provinceName = provinceList.get(i);
                        break;
                    }
                }
                // }

                //遍历市的list，找到对应的市
                String cityName = "";
                // if (bankName.indexOf("市") > 0) {
                for (int i = 0; i < cityList.size(); i++) {
                    if (bankName.indexOf(cityList.get(i)) > 0) {
                        cityName = cityList.get(i);
                        break;
                    }
                }
                // }

                String areaName = "";

                for (int i = 0; i < areaList.size(); i++) {
                    if (bankName.indexOf(areaList.get(i)) > 0) {
                        areaName = areaList.get(i);
                        break;
                    }
                }


                // System.out.println("provinceName="+provinceName);
                //System.out.println("cityName="+cityName);


                List<Work> mList = new ArrayList<Work>();
                boolean isOk = false;

                if (ends > 0) {
                    String subStringBankName = bankName.substring(0, ends);
                    String subString2 = bankName.substring(ends, bankName.length());
                    //System.out.println(subStringBankName);
                    int wordnum = subString2.length();
                    int num = 0;
                    Iterator entries = allMap.entrySet().iterator();


                    while (entries.hasNext()) {
                        Work mWork = new Work();
                        Map.Entry entryAll = (Map.Entry) entries.next();
                        Map valueAll = (Map) entryAll.getValue();
                        String sourceName = (String) valueAll.get(1);
                        String soruceCode = (String) valueAll.get(0);
                        //如果匹配到work字符串头，则放入list，做进一步比较
                        //为了减少对比数据
                        if (subStringBankName.equals("中国交通银行")) {
                            subStringBankName = "交通银行";
                        } else if (subStringBankName.equals("中国平安银行")) {
                            subStringBankName = "平安银行";
                        } else if (subStringBankName.equals("中国邮储蓄银行")) {
                            subStringBankName = "中国邮政储蓄银行";
                        }

                        if (sourceName.indexOf(subStringBankName) >= 0) {
                            isOk = true;
                            int slen = sourceName.indexOf(subStringBankName) + subStringBankName.length();
                            mWork.setSourceName(sourceName);
                            mWork.setSourceCode(soruceCode);
                            mWork.setBankNameSubStr(subString2);
                            mWork.setSourceNameSbuStr(sourceName.substring(slen, sourceName.length()));
                            mList.add(mWork);
                            //System.out.println(subStringBankName+"="+subString2+"=="+sourceName.substring(0,slen)+"=="+sourceName.substring(slen,sourceName.length()));
                        }

                    }
                }

                if (!isOk) {
                    notInPublicWork.add(bankName);
                }

                //最小编辑距离法 计算最大相似度
                float nums = 0.0f;
                float tmp = 0.0f;
                String tmpBankName = "";
                String soruceName = "";
                String sourceCode = "";
                float socre = 0.0f;
                List<Work> workList = new ArrayList<Work>();
                for (int i = 0; i < mList.size(); i++) {
                    Work work = new Work();
                    Work mWork = new Work();
                    mWork = mList.get(i);
                    tmp = 0.0f;
                    if (!provinceName.equals("")) {
                        //System.out.println(provinceName);
                        if (mWork.getSourceNameSbuStr().indexOf(provinceName) >= 0) {
                            tmp = tmp + 1;
                        }
                    }
                    if (!cityName.equals("")) {
                        //System.out.println(cityName);
                        if (mWork.getSourceNameSbuStr().indexOf(cityName) >= 0) {
                            tmp = tmp + 1;
                        }
                    }

/*                    if (!areaName.equals("")) {
                        System.out.println(areaName);
                        if (mWork.getSourceNameSbuStr().indexOf(areaName) > 0) {
                            tmp = tmp + 1;
                        }
                    }*/


                    tmp = tmp + lev.getSimilarityRatio(mWork.getBankNameSubStr(), mWork.getSourceNameSbuStr());
                    //取最大相似值
                    if (nums < tmp) {
                        nums = tmp;
                        tmpBankName = bankName;
                        soruceName = mWork.getSourceName();
                        sourceCode = mWork.getSourceCode();
                        socre = nums;
                        work.setBankName(tmpBankName);
                        work.setScore(socre);
                        work.setSourceCode(sourceCode);
                        work.setSourceName(soruceName);

                        workList = new ArrayList<Work>();
                        workList.add(work);
                    }
                    //取相等的相似值
                    else if (nums == tmp) {
                        nums = tmp;
                        tmpBankName = bankName;
                        soruceName = mWork.getSourceName();
                        sourceCode = mWork.getSourceCode();
                        socre = nums;
                        work.setBankName(tmpBankName);
                        work.setScore(socre);
                        work.setSourceCode(sourceCode);
                        work.setSourceName(soruceName);
                        //if (workList.size() < 5) {
                            workList.add(work);
                       // }
                    }
                }
                publicWork.add(workList);
            /*    float nums = 0.0f;
                float tmp = 0.0f;
                String tmpBankName = "";
                String soruceName = "";
                String sourceCode = "";
                float socre = 0.0f;
                Iterator entries = allMap.entrySet().iterator();
                List<Work> workList = new ArrayList<Work>();
                while (entries.hasNext()) {
                    Work work = new Work();
                    Map.Entry entry1 = (Map.Entry) entries.next();
                    Map value1 = (Map) entry1.getValue();
                    tmp = lev.getSimilarityRatio(bankName, (String) value1.get(1));
                    //取最大相似值
                    if (nums < tmp) {
                        nums = tmp;
                        tmpBankName = bankName;
                        soruceName = (String) value1.get(1);
                        sourceCode = (String) value1.get(0);
                        socre = nums;
                        work.setBankName(tmpBankName);
                        work.setScore(socre);
                        work.setSourceCode(sourceCode);
                        work.setSourceName(soruceName);

                        workList = new ArrayList<Work>();
                        workList.add(work);
                    }
                    //取相等的相似值
                    else if (nums == tmp) {
                        nums = tmp;
                        tmpBankName = bankName;
                        soruceName = (String) value1.get(1);
                        sourceCode = (String) value1.get(0);
                        socre = nums;
                        work.setBankName(tmpBankName);
                        work.setScore(socre);
                        work.setSourceCode(sourceCode);
                        work.setSourceName(soruceName);
                        if (workList.size() < 5) {
                            workList.add(work);
                        }
                    }
                }
                publicWork.add(workList);
*/
                //System.out.println(tmpBankName + "===" + bankName);
                /**/
            }


            //输出内容  =1   966

            List<String> fiveList = new ArrayList<String>();
            int count = 0;
            int count_zero = 0;
            System.out.println(publicWork.size() + "==");

            File csv = new File("d:\\bankName2.csv");//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            bw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            //bw.write("u\feff");
            // System.out.println(i+"=="+jsonList.get(i).getMaterialname());


            for (int i = 0; i < publicWork.size(); i++) {
                List tmpList = publicWork.get(i);
                if (tmpList.size() == 5) {
                    count++;
                    Work fwork = (Work) tmpList.get(0);
                    fiveList.add(fwork.getBankName());
                }
                if (tmpList.size() == 0) count_zero++;

                System.out.println(i + "==");
                for (int j = 0; j < tmpList.size(); j++) {

                    Work work = (Work) tmpList.get(j);
                    if (tmpList.size() < 5) {
                        bw.newLine();
                        bw.write(work.getSourceCode() + ","
                                + work.getSourceName() + "," + work.getBankName());
                    }
                    System.out.print(work.getScore() + "==");
                    System.out.print("cname==" + work.getBankName() + "==");
                    System.out.print("sname==" + work.getSourceName() + "==");
                    System.out.println();
                }

                System.out.println();
            }

            for (int i = 0; i < notInPublicWork.size(); i++) {
                bw.newLine();
                bw.write("" + "," + "" + "," + notInPublicWork.get(i));
            }

            for (int i = 0; i < fiveList.size(); i++) {
                bw.newLine();
                bw.write("" + "," + "" + "," + fiveList.get(i));
            }
            bw.close();


            //System.out.println("count==" + count);
            //System.out.println("count_zero==" + count_zero);


/*            for(int i=0;i<fiveList.size();i++){
                System.out.println("bankName==" + fiveList.get(i));
            }

            for(int i=0;i<notInPublicWork.size();i++){
                System.out.println("bankName==" + notInPublicWork.get(i));
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
