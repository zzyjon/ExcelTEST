package com.example.excel.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.example.excel.common.ExcelView;
import com.example.excel.common.ExceladdRowData;
import com.example.excel.common.MakeExcel;
import com.example.excel.service.ExcelTestService;
import com.example.excel.vo.ExcelTestVO;

@Controller
public class ExcelTestController {

	@Autowired
	ExcelTestService excelTestService;
	
	@RequestMapping("/")
	public String getDataList(Model model, @RequestParam(value="currPage", defaultValue="1") int currPage) throws Exception {
		
		int totalRow = excelTestService.dataCount();
		int viewRow = 30;
		int startRow = (currPage - 1) * viewRow;
		int totalPage = totalRow/viewRow + ((totalRow % viewRow > 0)? 1:0);
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startRow", startRow);
		map.put("viewRow", viewRow);
		
		model.addAttribute("dataCount", excelTestService.dataCount());
		model.addAttribute("dataList", excelTestService.dataList(map));
		model.addAttribute("currPage", currPage);
		model.addAttribute("totalPage", totalPage);
		
		return "testDataList";
	}
	
	@RequestMapping(value = "/jexelDown")
	public void excelDown(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(value="currPage", defaultValue="1") int currPage) throws Exception {
		int viewRow = 30;
		int startRow = (currPage - 1) * viewRow;
		
		Map<String, Object> jxlsmap = new HashMap<>();
		Map<String, Integer> map = new HashMap<>();
		
		map.put("startRow", startRow);
		map.put("viewRow", viewRow);
		
		jxlsmap.put("listview", excelTestService.dataList(map));
		
		MakeExcel makeExcel = new MakeExcel();
		makeExcel.download(request, response, jxlsmap, makeExcel.get_Filename("jexcel_down"), "jxlsTemplate.xlsx");
	}
	
	@RequestMapping(value = "/poiDown")
	public void excelDown(HttpServletResponse response) throws Exception {
		
		long start = System.currentTimeMillis(); //시작하는 시점 계산
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startRow", 1);
		map.put("viewRow", excelTestService.dataCount());
		//map.put("viewRow", 65535);
	    // 데이터 목록조회
	    List<ExcelTestVO> list = excelTestService.dataList(map);

	    // 워크북 생성
	    //Workbook wb = new HSSFWorkbook(); // xls 형식
	    XSSFWorkbook wb = new XSSFWorkbook(); // xlsx 형식
	    Sheet sheet = wb.createSheet("DATA LIST"); // 시트 이름
	    Row row = null;
	    Cell cell = null;
	    int rowNo = 0;

	    // 테이블 헤더용 스타일
	    CellStyle headStyle = wb.createCellStyle();

	    // 가는 경계선을 가집니다.
	    headStyle.setBorderTop(BorderStyle.THIN);
	    headStyle.setBorderBottom(BorderStyle.THIN);
	    headStyle.setBorderLeft(BorderStyle.THIN);
	    headStyle.setBorderRight(BorderStyle.THIN);

	    // 배경색은 노란색입니다.
	    headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
	    headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    // 데이터는 가운데 정렬합니다.
	    headStyle.setAlignment(HorizontalAlignment.CENTER);

	    // 데이터용 경계 스타일 테두리만 지정
	    CellStyle bodyStyle = wb.createCellStyle();
	    bodyStyle.setBorderTop(BorderStyle.THIN);
	    bodyStyle.setBorderBottom(BorderStyle.THIN);
	    bodyStyle.setBorderLeft(BorderStyle.THIN);
	    bodyStyle.setBorderRight(BorderStyle.THIN);

	    // 헤더 생성
	    row = sheet.createRow(rowNo++);
	    cell = row.createCell(0);
	    cell.setCellStyle(headStyle);
	    cell.setCellValue("번호");
	    cell = row.createCell(1);
	    cell.setCellStyle(headStyle);
	    cell.setCellValue("이름");
	    cell = row.createCell(2);
	    cell.setCellStyle(headStyle);
	    cell.setCellValue("이메일");
	    cell = row.createCell(3);
	    cell.setCellStyle(headStyle);
	    cell.setCellValue("등록일시");

	    // 데이터 부분 생성
	    for(ExcelTestVO vo : list) {
	        row = sheet.createRow(rowNo++);
	        cell = row.createCell(0);
	        cell.setCellStyle(bodyStyle);
	        cell.setCellValue(vo.getMytestId());
	        cell = row.createCell(1);
	        cell.setCellStyle(bodyStyle);
	        cell.setCellValue(vo.getName());
	        cell = row.createCell(2);
	        cell.setCellStyle(bodyStyle);
	        cell.setCellValue(vo.getEmail());
	        cell = row.createCell(3);
	        cell.setCellStyle(bodyStyle);
	        cell.setCellValue(vo.getRegDate());
	    }

	    // 컨텐츠 타입과 파일명 지정
	    response.setContentType("ms-vnd/excel");
	    response.setHeader("Content-Disposition", "attachment;filename=test_poi.xlsx");

	    // 엑셀 출력
	    wb.write(response.getOutputStream());
	    wb.close();
	    
	    long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
	    System.out.println( "poi download 실행 시간 : " + ( end - start )/1000.0 +"초"); //실행 시간 계산 및 출력

	}
	
