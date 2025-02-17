<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="histories" type="java.util.List" scope="request"/>
<tags:master pageTitle="Price History">
  <h1>
    Price History
  </h1>
  <h2>
    ${product.description}
  </h2>
  <table>
    <thead>
      <tr>
        <td>Start date</td>
        <td>Price</td>
      </tr>
    </thead>
    <c:forEach var="history" items="${histories}">
      <tr>
        <td>
          ${history.getStringDate()}
        </td>
        <td class="price">
          <fmt:formatNumber value="${history.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>