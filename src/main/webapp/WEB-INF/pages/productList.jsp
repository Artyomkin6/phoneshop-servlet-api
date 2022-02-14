<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentProducts" type="java.util.Queue" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}" placeholder="Search product...">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sortField="description" sortOrder="ascending"/>
          <tags:sortLink sortField="description" sortOrder="descending"/>
        </td>
        <td class="quantity">
          Quantity
        </td>
        <td class="price">
          Price
          <tags:sortLink sortField="price" sortOrder="ascending"/>
          <tags:sortLink sortField="price" sortOrder="descending"/>
        </td>
        <td>
          Add to cart
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
        <form method="post">
          <td class="quantity">
            <input name="quantity" value="${not empty param.error and product.id eq param.errorProductId ? param.errorQuantity : 1}" class="quantity"/>
            <input type="hidden" name="productId" value="${product.id}">
            <c:if test="${not empty param.error and product.id eq param.errorProductId}">
              <div class="error">
                ${param.error}
              </div>
            </c:if>
            <c:if test="${not empty param.message and product.id eq param.addedProductId}">
              <div class="success">
                ${param.message}
              </div>
            </c:if>
          </td>
          <td class="price">
            <tags:popupWindow productId="${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </tags:popupWindow>
          </td>
          <td>
            <button>
              Add to cart
            </button>
          </td>
        </form>
      </tr>
    </c:forEach>
  </table>
  <c:if test="${not empty recentProducts}">
    <h2>
      Recently viewed
    </h2>
    <table>
      <tr>
        <c:forEach var="recentProduct" items="${recentProducts}">
          <td>
            <p>
              <img class="product-tile" src="${recentProduct.imageUrl}">
            </p>
            <p>
              <a href="${pageContext.servletContext.contextPath}/products/${recentProduct.id}">
                ${recentProduct.description}
              </a>
            </p>
            <p class="price">
              <fmt:formatNumber value="${recentProduct.price}" type="currency" currencySymbol="${recentProduct.currency.symbol}"/>
            </p>
          </td>
        </c:forEach>
      </tr>
    </table>
  </c:if>
</tags:master>