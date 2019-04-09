package com.example.excel.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.example.excel.common.ExcelView;
import com.example.excel.common.ExceladdRowData;
import com.example.excel.common.MakeExcel;
import com.example.excel.service.ExcelTestService;
import com.example.excel.service.ExcelUploadTestService;
import com.example.excel.vo.ExcelTestVO;

@Controller
public class ExcelTestController {

	@Autowired
	ExcelTestService excelTestService;
	
	@Autowired
	ExcelUploadTestService excelUploadTestService;
	
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
	
	
	@RequestMapping(value = "/jxlsDownload")
	public void jxlsTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long start = System.currentTimeMillis(); //시작하는 시점 계산
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startRow", 0);
		map.put("viewRow", 300000);
		
		List<ExcelTestVO> list = excelTestService.dataList(map);
		
		String tempPath = request.getSession().getServletContext().getRealPath("/WEB-INF/template/");
		FileInputStream fileInputStream = new FileInputStream(tempPath + "Book2.xlsx");
		BufferedInputStream bufferedInputStream = new  BufferedInputStream(fileInputStream);
		
		try (InputStream is = bufferedInputStream) {
			//엑셀파일로 다운로드위한 response 객체 세팅
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "jxlsTestDown" + ".xlsx");
			try (OutputStream os = response.getOutputStream()) {
				//컨텍스트 객체 생성 및 세팅
			    //context 속성에 컨텍스트명과 엑셀에 쓰일 데이터를 Key & Value로 세팅
			    //여기서 contextName ("excelDataList")은 엑셀 템플릿파일에서 items="컨텍스트명"과 반드시 일치해야함
				Context context = new Context();
				context.putVar("list", list);
				JxlsHelper.getInstance().processTemplate(is, os, context);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
	    System.out.println( "jxls download 실행 시간 : " + ( end - start )/1000.0 +"초"); //실행 시간 계산 및 출력
		
	}
	
	@RequestMapping(value = "/poiDown")
	public void excelDown(HttpServletResponse response) throws Exception {
		
		long start = System.currentTimeMillis(); //시작하는 시점 계산
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startRow", 1);
		//map.put("viewRow", excelTestService.dataCount());
		map.put("viewRow", 300000);
	    // 데이터 목록조회
	    List<ExcelTestVO> list = excelTestService.dataList(map);

	    // 워크북 생성
	    //HSSFWorkbook wb = new HSSFWorkbook(); // xls 형식
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
	    response.setHeader("Content-Disposition", "attachment;filename=test_poi.xls");

	    // 엑셀 출력
	    wb.write(response.getOutputStream());
	    wb.close();
	    
	    long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
	    System.out.println( "poi download 실행 시간 : " + ( end - start )/1000.0 +"초"); //실행 시간 계산 및 출력

	}
	
	
	@RequestMapping(value = "/MaxPoiDown")
	public void MaxExcelDown(HttpServletResponse response) throws Exception {
		
		long start = System.currentTimeMillis(); //시작하는 시점 계산
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startRow", 0);
		//map.put("viewRow", excelTestService.dataCount());
		map.put("viewRow", 10000);
		
	    // 데이터 목록조회
	    List<ExcelTestVO> list = excelTestService.dataList(map);

	    // 워크북 생성
	    //Workbook wb = new HSSFWorkbook(); // xls 형식
	    XSSFWorkbook wb = new XSSFWorkbook(); // xlsx 형식
	    
	    Sheet sheet = wb.createSheet("DATA LIST"); // 시트 이름
	    Row row = null;
	    Cell cell = null;
	    int rowNo = 0;
	    
	    // 헤더 생성
	    row = sheet.createRow(rowNo);
	    cell = row.createCell(0);
	    cell.setCellValue("번호");
	    cell = row.createCell(1);
	    cell.setCellValue("이름");
	    cell = row.createCell(2);
	    cell.setCellValue("이메일");
	    cell = row.createCell(3);
	    cell.setCellValue("등록일시");
	    
	    SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(wb, 1000);
	    sheet = sxssfWorkbook.getSheetAt(cell.getRowIndex());
	    
	    ExceladdRowData exceladdRowData = new ExceladdRowData();
	    exceladdRowData.addRowDataTest(list, sheet, rowNo);
	    exceladdRowData.writeResponse("excelMax.xlsx", response, sxssfWorkbook);
	    
	    long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산
	    System.out.println( "poi 대용량  다운로드 실행 시간 : " + ( end - start )/1000.0 +"초"); //실행 시간 계산 및 출력

	}
	
	
	@RequestMapping(value = "/ExcelUpload")
	public ModelAndView excelUpload(MultipartHttpServletRequest req) {
		ModelAndView mav = new ModelAndView("testDataList");
		List<ExcelTestVO> list = new ArrayList<>();
		// 엑셀 파일이 xls일때와 xlsx일때 서비스 각각 다른 처리
		String excelType = req.getParameter("excelType");
		if (excelType.equals("xlsx")) {
			list = excelUploadTestService.xlsxExcelReader(req);
		} else if (excelType.equals("xls")) {
			list = excelUploadTestService.xlsExcelReader(req);
		}
		mav.addObject("list", list);
		return mav;
	}
	

}
