<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DATA LIST</title>
<!-- jQuery -->
<!-- <script src="//code.jquery.com/jquery.min.js"></script> -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<!-- easyui css -->
<link rel="stylesheet" href="/css/easyui.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script src="http://malsup.github.io/jquery.form.js"></script>
<script type="text/javascript" src="/js/jquery.paging.js"></script>
<script type="text/javascript" src="/js/jquery.bootpag.min.js"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#btnSubmit").click(function(){
		
		if($("#test").val() == ""  ){
			//alert("입력해주세요");

			//$('#test').trigger("click"); 

			// esayui에서 콜백에 focus 걸어줌
			var title = document.getElementById("test");
			$.messager.alert({
				title: 'My Title',
				msg: 'Here is a message!',
				fn: function(){
					//...
					title.focus();
				}
			});

			/* 
			// setTimeout() 에  focus()함수를 걸어주고 세미콜론으로 마침 후 다시 focus함수를 또 적용
			setTimeout(function(){
				title.focus();
				}, 1
			);
			
			title.focus(); */
			return false;
		}
	});
});

function testChange(obj){
	alert("돌아가라~포커스야~제발좀~");
	//setTimeout(function(){obj.focus();}, 1);
	setTimeout(function(){
		document.getElementById(obj).focus();
		validating = false;
	});

}

function checkFileType(filePath){

	var fileFormat = filePath.split(".");
	if (fileFormat.indexOf("xls") > -1) {
		return true;
	} else if (fileFormat.indexOf("xlsx") > -1) {
		return true;
	} else {
		return false;
	}
}

function check() {

	var file = $("#excel").val();
	if (file == "" || file == null) {
		alert("파일을 선택");
		return false;
	} else if (!checkFileType(file)) {
		alert("엑셀 파일만 업로드");
		return false;
	}
	var fileFormat = file.split(".");
	var fileType = fileFormat[1];
	if (confirm("업로드 하시겠습니까?")) {
		$("#excelUpForm").attr("action", "/accountsMngs/compExcelUpload");
		var options = {
			success : function(data) {
				alert("업로드 완료");
				//$("#ajax-content").html(data);
				location.reload();
			},
			beforeSend: function () {
	              var width = 0;
	              var height = 0;
	              var left = 0;
	              var top = 0;

	              width = 50;
	              height = 50;

	              top = ( $(window).height() - height ) / 2 + $(window).scrollTop();
	              left = ( $(window).width() - width ) / 2 + $(window).scrollLeft();

	              if($("#div_ajax_load_image").length != 0) {
	                     $("#div_ajax_load_image").css({
	                            "top": top+"px",
	                            "left": left+"px"
	                     });
	                     $("#div_ajax_load_image").show();
	              }
	              else {
	            	  $('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' + top + 'px; left:' + left + 'px; width:' + width + 'px; height:' + height + 'px; z-index:9999; background:#f0f0f0; filter:alpha(opacity=50); opacity:alpha*0.5; margin:auto; padding:0; "><img src="/img/loading.gif" style="width:50px; height:50px;"></div>');
	              }

	       },
	       complete: function () {
	                     $("#div_ajax_load_image").hide();
	       },
			type : "POST",
			data : {
				"excelType" : fileType
			}
		};
		$("form").ajaxSubmit(options);
	}
}
</script>
</head>
<body>
	<div><h2>the total number of <span style="color:red; font-size:15;" ></span></h2></div>
	
	<div class="container">
		<table class="table table-hover">
			<tr>
				<th>data id</th>
				<th>name</th>
				<th>eamil</th>
				<th>reg date</th>
			</tr>
		</table>
		<div id="page-selection"></div>
		<form id="excelUpForm" method="post" action="" role="form" enctype="multipart/form-data">
			<input id="excel" name="excel" class="file" type="file" multiple data-show-upload="false" data-show-caption="true">
			<button class="btn btn-secondary btn-lg btn-block" type="button" id="excelUp" onclick="check()">엑셀 등록</button>
		</form>
		
		<div>
			<input type="text" id="test" class="form-control" name="test">
		</div>
		
		<div style="margin-top: 1000px;">
			<button class="btn btn-primary" onclick="location.href='/jxlsDownload'">jxls 엑셀 다운로드</button>
			<button class="btn btn-warning" onclick="location.href='/poiDown'">poi 엑셀 다운로드</button>
			<button class="btn btn-warning" onclick="location.href='/MaxPoiDown'">poi 대용량 엑셀 다운로드</button>
			<button class="btn btn-warning" id="btnSubmit" >submit</button>
		</div>
	</div>
	<div id="div_ajax_load_image" style="display: none;"><img src="/img/loading.gif"></div>
</body>
</html>