package cn.dwxmp.core.support.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;

public class Excel2007Reader extends DefaultHandler {
    private final Logger logger = LoggerFactory.getLogger(Excel2007Reader.class);
    // 共享字符串表
    private SharedStringsTable sst;
    // 上一次的内容
    private String lastContents;
    private boolean nextIsString;

    private int sheetIndex = -1;
    private List<String> rowlist = new ArrayList<String>();
    // 当前行
    private int curRow = 0;
    // 当前列
    private int curCol = 0;
    // 日期标志
    private boolean dateFlag;
    // 数字标志
    private boolean numberFlag;

    private boolean isTElement;

    private IRowReader rowReader;

    public void setRowReader(IRowReader rowReader) {
        this.rowReader = rowReader;
    }

    public Excel2007Reader() {
    }

    public Excel2007Reader(IRowReader rowReader) {
        this.rowReader = rowReader;
    }

    /**
     * 只遍历一个电子表格，其中sheetId为要遍历的sheet索引，从1开始，1-3
     *
     * @param filename
     * @param sheetId
     * @throws Exception
     */
    public void processOneSheet(String filename, int sheetId) {
        try {
            OPCPackage pkg = OPCPackage.open(filename);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);

            process(r, parser, sheetId);
        } catch (IOException | OpenXML4JException | SAXException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 遍历工作簿中所有的电子表格
     *
     * @param filename
     * @throws Exception
     */
    public void processAll(String filename) {
        try {
            InputStream stream = new FileInputStream(filename);
            processAll(stream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 遍历工作簿中所有的电子表格
     *
     * @param r
     * @param parser
     * @param sheetId
     */
    public void process(XSSFReader r, XMLReader parser, int sheetId) {
        try {
            // 根据 rId# 或 rSheet# 查找sheet
            InputStream sheet2 = r.getSheet("rId" + sheetId);
            process(r, parser, sheet2);
        } catch (IOException | OpenXML4JException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 遍历工作簿中所有的电子表格
     *
     * @param r
     * @param parser
     * @param sheet
     */
    public void process(XSSFReader r, XMLReader parser, InputStream sheet) {
        try {
            // 根据 rId# 或 rSheet# 查找sheet
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        } catch (IOException | SAXException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 遍历工作簿中所有的电子表格
     *
     * @param stream
     * @throws Exception
     */
    public void processAll(InputStream stream) {
        try {
            OPCPackage pkg = OPCPackage.open(stream);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                curRow = 0;
                sheetIndex++;
                InputStream sheet = sheets.next();
                process(r, parser, sheet);
            }
        } catch (IOException | OpenXML4JException | SAXException e) {
            logger.error(e.getMessage());
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) {
        // c => 单元格
        if ("c".equals(name)) {
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            if ("s".equals(cellType)) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
            // 日期格式
            String cellDateType = attributes.getValue("s");
            if ("1".equals(cellDateType)) {
                dateFlag = true;
            } else {
                dateFlag = false;
            }
            String cellNumberType = attributes.getValue("s");
            if ("2".equals(cellNumberType)) {
                numberFlag = true;
            } else {
                numberFlag = false;
            }
        }
        // 当元素为t时
        if ("t".equals(name)) {
            isTElement = true;
        } else {
            isTElement = false;
        }

        // 置空
        lastContents = "";
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            } catch (Exception e) {

            }
        }
        // t元素也包含字符串
        if (isTElement) {
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            isTElement = false;
            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        } else if ("v".equals(name)) {
            String value = lastContents.trim();
            value = value.equals("") ? " " : value;
            // 日期格式处理
            if (dateFlag) {
                Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                value = dateFormat.format(date);
            }
            // 数字类型处理
            if (numberFlag) {
                BigDecimal bd = new BigDecimal(value);
                value = bd.setScale(3, BigDecimal.ROUND_UP).toString();
            }
            rowlist.add(curCol, value);
            curCol++;
        } else {
            // 如果标签名称为 row ，这说明已到行尾
            if (name.equals("row")) {
                if (rowReader != null) { // 每行结束时， 调用getRows() 方法
                    rowReader.getRows(sheetIndex, curRow, rowlist);
                }
                rowlist.clear();
                curRow++;
                curCol = 0;
            }
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到单元格内容的值
        lastContents += new String(ch, start, length);
    }
}
