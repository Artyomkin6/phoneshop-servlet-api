<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
    Cart: ${cart}
  </p>
  <p>
    ${product.description}
  </p>
  <c:if test="${not empty error}">
    <div class="error">
      ${error}
    </div>
  </c:if>
  <c:if test="${not empty param.message and empty error}">
    <div class="success">
      ${param.message}
    </div>
  </c:if>
  <form method="post">
    <table>
        <tr>
          <td>Image</td>
          <td>
            <img src="${product.imageUrl}">
          </td>
        </tr>
        <tr>
          <td>Code</td>
          <td>
            ${product.code}
          </td>
        </tr>
        <tr>
          <td>Stock</td>
          <td>
            ${product.stock}
          </td>
        </tr>
        <tr>
          <td>Price</td>
          <td class="price">
            <tags:popupWindow productId="${product.id}">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </tags:popupWindow>
          </td>
        </tr>
        <tr>
          <td>Quantity</td>
          <td>
            <input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity"/>
            <c:if test="${not empty error}">
              <div class="error">
                ${error}
              </div>
            </c:if>
            <c:if test="${not empty param.message and empty error}">
              <div class="success">
                ${param.message}
              </div>
            </c:if>
          </td>
        </tr>
    </table>
    <p>
      <button>
        Add to cart
      </button>
    </p>
  </form>
</tags:master>