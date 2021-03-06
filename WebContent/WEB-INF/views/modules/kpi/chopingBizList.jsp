<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>切片绩效记录</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<!-- tab头部 -->
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/kpi/chopingBiz/">切片工站每日绩效</a></li>
		<li><a href="${ctx}/kpi/chopingBiz/month">切片工站月度绩效</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chopingBiz" action="${ctx}/kpi/chopingBiz/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>开始时间：</label><form:input path="startTime" htmlEscape="false" maxlength="50" class="input-medium" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
		&nbsp;<label>结束时间：</label><form:input path="endTime" htmlEscape="false" maxlength="50" class="input-medium" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
		&nbsp;<label>人员名称：</label><form:input path="employeeName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<a href="${ctx}/kpi/chopingBiz/export">
			<input id="btnExport" class="btn btn-primary" type="button" value="报表"/>
		</a>
	</form:form>
	
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>日期</th><th>人员</th><th>总数</th><th>薪资</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="chopingBiz">
			<tr>
				<td>${chopingBiz.time}</td>
				<td>${chopingBiz.employeeName}</td>
				<td>${chopingBiz.pieces}</td>
				<td>${chopingBiz.money}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>