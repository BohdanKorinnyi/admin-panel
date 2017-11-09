package com.omnia.admin.service.impl;

import com.omnia.admin.dto.StatFilter;
import com.omnia.admin.model.BuyerStatistic;
import com.omnia.admin.model.Statistic;
import com.omnia.admin.service.ExcelReportService;
import com.omnia.admin.service.StatisticService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

@Log4j
@Service
@AllArgsConstructor
public class StatsExcelReportServiceImpl implements ExcelReportService {

    private static final String SHEET_NAME = "Buyer's statistics report";
    private static final String BUYER_REPORT_NAME = "Buyer: %s";
    private static final String TOTAL_BUYER_SPENT = "Total by buyer %s: %s";

    private final StatisticService statisticService;


    @Override
    public Object create(StatFilter filter) {
        Map<Integer, BuyerStatistic> stats = statisticService.getAllStatistics(filter);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
            int rowNumber = 0;
            for (Map.Entry<Integer, BuyerStatistic> entry : stats.entrySet()) {
                BuyerStatistic buyerStatistic = entry.getValue();
                rowNumber = aheadRow(sheet, buyerStatistic.getBuyerName(), rowNumber);
                rowNumber = createRows(sheet, buyerStatistic, rowNumber);
                rowNumber = resultBuyerRow(sheet, buyerStatistic.getBuyerName(), buyerStatistic.getBuyerTotalSpent(), rowNumber);
            }

            FileOutputStream out = new FileOutputStream(new File("test.xlsx"));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            log.error("Error occurred during filling excel sheet", e);
        }
        return null;
    }

    private int createRows(XSSFSheet sheet, BuyerStatistic buyerStats, int rowNumber) {
        for (Statistic statistic : buyerStats.getStatistics()) {
            XSSFRow row = sheet.createRow(rowNumber);
            rowNumber = fillStatsRow(row, statistic, rowNumber);
        }
        return rowNumber;
    }

    private int fillStatsRow(XSSFRow row, Statistic statistic, int rowNumber) {
        XSSFCell date = row.createCell(0);
        XSSFCell accountType = row.createCell(1);
        XSSFCell campaign = row.createCell(2);
        XSSFCell accountHolder = row.createCell(3);
        XSSFCell spent = row.createCell(4);

        date.setCellValue(statistic.getDate());
        accountType.setCellValue(statistic.getAccountType());
        campaign.setCellValue(statistic.getCampaignName());
        accountHolder.setCellValue(statistic.getUsername());
        spent.setCellValue(statistic.getSpent());

        return rowNumber + 1;
    }

    private int aheadRow(XSSFSheet sheet, String buyerName, int rowNumber) {
        XSSFRow row = sheet.createRow(rowNumber);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(String.format(BUYER_REPORT_NAME, buyerName));
        return rowNumber + 1;
    }

    private int resultBuyerRow(XSSFSheet sheet, String buyerName, Double totalSpent, int rowNumber) {
        XSSFRow row = sheet.createRow(rowNumber);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(String.format(TOTAL_BUYER_SPENT, buyerName, totalSpent));
        return rowNumber + 1;
    }
}
