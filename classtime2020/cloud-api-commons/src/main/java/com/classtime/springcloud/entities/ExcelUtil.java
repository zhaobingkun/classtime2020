package com.classtime.springcloud.entities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 问题1：不能自动判断表头，每次读取表头都要手动修改,已解决，方法是读取某一行时，用正则判断是否包含几个关键字，
 * 如四要素（名称，规格，价格，数量），只要匹配到4个以上关键词，则默认此行为表头，返回表头字段list及表头所在行。
 * 问题2：一个excel文件中有多个表的情况，可根据表头是否有重复字段判断，在解决中。可在提取表头的方法中实现。
 * 问题3：yn.xls  excel 表 问题。
 * 问题4：表头有合并单元格情况，有一个方法已经实现，看是否还有可能继续优化。
 * 问题5：如果单元格内容为表达式 但是不是数学表达式，如"&"字样，目前已经做出判断，但是还需进一步优化。
 * 问题6：单元格中四要素（名称，规格，价格，数量）任意一个为空，则此行跳过。
 * 问题7：判断列为空情况，表头返回的行数+1行开始，逐个单元格取值，如果有一个单元格不为空，则返回此列不为空。
 * 问题8：判断表内容中是否有小计，合计，读取单元格内容时，用正则判断，如果有一个字样出现，则此行跳过。
 * 问题9：判断表内容中有表头的情况，读取单元格内容时，取表头的第一个字段值，用正则判断，如果此行有表头字段出现，则此行跳过。
 * 但是对于两个表头行，目前第二个表头需过滤的字段是写死的，还需进一步优化。
 * 问题10：判断当前行是否为空，逐个单元格读取数据，如果有一个不为空，则此行不为空，否则跳过。
 * 还有一个方法，如果此行的空单元格数量大于一个阈值，可以认为此行为空，不知是否可行。暂未实现。
 */

/**
 * 1、合并单元格读取表头问题
 * 2、yn.xml中批准文号的sheet问题
 * 3、一个excel中有两个表的情况，两个表长度不同，短表的脏数据判断问题。
 * 只能解析并列两个表的情况，如果有多个表并列，还需进一步优化。
 */
public class ExcelUtil {

    private Workbook wb;
    private Sheet sheet;
    private Row row;


    public ExcelUtil(String filepath) {
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

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     * @author laozhao
     */
    public List<TitleInfo> readExcelTitle(int sheetNum) throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(sheetNum);
        row = sheet.getRow(2); //01.xls
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        //标题数组
        //01.xls
        List<TitleInfo> titleList = new ArrayList<TitleInfo>();
        Map<Integer,TitleInfo> titleMap = new HashMap<Integer,TitleInfo>();
        //获取合并单元格数量及合并单元格内容
        int sheetMergeCount = sheet.getNumMergedRegions();
        if (sheetMergeCount > 0) {
            for (int i = 1; i < sheetMergeCount; i++) {
                CellRangeAddress ca = sheet.getMergedRegion(i);
                int firstColumn = ca.getFirstColumn();
                int firstRow = ca.getFirstRow();
                Row fRow = sheet.getRow(firstRow);
                Cell fCell = fRow.getCell(firstColumn);
                TitleInfo titleInfo = new TitleInfo();
                titleInfo.setName(getCellFormatValue(fCell).toString());
                titleInfo.setColNum(firstColumn);
                titleInfo.setRowNum(2);
                titleList.add(titleInfo);
                titleMap.put(firstColumn,titleInfo);
            }
        }
        for (int i = 1; i < colNum; i++) {
            if(titleMap.get(i)==null) {
                TitleInfo titleInfo = new TitleInfo();
                titleInfo.setName(getCellFormatValue(row.getCell(i)).toString());
                titleInfo.setColNum(i);
                titleInfo.setRowNum(2);
                titleList.add(titleInfo);
            }
        }
        return titleList;
    }

    public List<TitleInfo> readExcelTitle0(int sheetNum) throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(sheetNum);

