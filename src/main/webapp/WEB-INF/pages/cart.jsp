<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <p/>
  <p>
    Total quantity: ${cart.totalQuantity}
  </p>
  <p>
    Total cost: <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cart.currency.symbol}"/>
  </p>
  <c:if test="${not empty param.message and empty errors}">
    <div class="success">
      ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty errors}">
    <div class="error">
      There was errors during update
    </div>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <c:if test="${not empty cart.items}">
      <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>
              Description
            </td>
            <td class="quantity">
              Quantity
            </td>
            <td class="price">
              Price
            </td>
            <td>
              Delete item
            </td>
          </tr>
        </thead>
        <c:forEach var="item" items="${cart.items}" varStatus="status">
          <tr>
            <td>
              <img class="product-tile" src="${item.product.imageUrl}">
            </td>
            <td>
              <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                ${item.product.description}
              </a>
            </td>
            <td class="quantity">
              <fmt:formatNumber value="${item.quantity}" var="quantity"/>
              <c:set var="error" value="${errors[item.product.id]}"/>
              <c:set var="errorQuantity" value="${errorQuantities[item.product.id]}"/>
              <input name="quantity" class="quantity" value="${not empty error ? errorQuantity : quantity}"/>
              <input type="hidden" name="productId" value="${item.product.id}">
              <c:if test="${not empty error}">
                <div class="error">
                  ${error}
                </div>
              </c:if>
              <c:if test="${not empty errors and empty error}">
                <div class="success">
                  Item updated
                </div>
              </c:if>
            </td>
            <td class="price">
              <tags:popupWindow productId="${item.product.id}">
                <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
              </tags:popupWindow>
            </td>
            <td>
              <button form="deleteCartItem" 
                      formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                Delete
              </button>
            </td>
          </tr>
        </c:forEach>
      </table>
      <p>
        <button>
          Update
        </button>
      </p>
    </c:if>
  </form>
  <form id="deleteCartItem" method="post">
  </form>
</tags:master>