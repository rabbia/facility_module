<%@ include file="/WEB-INF/view/module/facilitydata/include/include.jsp"%>
<%@ include file="/WEB-INF/view/module/facilitydata/include/localHeader.jsp"%>

<openmrs:require privilege="Manage Facility Data Reports" otherwise="/login.htm" redirect="/module/facilitydata/question.list"/>

<script type="text/javascript">
    $j(document).ready(function() {
        $j('#questionList').dataTable({
            "bPaginate": true,
            "bLengthChange": false,
            "bFilter": false,
            "bSort": false,
            "bInfo": false,
            "bAutoWidth": false,
            "bSortable": true
        });
    });
    
    function deleteQuestion(id) {
    	if (confirm('<spring:message code="facilitydata.question.delete-warning"/>')) {
    		document.location.href='deleteQuestion.form?question='+id;
    	}
    }
</script>

<div class="facilityDataHeader">
	<a href="${pageContext.request.contextPath}/module/facilitydata/dashboard.list"><spring:message code="facilitydata.dashboard"/></a>
	-&gt;
	<spring:message code="facilitydata.manage-question"/>
	<hr/>
</div>

<b class="boxHeader"><spring:message code="facilitydata.manage-questions"/></b>
<div class="box">
	<a href="questionForm.form"><spring:message code="facilitydata.add-question"/></a>
	<br/><br/>
	<table cellpadding="2" cellspacing="1" id="questionList" class="facilityDataTable">
	    <thead>
		    <tr>
		        <th style="white-space:nowrap;"><spring:message code="facilitydata.display-name"/></th>
		        <th style="white-space:nowrap;"><spring:message code="facilitydata.question-type"/></th>
		        <th style="white-space:nowrap;"><spring:message code="facilitydata.period-applicability"/></th>
		        <th style="width:100%;"><spring:message code="general.description"/></th>
		        <th></th>
		    </tr>
	    </thead>
	    <tbody>
		    <c:forEach items="${questions}" var="question">
		        <tr>
	                <td style="white-space:nowrap;"><a href="questionForm.form?id=${question.id}">${question.name}</a></td>
	                <td style="white-space:nowrap;">${question.questionType}</td>
	                <td style="white-space:nowrap;">${question.periodApplicability}</td>
	                <td>${question.description}</td>
	                <td style="white-space:nowrap;">
	                	<c:if test="${questionBreakdown[question.id] == null || questionBreakdown[question.id] == 0}">
		        			<img class="actionImage" src='<c:url value="/images/trash.gif"/>' border="0" onclick="deleteQuestion('${question.id}');"/>
		        		</c:if>
		        	</td>
		        </th>
		        </tr>
		    </c:forEach>
	    </tbody>
	</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>