     /*
        Row trow = sheet.getRow(1);
        int sheetMergeCount = sheet.getNumMergedRegions();
        Map<Integer, Integer> mergeMap = new HashMap<Integer, Integer>();
        //如果有合并单元格，将合并单元格列号写入map
        List<Integer> mergeList = new ArrayList<Integer>();
        List<TitleInfo> titleMergeList = new ArrayList<TitleInfo>();
        if (sheetMergeCount > 0) {
            int tcolNum = trow.getPhysicalNumberOfCells() + 1;
            for (int i = 0; i < sheetMergeCount; i++) {
                CellRangeAddress ca = sheet.getMergedRegion(i);
                int firstColumn = ca.getFirstColumn();
                mergeMap.put(firstColumn, firstColumn);
            }
            //将没有合并的单元格列号取出
            for (int i = 0; i < tcolNum; i++) {
                if (mergeMap.get(i) == null) {
                    mergeList.add(i);
                }
            }

            //取出没有合并单元格的列title
            for (int i = 0; i < mergeList.size(); i++) {
                trow = sheet.getRow(2);
                Object obj = getCellFormatValue(trow.getCell(mergeList.get(i)));
                if (!"".equals(obj.toString().trim())) {
                    TitleInfo titleInfo = new TitleInfo();
                    titleInfo.setName(obj.toString());
                    titleInfo.setRowNum(1);
                    titleInfo.setColNum(mergeList.get(i));
                    titleMergeList.add(titleInfo);
                }
            }
        }*/
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells() + 1;
        // 正文内容应该从第n行开始,前n-1行为表头的标题
        int rowNum = sheet.getLastRowNum();
        if (rowNum >= 7) rowNum = 7;
        List<TitleInfo> titleList = new ArrayList<TitleInfo>();
        int i = 0;
        int keywordCount = 0;
        while (i <= rowNum && keywordCount <= 4) {
            keywordCount = 0;
            titleList = new ArrayList<TitleInfo>();
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                try {
                    Cell c = row.getCell(j, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
                } catch (Exception e) {
                    System.out.println("i = " + i + "==j = " + j);
                    break;
                }
                Object obj = getCellFormatValue(row.getCell(j));
                if (!"".equals(obj.toString().trim()) && obj != null) {
                    TitleInfo titleInfo = new TitleInfo();
                    titleInfo.setName(obj.toString());
                    titleInfo.setRowNum(i);
                    titleInfo.setColNum(j);
                    titleList.add(titleInfo);
                }
                Pattern p = Pattern.compile("名称|品名|数量|售价|规格|金额|生产厂商|零售价|数量|药品编码|通用名|价格|商品名|零售价|购入金额|销售金额|药品信息|产地|分类|采购|5月|6月|7月|药名|包装规格");
                Matcher m = p.matcher(obj.toString());
                if (m.find()) {
                    keywordCount++;
                }
                j++;
            }
            if (keywordCount >= 4) break; //如果匹配到大于等于4个关键词，则说明找到了表头
            i++;
        }
        //如果有合并的单元格，将非合并单元格title合并到总的title中。
        /*
        if (sheetMergeCount > 0) {
            for (int mergeSize = 0; mergeSize < titleMergeList.size(); mergeSize++) {
                TitleInfo mtitleInfo = titleMergeList.get(mergeSize);
                boolean isFind = false;
                for (int tSize = 0; tSize < titleList.size(); tSize++) {
                    TitleInfo tInfo = titleList.get(tSize);
                    if (mtitleInfo.getColNum() == tInfo.getColNum()) {
                        isFind = true;
                        titleList.remove(tInfo);
                        titleList.add(mtitleInfo);
                    }
                }
                if (!isFind) {
                    titleList.add(mtitleInfo);
                }

            }
        }*/
        if (keywordCount < 2 || titleList.size() == 0) {
            titleList = new ArrayList<TitleInfo>();
            if(titleList.size()>0) {
                titleList.get(0).setName("没有标题");
                titleList.get(0).setRowNum(-1);
            }
            else{
                TitleInfo titleInfo = new TitleInfo();
                titleInfo.setName("没有标题");
                titleInfo.setRowNum(-1);
                titleInfo.setColNum(0);
                titleList.add(titleInfo);
                TitleInfo titleInfo1 = new TitleInfo();
                titleInfo1.setName("没有标题");
                titleInfo1.setRowNum(-1);
                titleInfo1.setColNum(colNum);
                titleList.add(titleInfo1);
            }
        }

