<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<%@ attribute name="oldValues" required="true" type="java.util.Map" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr>
	<td>${label}<span style="color:red">*</span></td>
	<td>
		<c:set var="error" value="${errors[name]}"/>
		<c:set var="oldValue" value="${oldValues[name]}"/>
		<input name="${name}" value="${oldValue}">
		<c:if test="${not empty error}">
			<div class="error">
        ${error}
      </div>
		</c:if>
	</td>
</tr>