package com.example.excel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.excel.vo.ExcelTestVO;

@Repository("com.example.excel.mapper.ExcelTestMapper")
public interface ExcelTestMapper {

	public int dataCount() throws Exception;
	
	public List<ExcelTestVO> dataList(Map<String, Integer> map) throws Exception;
	
	public List<ExcelTestVO> allDataList() throws Exception;
	
}
