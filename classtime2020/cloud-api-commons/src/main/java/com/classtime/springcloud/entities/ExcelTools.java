package com.classtime.springcloud.entities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelTools {

    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public ExcelTools(String filepath) {
        if(filepath==null){
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);
            if(".xls".equals(ext)){
                wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(ext)){
                wb = new XSSFWorkbook(is);
            }else{
                wb=null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel表格表头的内容
     * @return String 表头内容的数组
     * @author laozhao
     */
    public String[] readExcelTitle(int sheetNum) throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        //sheet = wb.getSheetAt(1);
        sheet = wb.getSheetAt(sheetNum);
        //row = sheet.getRow(2); //01.xls
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum="+colNum);
        //标题数组
        //String[] title = new String[colNum+1];//01.xls
        String[] title = new String[colNum];
        //获取合并单元格数量及合并单元格内容
            /*
            int sheetMergeCount = sheet.getNumMergedRegions();
            if(sheetMergeCount>0) {
                for (int i = 1; i < sheetMergeCount; i++) {
                    CellRangeAddress ca = sheet.getMergedRegion(i);
                    int firstColumn = ca.getFirstColumn();
                    System.out.println(firstColumn);
                    int lastColumn = ca.getLastColumn();
                    int firstRow = ca.getFirstRow();
                    int lastRow = ca.getLastRow();
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    title[firstColumn] = getCellFormatValue(fCell).toString();
                }
            }*/
        //for(int i=0;i<title.length;i++){
        for(int i=0;i<colNum;i++){
            //if(title[i]==null){
            title[i] = getCellFormatValue(row.getCell(i)).toString();
            System.out.println(title[i]);
            //}
        }

        return title;
    }

    public String[] readExcelTitle0(int sheetNum) throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(sheetNum);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        //标题数组
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            Object obj = getCellFormatValue(row.getCell(i));
            title[i] = obj.toString();
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * @return Map 包含单元格数据内容的Map对象
     *  filter0 用于过滤excel表内容中间部分表头
     *  filter1 用户过滤excel表内容中间部分表头
     *  veriMap 用于验证四要素是否为空：名称，规格，数量，价格
     * @param sheetNum sheet下标
     * @author laozhao
     */
    public Map<Integer, Map<Integer,Object>> readExcelContent(int sheetNum) throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        Map<Integer, Map<Integer,Object>> content = new HashMap<Integer, Map<Integer,Object>>();
        //sheet = wb.getSheetAt(1);
        sheet = wb.getSheetAt(sheetNum);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        if(sheetNum==1) {
            rowNum = 472;
        }
        //int colNum = row.getPhysicalNumberOfCells() + 1;
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第n行开始,前n-1行为表头的标题
        for (int i = 0; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer,Object> cellValue = new HashMap<Integer, Object>();
            /**
             * 行内容是否为空，默认为真（为空），只要行里单元格有一个有值，则为假
             */
            boolean rowIsNull = true;
            /**
             1、判断是否有小计、合计、总计行，剔除 并记录行号，默认为假（没有），正则查找有值则为真
             2、作为判断excel表中是否有表头的标记，判断行单元格中值如果有表头的第一个字段，则认为是表头行，直接跳过。
             */
            boolean isSum = false;
            /**
             * 判断四要素所在单元格中的值是否存在（名称，规格，数量，价格），如果有一个不存在，则标记此行数据无效
             */
            boolean valueIsNull = false;

            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                //判断四个要素值是否存在，一个为空，则此行数据无效
                /*if (!"".equals(veriMap.get(j)) && veriMap.get(j) != null) {
                    if ("".equals(obj) || obj == null) {
                        valueIsNull = true;
                    }
                }*/
                cellValue.put(j, obj);
                j++;
                //判断是否空行
                if (!"".equals(obj) && obj != null) {
                    rowIsNull = false;
                }
                //判断是否有小计、合计、总计行，剔除 并记录行号
                Pattern p = Pattern.compile("小计|合计");
                Matcher m = p.matcher(obj.toString());
                if (m.find()) {
                    System.out.println("第" + (i + 1) + "行==" + m.group());
                    isSum = true;
                }
            }
            if (!rowIsNull && !isSum && !valueIsNull) {
                content.put(i, cellValue);
            }

        }
        return content;
    }

    /**
     * 根据Cell类型设置数据
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
                    cellvalue = (int)cell.getNumericCellValue();
                    break;
                case FORMULA: {
                    // 判断当前的cell是否为Date
                    if(cell.toString().indexOf("&E")>0){
                        cellvalue = String.valueOf(cell.getStringCellValue());
                    }
                    else if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2020-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2020-7-10
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

    /*public Object getCellValue(Cell cell) {
        if(cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
                //return cell.getNumberValue();
            default:
                return null;
        }
    }*/

    /**
     * 读取excel表格中特定的列,并判断是否为空列
     * @param index 第index列（0开始）
     * @throws Exception
     */
    public boolean readColumn(int index,int sheetNum) throws Exception {
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        boolean isNullTxt = true;
        //sheet = wb.getSheetAt(0);
        sheet = wb.getSheetAt(sheetNum);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        //int colNum = row.getPhysicalNumberOfCells();
        //int colNum=sheet.getRow(6).getPhysicalNumberOfCells();
        for (int i = 7; i < rowNum ; i++) {
            row = sheet.getRow(i);
            //逐行读取第 index 列，判断此列是否为空，如果某一列有值，直接退出，并标记此列不为空
            Object obj = getCellFormatValue(row.getCell(index));
            System.out.println(obj);
            if(obj!=null){
                isNullTxt = false;
                break;
            }
        }
        return isNullTxt;
    }

    /**
     * 获取总列数
     *
     * @throws Exception
     */
    public int getColNum(int sheetNum) throws Exception {
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        //sheet = wb.getSheetAt(0);
        sheet = wb.getSheetAt(sheetNum);
        // 得到总列数
        //row.setRowNum(7);
        //int colNum = row.getPhysicalNumberOfCells();
        int colNum=sheet.getRow(6).getPhysicalNumberOfCells();
        System.out.println("总列数："+colNum);
        return colNum;
    }


    /**
     * 获取sheet数
     * @return
     * @throws Exception
     */
    private int getSheetCount() throws Exception {
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        int sheetCount = -1;
        sheetCount = wb.getNumberOfSheets();
        System.out.println("sheetCount = " + sheetCount);
        return sheetCount;
    }
    public static void main(String[] args) {
        try {
            //String filepath = "/Users/apple/Desktop/20200414/excels/yn.xls";
            //String filepath = "/Users/apple/Desktop/20200414/excels/1910西药.xls";
            //String filepath = "/Users/apple/Desktop/20200414/excels/01.xls";
            //String filepath = "/Users/apple/Desktop/20200414/excels/数据BB.xls";
            //String filepath = "/Users/apple/Desktop/20200414/excels/0731.xls";
            //String filepath = "/Users/apple/Desktop/20200414/excels/AA1.xlsx";
            String filepath = "/Users/apple/Desktop/20200414/excels/空行及空列.xlsx";
            //String filepath = "/Users/apple/Desktop/20200414/excels/10月份xx.xlsx";
            ExcelTools excelReader = new ExcelTools(filepath);
            //读取sheet数量，并遍历
            int sheetCounts = excelReader.getSheetCount();
            //int sheetNum=1;
            for(int sheetNum=0;sheetNum<sheetCounts;sheetNum++) {
                // 读取Excel表格标题
                //String[] title = excelReader.readExcelTitle0(sheetNum);
                //System.out.println("获得Excel表格的标题:");
                //Map<Integer, Integer> verificationMap = new HashMap<>();
                //System.out.println("title.length=" + title.length);
                //获取关键四要素所处列号，用于判断数据有效。
                /*
                for (int titleCount = 0; titleCount < title.length; titleCount++) {
                    //Pattern p = Pattern.compile("名称|品名|数量|销售数量|售价|金额|规格");
                    Pattern p = Pattern.compile("名称|品名|数量|售价|规格|金额");
                    Matcher m = p.matcher(title[titleCount]);
                    if (m.find()) {
                        verificationMap.put(titleCount, titleCount);
                    }
                }*/

                //获取总列数，并获得数据为空的列，记录。
                int colNum = excelReader.getColNum(sheetNum);
                System.out.println("colNum = " + colNum);
                for (int col = 0; col < colNum; col++) {
                    boolean returnval = excelReader.readColumn(col, sheetNum);
                    System.out.println("第" + col + "列:" + returnval);
                }
                // 对读取Excel表格内容测试
                // 数据BB，行间有表头
                //Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent(title[0],title0[0]);

                Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent(sheetNum);
                System.out.println("获得Excel表格的内容:" + map.size());
                //入库操作,需单独提取出一个方法
                Iterator entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    Integer key = (Integer) entry.getKey();
                    Map value = (Map) entry.getValue();
                    System.out.println("Key = " + key + ", Value = " + value);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
