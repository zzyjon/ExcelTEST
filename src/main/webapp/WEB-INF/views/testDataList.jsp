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
<script src="//code.jquery.com/jquery.min.js"></script>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script src="http://malsup.github.io/jquery.form.js"></script>
<script type="text/javascript" src="/js/jquery.paging.js"></script>
<script type="text/javascript" src="/js/jquery.bootpag.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('#page-selection').bootpag({
        total: ${totalPage},         <!-- total pages -->
        page: ${currPage},          <!-- current page -->
        maxVisible: 5,     <!-- Links per page -->
        leaps: true,
        firstLastUse: true,
        first: '←',
        last: '→',
        wrapClass: 'pagination',
        activeClass: 'active',
        disabledClass: 'disabled',
        nextClass: 'next',
        prevClass: 'prev',
        lastClass: 'last',
        firstClass: 'first'
    }).on("page", function(event, num){
        //$("#content").html("Page " + num); // or some ajax content loading...
        location.href="/?currPage="+num;

    });
});

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
	<div><h2>the total number of <span style="color:red; font-size:15;" >${dataCount}</span></h2></div>
	
	<div class="container">
		<table class="table table-hover">
			<tr>
				<th>data id</th>
				<th>name</th>
				<th>eamil</th>
				<th>reg date</th>
			</tr>
			<c:forEach var="rows" items="${dataList}">
				<tr>
					<td>${rows.mytestId}</td>
					<td>${rows.name}</td>
					<td>${rows.email}</td>
					<td>${rows.regDate}</td>
		        </tr>	
			</c:forEach>
		</table>
		<div id="page-selection"></div>
		<form id="excelUpForm" method="post" action="" role="form" enctype="multipart/form-data">
			<input id="excel" name="excel" class="file" type="file" multiple data-show-upload="false" data-show-caption="true">
			<button class="btn btn-secondary btn-lg btn-block" type="button" id="excelUp" onclick="check()">엑셀 등록</button>
		</form>
		<div>
			<button class="btn btn-primary" onclick="location.href='/jxlsDownload'">jxls 엑셀 다운로드</button>
			<button class="btn btn-warning" onclick="location.href='/poiDown'">poi 엑셀 다운로드</button>
			<button class="btn btn-warning" onclick="location.href='/MaxPoiDown'">poi 대용량 엑셀 다운로드</button>
		</div>
	</div>
	<div id="div_ajax_load_image" style="display: none;"><img src="/img/loading.gif"></div>
</body>
</html>