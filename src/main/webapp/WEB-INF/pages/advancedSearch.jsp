<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="errors" type="java.util.Map" scope="request"/>
<tags:master pageTitle="Advanced search">
  <h1>Advanced search page</h1>
  <form>
    <p>
      Product code
      <input name="productCode" value="${param.productCode}">
    </p>
    <p>
      Min price
      <input name="minPrice" value="${param.minPrice}">
      <c:if test="${not empty errors['minPrice']}">
        <div class="error">
          ${errors["minPrice"]}
        </div>
      </c:if>
    </p>
    <p>
      Max price
      <input name="maxPrice" value="${param.maxPrice}">
      <c:if test="${not empty errors['maxPrice']}">
        <div class="error">
          ${errors["maxPrice"]}
        </div>
      </c:if>
    </p>
    <p>
      Min stock
      <input name="minStock" value="${param.minStock}">
      <c:if test="${not empty errors['minStock']}">
        <div class="error">
          ${errors["minStock"]}
        </div>
      </c:if>
    </p>
    <div>
      <button>Search</button>
    </div>
  </form>
  <c:if test="${not empty products}">
    <div class="success">
      Found products:
    </div>
    <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>
            Description
          </td>
          <td class="price">
            Price
          </td>
        </tr>
      </thead>
      <c:forEach var="product" items="${products}">
        <tr>
          <td>
            <img class="product-tile" src="${product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
              ${product.description}
            </a>
          </td>
          <td class="price">
            <tags:popupWindow productId="${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </tags:popupWindow>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</tags:master>