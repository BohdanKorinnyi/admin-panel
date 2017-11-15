package com.omnia.admin.service.impl;

import com.google.common.collect.ImmutableList;
import com.omnia.admin.dto.StatisticFilter;
import com.omnia.admin.model.statistic.SourcesResult;
import com.omnia.admin.model.Source;
import com.omnia.admin.service.ExcelReportService;
import com.omnia.admin.service.SourceStatsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

@Log4j
@Service
@AllArgsConstructor
public class StatsExcelReportServiceImpl implements ExcelReportService {
    private static final String SHEET_NAME = "Buyer's sourceStatistics report";
    private static final String BUYER_REPORT_NAME = "Buyer: %s";
    private static final String TOTAL_BUYER_SPENT = "Total by buyer %s";
    private static final List<String> COLUMNS = ImmutableList.of("Date", "Source", "Campaign Name",
            "Account Holder", "Spent");

    private final SourceStatsService sourceStatsService;

    @Override
    public File create(StatisticFilter filter) {
        Map<Integer, SourcesResult> stats = sourceStatsService.getDailyAndGeneralStatistics(filter);
        File report = null;
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < COLUMNS.size(); i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNS.get(i));
            }
            int rowNumber = 1;
            for (Map.Entry<Integer, SourcesResult> entry : stats.entrySet()) {
                SourcesResult sourcesResult = entry.getValue();
                rowNumber = aheadRow(sheet, sourcesResult.getName(), rowNumber);
                rowNumber = createRows(sheet, sourcesResult, rowNumber);
                rowNumber = resultBuyerRow(sheet, sourcesResult.getName(), sourcesResult.getSum(), rowNumber);
            }
            report = new File("report.xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(report)) {
                workbook.write(outputStream);
            }
        } catch (Exception e) {
            log.error("Error occurred during filling excel sheet", e);
        }
        return report;
    }

    private int createRows(XSSFSheet sheet, SourcesResult buyerStats, int rowNumber) {
        for (Source source : buyerStats.getData()) {
            XSSFRow row = sheet.createRow(rowNumber);
            rowNumber = fillStatsRow(row, source, rowNumber);
        }
        return rowNumber;
    }

    private int fillStatsRow(XSSFRow row, Source source, int rowNumber) {
        XSSFCell date = row.createCell(0);
        XSSFCell accountType = row.createCell(1);
        XSSFCell campaign = row.createCell(2);
        XSSFCell accountHolder = row.createCell(3);
        XSSFCell spent = row.createCell(4);

        date.setCellValue(source.getDate());
        accountType.setCellValue(source.getAccountType());
        campaign.setCellValue(source.getCampaignName());
        accountHolder.setCellValue(source.getUsername());
        spent.setCellValue(source.getSpent());

        return rowNumber + 1;
    }

    private int aheadRow(XSSFSheet sheet, String buyerName, int rowNumber) {
        XSSFRow row = sheet.createRow(rowNumber);
        sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, COLUMNS.size() - 1));
        XSSFCell cell = row.createCell(0);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.setCellValue(String.format(BUYER_REPORT_NAME, buyerName));
        return rowNumber + 1;
    }

    private int resultBuyerRow(XSSFSheet sheet, String buyerName, Double totalSpent, int rowNumber) {
        XSSFRow row = sheet.createRow(rowNumber);
        XSSFCell data = row.createCell(0);
        data.setCellValue(String.format(TOTAL_BUYER_SPENT, buyerName));
        data.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        XSSFCell spent = row.createCell(4);
        spent.setCellValue(totalSpent);
        sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, COLUMNS.size() - 2));
        return rowNumber + 1;
    }
}
