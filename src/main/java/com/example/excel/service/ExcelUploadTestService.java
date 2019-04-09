package com.example.excel.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.excel.mapper.ExcelUploadTestMapper;
import com.example.excel.vo.ExcelTestVO;

@Service
public class ExcelUploadTestService {

	@Autowired
	ExcelUploadTestMapper uploadTestMapper;

	public List<ExcelTestVO> xlsExcelReader(MultipartHttpServletRequest req) {
		// 반환할 객체를 생성
		List<ExcelTestVO> list = new ArrayList<>();

		MultipartFile file = req.getFile("excel");
		HSSFWorkbook workbook = null;

		try {
			// HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
			workbook = new HSSFWorkbook(file.getInputStream());

			// 탐색에 사용할 Sheet, Row, Cell 객체
			HSSFSheet curSheet;
			HSSFRow curRow;
			HSSFCell curCell;
			ExcelTestVO vo;

			// Sheet 탐색 for문
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				// 현재 sheet 반환
				curSheet = workbook.getSheetAt(sheetIndex);
				// row 탐색 for문
				for (int rowIndex = 0; rowIndex < curSheet.getPhysicalNumberOfRows(); rowIndex++) {
					// row 0은 헤더정보이기 때문에 무시
					if (rowIndex != 0) {
						curRow = curSheet.getRow(rowIndex);
						vo = new ExcelTestVO();
						String value;

						// row의 첫번째 cell값이 비어있지 않는 경우만 cell탐색
						if (curRow.getCell(0) != null) {
							if (!"".equals(curRow.getCell(0).getStringCellValue())) {
								// cell 탐색 for문
								for (int cellIndex = 0; cellIndex < curRow.getPhysicalNumberOfCells(); cellIndex++) {
									curCell = curRow.getCell(cellIndex);

									if (true) {
										// cell 스타일이 다르더라도 String으로 반환 받음
										switch (curCell.getCellType()) {
										case FORMULA :
											value = curCell.getCellFormula();
											break;
										case NUMERIC :
											value = curCell.getNumericCellValue() + "";
											break;
										case STRING :
											value = curCell.getStringCellValue() + "";
											break;
										case BLANK :
											value = curCell.getBooleanCellValue() + "";
											break;
										case ERROR :
											value = curCell.getErrorCellValue() + "";
											break;
										default:
											value = new String();
											break;
										} // end switch
										// 현재 colum index에 따라서 vo입력
										switch (cellIndex) {
										case 0: // 이름
											vo.setName(curCell.getStringCellValue());
											break;
										case 1: // 이메일
											vo.setEmail(curCell.getStringCellValue());
											break;
										default:
											break;
										}
									} // end if
								} // end for
									// cell 탐색 이후 vo 추가
								list.add(vo);
							} // end
						} // end if
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 디비에 insert
		uploadTestMapper.insertExcelTest(list);
		return list;
	}

	
	
	/**
	* 1. MethodName	: xlsxExcelReader
	* 2. ClassName	: AdminAccountsMngService
	* 3. Commnet	: XLLX파일을 분석하여 List<ExcelTestVO>객체로 반환
	 *
	 * @return List<ExcelTestVO>
	 * @param req
	 * @return
	 */
	public List<ExcelTestVO> xlsxExcelReader(MultipartHttpServletRequest req) {
		// 반환할 객체를 생성
		List<ExcelTestVO> list = new ArrayList<>();

		MultipartFile file = req.getFile("excel");
		XSSFWorkbook workbook = null;

		try {
			// HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
			workbook = new XSSFWorkbook(file.getInputStream());

			// 탐색에 사용할 Sheet, Row, Cell 객체
			XSSFSheet curSheet;
			XSSFRow curRow;
			XSSFCell curCell;
			ExcelTestVO vo;

			// Sheet 탐색 for문
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				// 현재 sheet 반환
				curSheet = workbook.getSheetAt(sheetIndex);
				// row 탐색 for문
				for (int rowIndex = 0; rowIndex < curSheet.getPhysicalNumberOfRows(); rowIndex++) {
					// row 0은 헤더정보이기 때문에 무시
					if (rowIndex != 0) {
						curRow = curSheet.getRow(rowIndex);
						vo = new ExcelTestVO();
						String value;

						// row의 첫번째 cell값이 비어있지 않는 경우만 cell탐색
						if (curRow.getCell(0) != null) {
							if (!"".equals(curRow.getCell(0).getStringCellValue())) {
								// cell 탐색 for문
								for (int cellIndex = 0; cellIndex < curRow.getPhysicalNumberOfCells(); cellIndex++) {
									curCell = curRow.getCell(cellIndex);

									if (true) {
										// cell 스타일이 다르더라도 String으로 반환 받음
										switch (curCell.getCellType()) {
										case FORMULA :
											value = curCell.getCellFormula();
											break;
										case NUMERIC :
											value = curCell.getNumericCellValue() + "";
											break;
										case STRING :
											value = curCell.getStringCellValue() + "";
											break;
										case BLANK :
											value = curCell.getBooleanCellValue() + "";
											break;
										case ERROR :
											value = curCell.getErrorCellValue() + "";
											break;
										default:
											value = new String();
											break;
										} // end switch
										// 현재 colum index에 따라서 vo입력
										switch (cellIndex) {
										case 0: // 이름
											vo.setName(curCell.getStringCellValue());
											break;
										case 1: // 이메일
											vo.setEmail(curCell.getStringCellValue());
											break;
										default:
											break;
										}
									} // end if
								} // end for
									// cell 탐색 이후 vo 추가
								list.add(vo);
							} // end
						} // end if
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 디비에 insert
		uploadTestMapper.insertExcelTest(list);
		return list;

	}
}