        return titleList;
    }

    /**
     * 读取Excel数据内容
     * <p>
     * filter0  用于过滤excel表内容中间部分表头
     * filter1  用户过滤excel表内容中间部分表头
     * veriMap  用于验证四要素是否为空：名称，规格，数量，价格
     * sheetNum sheet下标
     *
     * @return Map 包含单元格数据内容的Map对象
     * @author laozhao
     */
    public Map<Integer, Map<Integer, Object>> readExcelContent(List<TitleInfo> titleList, Map<Integer, Integer> veriMap, int sheetNum, Map<Integer, Boolean> nullColMap) throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();
        sheet = wb.getSheetAt(sheetNum);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        //int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第n行开始,前n-1行为表头的标题
        int startRow = getStartRow(titleList) + 1;
        int startCol = getMinColNum(titleList);
        int endCol = getMaxColNum(titleList);
        String filterName = titleList.get(0).getName();
        boolean state = true;
        int nullRows = 0;
        Map<Integer, Integer> badRowsMap = new HashMap<Integer, Integer>();//记录脏数据行
        for (int i = startRow; i <= rowNum && state; i++) {
            row = sheet.getRow(i);
            int j = startCol;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
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
            while (j <= endCol) {
                //如果当前列为空，则跳过。读取下一列，如果当前列是为空且是最后一列，则直接退出循环
                if (nullColMap.get(j)) {
                    if (j == endCol) break;
                    j++;
                } //else if (j == endCol && nullColMap.get(j)) break;
                try {
                    Object obj = getCellFormatValue(row.getCell(j));
                    //判断四个要素值是否存在，一个为空，则此行数据无效
                    if (!"".equals(veriMap.get(j)) && veriMap.get(j) != null) {
                        if ("".equals(obj.toString().trim()) || obj == null) {
                            //System.out.println(obj.toString()+"i = " + (i + 1) + "  j= " + j +"脏数据");
                            badRowsMap.put(i + 1, i + 1);//记录脏数据行
                            valueIsNull = true;
                        }
                    }
                    //判断是否空行
                    if (!"".equals(obj) && obj != null) {
                        rowIsNull = false;
                    }
                    //判断是否有小计、合计、总计行，剔除 并记录行号
                    //Pattern p = Pattern.compile("小计|合计|" + filterName + "|" + filter1[0]);
                    Pattern p = Pattern.compile("小计|合计|" + filterName);
                    Matcher m = p.matcher(obj.toString());
                    if (m.find()) {
                        //System.out.println(obj.toString()+  "第" + (i + 1) + "行==" + filterName +"过滤合计及表头");
                        badRowsMap.put(i + 1, i + 1);//记录有小计，合计，和标题行
                        isSum = true;
                    }
                    cellValue.put(j, obj);
                } catch (Exception e) {
                    if (nullRows++ >= 50) state = false;
                    break;
                }
                j++;
            }
            //合法数据，放入map，非法数据行号放入list，数据丢弃
            if (!rowIsNull && !isSum && !valueIsNull) {
                content.put(i, cellValue);
            }
        }
        System.out.println("badRowsMap = " + badRowsMap);
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

    /**
     * 读取excel表格中特定的列,并判断是否为空列
     *
     * @param index 第index列（0开始）
     * @throws Exception
     */
    public boolean readColumn(int index, int sheetNum, int startRow) throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        boolean isNullTxt = true;
        sheet = wb.getSheetAt(sheetNum);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        //startRow 根据title方法返回的所在行+1计算；
        for (int i = startRow + 1; i < rowNum; i++) {
            row = sheet.getRow(i);
            //逐行读取第 index 列，判断此列是否为空，如果某一列有值，直接退出，并标记此列不为空
            try {
                Object obj = getCellFormatValue(row.getCell(index));
                if (!"".equals(obj.toString().trim()) && obj != null && !"合计".equals(obj.toString().trim())) {
                    isNullTxt = false;
                    break;
                }
            }catch (Exception e){
                System.out.println(index);
            }
        }
        return isNullTxt;
    }

    /**
     * 获取总列数
     *
     * @throws Exception
     */
    public int getColNum(int sheetNum, int startRow, int startCol, int endCol) throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(sheetNum);
        // 得到总列数
        int colNum = sheet.getRow(startRow).getPhysicalNumberOfCells();
        System.out.println("总列数：" + colNum);
        return colNum;
    }


    /**
     * 获取sheet数
     *
     * @return
     * @throws Exception
     */
    private int getSheetCount() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        int sheetCount = -1;
        sheetCount = wb.getNumberOfSheets();
        //System.out.println("sheetCount = " + sheetCount);
        return sheetCount;
    }

    /**
     * 获取过滤脏数据四要素：名称，价格，规格，数量
     *
     * @param titleList
     * @return
     */
    public Map getVerificationMap(List<TitleInfo> titleList) {
        Map<Integer, Integer> verificationMap = new HashMap<>();
        //获取关键四要素所处列号，用于判断数据有效性。
        for (int titleCount = 0; titleCount < titleList.size(); titleCount++) {
            //Pattern p = Pattern.compile("名称|品名|数量|销售数量|售价|金额|规格");
            //Pattern p = Pattern.compile("名称|品名|数量|售价|规格|金额|零售价|购入金额|可用数量|当前售价");
            //Pattern p = Pattern.compile("名称|规格|可用数量|当前售价");
            //Pattern p = Pattern.compile("名称|采购数量|采购价|零售价|售价金额");//ll201910.xls
            Pattern p = Pattern.compile("通用名|规格|月需求量|价格|名称|可用数量|当前售价|数量");//空行及空列
            Matcher m = p.matcher(titleList.get(titleCount).getName());
            if (m.find()) {
                //System.out.println(titleList.get(titleCount).getName());
                verificationMap.put(titleList.get(titleCount).getColNum(), titleList.get(titleCount).getColNum());
            }
        }
        Iterator entries = verificationMap.entrySet().iterator();
        int num = 0;
        while (entries.hasNext()) {
            num++;
            Map.Entry entry = (Map.Entry) entries.next();
            Integer key = (Integer) entry.getKey();
            Integer value = (Integer) entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);
        }
        return verificationMap;
    }

    public static int getStartRow(List<TitleInfo> titleInfoList) {
        int startRow = 0;
        //System.out.println("titleInfoList = " + titleInfoList + " " + titleInfoList.size());
        //for(int i = 0;i<titleInfoList.size();i++){
            //TitleInfo titleInfo = titleInfoList.get(i);
            //System.out.println("titleInfo  = " + titleInfo);
        //}
        startRow = titleInfoList.get(0).getRowNum();
        return startRow;
    }

    public static int getMaxColNum(List<TitleInfo> titleInfoList) {
        int maxColNum = 0;
        for (int i = 0; i < titleInfoList.size(); i++) {
            TitleInfo title = titleInfoList.get(i);
            if (title.getColNum() > maxColNum) maxColNum = title.getColNum();
        }
        return maxColNum;
    }

    public static int getMinColNum(List<TitleInfo> titleInfoList) {
        int minColNum = 1;
        for (int i = 0; i < titleInfoList.size(); i++) {
            TitleInfo title = titleInfoList.get(i);
            if (title.getColNum() <= minColNum) minColNum = title.getColNum();
        }
        return minColNum;
    }

    /**
     * 输出过滤后的内容，测试用。后期写库
     *
     * @param map
     */
    public void outPutValue(Map<Integer, Map<Integer, Object>> map) {
        Iterator entries = map.entrySet().iterator();
        int num = 0;
        while (entries.hasNext()) {
            num++;
            Map.Entry entry = (Map.Entry) entries.next();
            Integer key = (Integer) entry.getKey();
            Map value = (Map) entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);
        }
        //System.out.println("num = " + num);
        //入库操作
        //saveToDb(map); //针对1910西药.xls表

    }

    public void saveToDb(Map<Integer, Map<Integer, Object>> map) throws SQLException {
        Iterator entries = map.entrySet().iterator();
        int num = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String name = "";
        String manufacturer = "";
        String nums = "";
        String price = "";
        String norms = "";
        String batchnumber = "";
        try {
            // 3. 建立连接
            conn = DBPoolHelper.getConnection();
            PreparedStatement ps = null;
            // 4. 操作数据
            String sql = "";
            while (entries.hasNext()) {
                num++;
                Map.Entry entry = (Map.Entry) entries.next();
                Integer key = (Integer) entry.getKey();
                Map value = (Map) entry.getValue();
                name = (String) value.get(0);
                manufacturer = (String) value.get(1);
                nums = String.valueOf(value.get(2));
                price = String.valueOf(value.get(3));
                norms = (String) value.get(4);
                batchnumber = (String) value.get(5);
                sql = "insert into 1910wmedicine (name,manufacturer,nums,price,norms,batchnumber) values(?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, manufacturer);
                ps.setString(3, nums);
                ps.setString(4, price);
                ps.setString(5, norms);
                ps.setString(6, batchnumber);
                int resultSet = ps.executeUpdate();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static void main(String[] args) {
        try {
            //String filepath = "/Users/apple/Desktop/20200414/excels/yn.xls";
            //String filepath = "/Users/apple/Desktop/20200414/excels/01.xls";//表头有合并行

            /***************           可以正确解析           *****************/
            String filepath = "/Users/apple/Desktop/20200414/excels/LL201910.xls";//有两个并列的表
            //String filepath = "/Users/apple/Desktop/20200414/excels/0731.xls";//有空，null 用try cache 大量空尾行
            //String filepath = "/Users/apple/Desktop/20200414/excels/数据BB.xls";//数据中间有表头 //名称|规格|数量|生产厂商
            //String filepath = "/Users/apple/Desktop/20200414/excels/多个字段为空值.xlsx"; //名称|规格|可用数量|当前售价
            //String filepath = "/Users/apple/Desktop/20200414/excels/AA1.xlsx";//售价金额，药品信息，规格，数量 //有小计等字样
            //String filepath = "/Users/apple/Desktop/20200414/excels/10月份xx.xlsx";//商品名，规格，购入数量，购入金额//中间有表头等
            //String filepath = "/Users/apple/Desktop/20200414/excels/1910西药.xls";//名称，数量，规格，售价//正常 //表名：1910wmedicine
            //String filepath = "/Users/apple/Desktop/20200414/excels/空行及空列.xlsx";//品名，规格，数量，金额
            ExcelUtil excelReader = new ExcelUtil(filepath);
            //读取sheet数量，并遍历
            int sheetCounts = excelReader.getSheetCount();
            for (int sheetNum = 0; sheetNum < sheetCounts; sheetNum++) {
            //int sheetNum =4;
                // 读取Excel表格标题
                List<TitleInfo> titleList = excelReader.readExcelTitle0(sheetNum);
                //读取表格标题，01.xsl文件合并单元格。
                //List<TitleInfo> titleList = excelReader.readExcelTitle(sheetNum);
                System.out.println("获得Excel表格的标题: titleList" + titleList);

                //处理一个excel存在多个表的情况 begin
                Map<String, TitleInfo> titleMap = new HashMap<String, TitleInfo>();
                boolean isSameTitle = false;
                int samCount = 0;
                List<TitleInfo> titleInfoList1 = new ArrayList<TitleInfo>();
                List<TitleInfo> titleInfoList2 = new ArrayList<TitleInfo>();
                for (int titleCount = 0; titleCount < titleList.size(); titleCount++) {
                    TitleInfo samTitleInfo1 = new TitleInfo();
                    TitleInfo samTitleInfo2 = new TitleInfo();
                    if (!"".equals(titleMap.get(titleList.get(titleCount).getName())) && titleMap.get(titleList.get(titleCount).getName()) != null) {
                        //isSameTitle = true;
                        samCount++;
                        samTitleInfo1.setRowNum(titleMap.get(titleList.get(titleCount).getName()).getRowNum());
                        samTitleInfo1.setColNum(titleMap.get(titleList.get(titleCount).getName()).getColNum());
                        samTitleInfo1.setName(titleMap.get(titleList.get(titleCount).getName()).getName());
                        titleInfoList1.add(samTitleInfo1);

                        samTitleInfo2.setName(titleList.get(titleCount).getName());
                        samTitleInfo2.setColNum(titleList.get(titleCount).getColNum());
                        samTitleInfo2.setRowNum(titleList.get(titleCount).getRowNum());
                        titleInfoList2.add(samTitleInfo2);
                    } else {
                        titleMap.put(titleList.get(titleCount).getName(), titleList.get(titleCount));
                    }
                }
                List<List<TitleInfo>> allTitleLists = new ArrayList<>();
                //有重复表头

                if (samCount == (titleList.size()/2)) {
                    allTitleLists.add(titleInfoList1);
                    allTitleLists.add(titleInfoList2);
                } else {
                    allTitleLists.add(titleList);
                }

                ////处理一个excel存在多个表的情况 end
                // 对读取Excel表格内容测试
                // 数据BB，行间有表头
                //内容中间插入两行表头，目前写死，要想办法自动完成
                //title1[0] = "供货单位";
                Map<Integer, Integer> verificationMap = new HashMap<>();
                Map<Integer, Map<Integer, Object>> map = new HashMap<Integer, Map<Integer, Object>>();
                Map<Integer, Boolean> nullColMap = new HashMap<Integer, Boolean>();
                for (int i = 0; i < allTitleLists.size(); i++) {
                    List<TitleInfo> titleLists = allTitleLists.get(i);
                    int startCol = getMinColNum(titleLists); //titleLists.get(0).getColNum();
                    int endCol = getMaxColNum(titleLists);//titleLists.get(titleLists.size() - 1).getColNum();
                    int startRow = getStartRow(titleLists);//titleLists.get(0).getRowNum() + 1;

                    for (int col = startCol; col <= endCol; col++) {
                        boolean returnval = excelReader.readColumn(col, sheetNum, startRow);
                        nullColMap.put(col, returnval);
                    }
                    //获取过滤脏数据四要素
                    verificationMap = excelReader.getVerificationMap(titleLists);
                    map = excelReader.readExcelContent(titleLists, verificationMap, sheetNum, nullColMap);
                    System.out.println("获得Excel表格的内容:" + map.size());
                    excelReader.outPutValue(map);
                    //入库操作,需单独提取出一个方法
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
