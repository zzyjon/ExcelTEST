package com.example.excel.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.excel.mapper.ExcelTestMapper;
import com.example.excel.vo.ExcelTestVO;

@Service
public class ExcelTestService {

	@Autowired
	ExcelTestMapper excelTestMapper;
	
	public int dataCount() throws Exception {
		return excelTestMapper.dataCount();
	}
	
	public List<ExcelTestVO> dataList(Map<String, Integer> map) throws Exception {
		return excelTestMapper.dataList(map);
	};
	
	public List<ExcelTestVO> allDataList() throws Exception {
		return excelTestMapper.allDataList();
	};
}
