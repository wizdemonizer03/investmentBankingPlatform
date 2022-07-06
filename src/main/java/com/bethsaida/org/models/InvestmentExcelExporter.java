package com.bethsaida.org.models;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


public class InvestmentExcelExporter {
	
	    private XSSFWorkbook workbook;
	    private XSSFSheet sheet;
	    private List<Investment> listInvestments;
	     
	    public InvestmentExcelExporter(List<Investment> listInvestments) {
	        this.listInvestments = listInvestments;
	        workbook = new XSSFWorkbook();
	    }
	 
	 
	    private void writeHeaderLine() {
	        sheet = workbook.createSheet("Investments");
	         
	        Row row = sheet.createRow(0);
	         
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        font.setFontHeight(16);
	        style.setFont(font);
	         
	        createCell(row, 0, "Account Number", style);      
	        createCell(row, 1, "Category", style);       
	        createCell(row, 2, "Principal", style);    
	        createCell(row, 3, "Marketer", style);
	        createCell(row, 4, "Maturity Interest", style);
	        createCell(row, 5, "Tax Witholding", style);
	        createCell(row, 6, "Status", style);
	        createCell(row, 7, "Tenure", style);
	        createCell(row, 8, "Start Date", style);
	        createCell(row, 9, "Maturity Date", style);
	        
	    }
	     
	    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
	        sheet.autoSizeColumn(columnCount);
	        Cell cell = row.createCell(columnCount);
	        if (value instanceof Integer) {
	            cell.setCellValue((Integer) value);
	        } else if (value instanceof Boolean) {
	            cell.setCellValue((Boolean) value);
	         } else {
	            cell.setCellValue((String) value);
	        }
	        cell.setCellStyle(style);
	    }
	     
	    private void writeDataLines() {
	        int rowCount = 1;
	 
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setFontHeight(14);
	        style.setFont(font);
	                 
	        for (Investment investment : listInvestments) {
	            Row row = sheet.createRow(rowCount++);
	            int columnCount = 0;
	             
	            createCell(row, columnCount++, investment.getAccountNumber(), style);
	            createCell(row, columnCount++, investment.getCategory(), style);
	            createCell(row, columnCount++, investment.getPrincipal(), style);
	            createCell(row, columnCount++, investment.getCustomer_id().getMarketer(), style);
	            createCell(row, columnCount++, investment.getMaturityInterest(), style);
	            createCell(row, columnCount++, investment.getTaxWitholding(), style);
	            createCell(row, columnCount++, investment.getStatus(), style);
	            createCell(row, columnCount++, investment.getTenure(), style);
	            createCell(row, columnCount++, investment.getStartDate(), style);
	            createCell(row, columnCount++, investment.getMaturityDate(), style);
	        }
	    }
	     
	    
	    public void export(HttpServletResponse response) throws IOException {
	        writeHeaderLine();
	        writeDataLines();
	         
	        ServletOutputStream outputStream = response.getOutputStream();
	        workbook.write(outputStream);
	        workbook.close();
	         
	        outputStream.close();
	         
	    }
	}