	@RequestMapping("/jxlsDown")
	public View reportExcelDownload2(final HttpServletResponse response,
			@RequestParam(value = "reqData", required = false, defaultValue = "") final String reqData, final Model model) throws Exception {

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("startRow", 1);
			map.put("viewRow", 5000);
			
			List<ExcelTestVO> allDataList = excelTestService.dataList(map);
			
			model.addAttribute("list", allDataList);
			model.addAttribute("count", allDataList.size());
			model.addAttribute("DownloadDate", sdf.format(new Date()));
	

		} catch (Exception e) {
		e.printStackTrace();
		}
		return new ExcelView();
	}
	
	
	@RequestMapping(value = "/MaxPoiDown")
	public void MaxExcelDown(HttpServletResponse response) throws Exception {
		
		long start = System.currentTimeMillis(); //시작하는 시점 계산
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startRow", 1);
		map.put("viewRow", excelTestService.dataCount());
		//map.put("viewRow", 65535);
	    // 데이터 목록조회
	    List<ExcelTestVO> list = excelTestService.dataList(map);

	    // 워크북 생성
	    //Workbook wb = new HSSFWorkbook(); // xls 형식
	    XSSFWorkbook wb = new XSSFWorkbook(); // xlsx 형식
	    Sheet sheet = wb.createSheet("DATA LIST"); // 시트 이름
	    int LastRowNo = sheet.getLastRowNum();
	    Row row = null;
	    Cell cell = null;
	    int rowNo = 0;
	    
	    // 헤더 생성
	    row = sheet.createRow(rowNo++);
	    cell = row.createCell(0);
	    cell.setCellValue("번호");
	    cell = row.createCell(1);
	    cell.setCellValue("이름");
	    cell = row.createCell(2);
	    cell.setCellValue("이메일");
	    cell = row.createCell(3);
	    cell.setCellValue("등록일시");
	    
	    
	    SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(wb, 1000);
	    sheet = sxssfWorkbook.getSheetAt(LastRowNo);
	    
	    ExceladdRowData exceladdRowData = new ExceladdRowData();
	    exceladdRowData.addRowDataTest(list, sheet, LastRowNo);
	    
	    exceladdRowData.writeResponse("excelMax.xlsx", response, sxssfWorkbook);

	    // 엑셀 출력
	    wb.write(response.getOutputStream());
	    wb.close();
	    
	    long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
	    System.out.println( "poi download 실행 시간 : " + ( end - start )/1000.0 +"초"); //실행 시간 계산 및 출력

	}

}
