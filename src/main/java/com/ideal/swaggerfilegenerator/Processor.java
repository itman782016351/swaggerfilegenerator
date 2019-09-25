package com.ideal.swaggerfilegenerator;

import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author zhaopei
 * @create 2019-09-20 11:18
 */
public class Processor {

    public void process(File f) throws IOException, TemplateException {
        if (f.getName().endsWith(".xlsx")) {
            Map map = getRenderMap(f);
            FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
            String result = freemarkerUtil.render("swagger.ftl", map);
            String execelPath = f.getPath();
            File target = new File(execelPath.substring(0, execelPath.lastIndexOf(".xlsx")) + ".yaml");
            if (target.exists()) {
                target.delete();
            }
            PrintWriter pw = new PrintWriter(target);
            pw.print(result);
            pw.flush();
            pw.close();
        }
    }

    public Map getRenderMap(File f) throws IOException {
        Map map = new HashMap<>();
        Workbook wb = new XSSFWorkbook(new FileInputStream(f));
        Sheet sheet = wb.getSheetAt(0);
        int reqStart = 0;
        int reqEnd = 0;
        int respStart = 0;
        int respEnd = sheet.getLastRowNum();
        Map fixInfo = new HashMap();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row.getCell(0).getStringCellValue().equals("输入")) {
                reqStart = i;
            } else if (row.getCell(0).getStringCellValue().equals("输出")) {
                respStart = i;
            }
            reqEnd = respStart - 1;
            //处理固定信息
            String cell1Val = commProccssStr(row.getCell(1).getStringCellValue());
            String cell2Val = commProccssStr(row.getCell(2).getStringCellValue());
            switch (cell1Val) {
                case "服务名":
                    fixInfo.put("serverName", cell2Val);
                    break;
                case "服务版本":
                    fixInfo.put("serverVersion", cell2Val);
                    break;
                case "API名称":
                    fixInfo.put("apiName", cell2Val);
                    break;
                case "API描述":
                    fixInfo.put("apiDesc", cell2Val);
                    break;
                case "EOP基础路径":
                    fixInfo.put("eopBasePath", cell2Val);
                    break;
                case "资源动作":
                    fixInfo.put("httpMethod", StringUtils.upperCase(cell2Val));
                    break;
                case "资源地址":
                    fixInfo.put("resourcePath", cell2Val);
                    break;
                case "提供方Host（测试）":
                    fixInfo.put("providerHost4Test", cell2Val);
                    break;
                case "提供方basePath（测试）":
                    fixInfo.put("providerBasePath4Test", cell2Val);
                    break;
                default:
                    break;
            }
        }
        //System.out.println(reqStart+" "+ reqEnd+" "+respStart+" "+ respEnd);
        map.put("fixInfo", fixInfo);
        //开始处理入参和出参
        List<EntityNode> reqList = new ArrayList<>();
        List<EntityNode> respList = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String cell1Val = commProccssStr(row.getCell(1).getStringCellValue());
            String cell2Val = commProccssStr(row.getCell(2).getStringCellValue());
            String cell3Val = commProccssStr(row.getCell(3).getStringCellValue());
            String cell4Val = commProccssStr(row.getCell(4).getStringCellValue());
            if (StringUtils.isEmpty(cell4Val)) {
                cell4Val = "''";
            }
            EntityNode entityNode;
            //处理入参信息
            if (i >= reqStart && i <= reqEnd) {
                entityNode = new EntityNode(cell1Val, cell2Val, collectionJudge(cell3Val), cell4Val, requiredJudge(cell3Val));
                reqList.add(entityNode);
            }
            processLeafProp(reqList);
            if (i >= respStart && i <= respEnd) {
                boolean dupflag = false;
                for (EntityNode node : reqList) {
                    if (cell2Val.equals(node.getPropertyName()) && !node.isLeafNode()) {
                        dupflag = true;
                        break;
                    }
                }
                if (dupflag) {
                    continue;
                }
                entityNode = new EntityNode(cell1Val, cell2Val, collectionJudge(cell3Val), cell4Val, requiredJudge(cell3Val));
                respList.add(entityNode);
            }
        }
        processLeafProp(respList);
        afterProcess(reqList, "req");
        afterProcess(respList, "resp");
        map.put("reqList", reqList);
        map.put("respList", respList);
        return map;
    }

    public boolean collectionJudge(String para) {
        boolean result = false;
        switch (para) {
            case "1":
                result = false;
                break;
            case "1-n":
                result = true;
                break;
            case "0-n":
                result = true;
                break;
            case "0-1":
                result = false;
                break;
            default:
                break;
        }
        return result;
    }

    public boolean requiredJudge(String para) {
        boolean result = false;
        switch (para) {
            case "1":
                result = true;
                break;
            case "1-n":
                result = true;
                break;
            case "0-n":
                result = false;
                break;
            case "0-1":
                result = false;
                break;
            default:
                break;
        }
        return result;
    }

    public void processLeafProp(List<EntityNode> list) {
        for (EntityNode entityNode : list) {
            String orderNo = entityNode.getOrderNo();
            entityNode.setLeafNode(true);
            for (EntityNode node : list) {
                if (node.getParentOrderNo().equals(orderNo)) {
                    entityNode.setLeafNode(false);
                    break;
                }
            }
        }
    }

    public String commProccssStr(String source) {
        String temp = StringUtils.replace(source, "\n", "");
        return StringUtils.strip(temp);
    }

    public void afterProcess(List<EntityNode> list, String str) {
        boolean flag = false;
        Iterator<EntityNode> iter = list.iterator();
        while (iter.hasNext()) {
            EntityNode entityNode = iter.next();
            if ("".equals(entityNode.getParentOrderNo()) && entityNode.isLeafNode()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            Iterator<EntityNode> iterator = list.iterator();
            while (iterator.hasNext()) {
                EntityNode entityNode = iterator.next();
                entityNode.setOrderNo("1." + entityNode.getOrderNo());
                if ("".equals(entityNode.getParentOrderNo())) {
                    entityNode.setParentOrderNo("1");
                } else {
                    entityNode.setParentOrderNo("1." + entityNode.getParentOrderNo());
                }
            }
            EntityNode tempEntityNode = new EntityNode("1", "", false, "", true);
            if ("req".equals(str)) {
                tempEntityNode.setPropertyName("req");
                list.add(tempEntityNode);
            } else if ("resp".equals(str)) {
                tempEntityNode.setPropertyName("resp");
                list.add(tempEntityNode);
            }
        }

    }
}
