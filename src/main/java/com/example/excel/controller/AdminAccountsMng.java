package com.example.excel.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.example.excel.service.AdminAccountsMngService;
import com.example.excel.vo.ExcelTestVO;


@Controller
@RequestMapping(value = "/accountsMngs")
public class AdminAccountsMng {

	//private static final Logger logger = (Logger) LoggerFactory.getLogger(AdminAccountsMng.class);

	@Autowired
	AdminAccountsMngService adminAccountsMngService;

	@RequestMapping(value = "/excelUploadPage", method = RequestMethod.GET)
	public String serviceMngForm(Model model, Principal principal, Map paramMap) {
		model.addAttribute("userNm", principal.getName());
		model.addAttribute("menuTarget", paramMap.get("menuTarget"));
		return "admin/accountsMng/excelUploadPage";
	}

	@RequestMapping(value = "/compExcelUpload")
	public ModelAndView excelUpload(MultipartHttpServletRequest req) {
		ModelAndView mav = new ModelAndView("testDataList");
		List<ExcelTestVO> list = new ArrayList<>();
		// 엑셀 파일이 xls일때와 xlsx일때 서비스 라우팅
		String excelType = req.getParameter("excelType");
		if (excelType.equals("xlsx")) {
			list = adminAccountsMngService.xlsxExcelReader(req);
		} else if (excelType.equals("xls")) {
			list = adminAccountsMngService.xlsExcelReader(req);
		}
		mav.addObject("list", list);
		return mav;
	}
}
