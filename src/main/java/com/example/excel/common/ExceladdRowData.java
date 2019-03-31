package com.example.excel.common;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.example.excel.vo.ExcelTestVO;

public class ExceladdRowData {

	public void addRowDataTest(List<ExcelTestVO> excelDataList, Sheet sheet, int rowNo) throws IOException {
	    // 엑셀 row 생성
	    for(ExcelTestVO vo : excelDataList) {
	        Row row = sheet.createRow(++rowNo);

	        // 엑셀 cell 생성 및 값 주입
	        Cell cell = row.createCell(0);
	        cell.setCellValue(vo.getMytestId());
	        cell = row.createCell(1);
	        cell.setCellValue(vo.getName());
	        cell = row.createCell(2);
	        cell.setCellValue(vo.getEmail());
	        cell = row.createCell(3);
	        cell.setCellValue(vo.getRegDate());
	        
	    }
	    // 디스크로 flush
	    ((SXSSFSheet)sheet).flushRows(excelDataList.size());
	}
	
	
	public void writeResponse(String downloadFileName, HttpServletResponse response, SXSSFWorkbook sxssfWorkbook) throws IOException {
	    response.setContentType("application/msexcel");
	    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(downloadFileName,"UTF-8")));
	    try {
	    	sxssfWorkbook.write(response.getOutputStream());
	    }catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}finally {
			// 디스크에 임시파일로 저장한 파일 삭제
			sxssfWorkbook.dispose();
			sxssfWorkbook.close();// close 를 안하면 엑셀 데이터가 깨짐
		}

	}

}
