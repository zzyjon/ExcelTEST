package com.example.excel.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.excel.vo.ExcelTestVO;

@Repository("com.example.excel.mapper.ExcelUploadTestMapper")
public interface ExcelUploadTestMapper {

	void insertExcelTest(List<ExcelTestVO> list);

}
