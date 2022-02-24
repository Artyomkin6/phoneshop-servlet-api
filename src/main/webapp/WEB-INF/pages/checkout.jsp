<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
  <p/>
  <p>
    Total quantity: ${order.totalQuantity}
  </p>
  <c:if test="${not empty param.message and empty checkoutErrors}">
    <div class="success">
      ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty checkoutErrors}">
    <div class="error">
      There was errors during placing order
    </div>
  </c:if>
  <c:if test="${not empty order.items}">
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
        </tr>
      </thead>
      <c:forEach var="item" items="${order.items}" varStatus="status">
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
            ${item.quantity}
          </td>
          <td class="price">
            <tags:popupWindow productId="${item.product.id}">
              <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </tags:popupWindow>
          </td>
        </tr>
      </c:forEach>
      <tr>
        <td></td>
        <td></td>
        <td class="quantity">
          Subtotal cost
        </td>
        <td class="price">
          <fmt:formatNumber value="${order.subtotalCost}" type="currency" currencySymbol="${order.currency.symbol}"/>
        </td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td class="quantity">
          Delivery cost
        </td>
        <td class="price">
          <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${order.currency.symbol}"/>
        </td>
      </tr>
      <tr>
        <td></td>
        <td></td>
        <td class="quantity">
          Total cost
        </td>
        <td class="price">
          <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${order.currency.symbol}"/>
        </td>
      </tr>
    </table>
  </c:if>

  <c:if test="${not empty order.items}">
    <h2>Your details</h2>
    <form method="post">
      <table>
        <tags:orderFormRow name="firstName" label="First name" errors="${checkoutErrors}" oldValues="${oldValues}"/>
        <tags:orderFormRow name="lastName" label="Last name" errors="${checkoutErrors}" oldValues="${oldValues}"/>
        <tags:orderFormRow name="phone" label="Phone" errors="${checkoutErrors}" oldValues="${oldValues}"/>
        <tags:orderFormRow name="deliveryDate" label="Delivery date" errors="${checkoutErrors}" oldValues="${oldValues}"/>
        <tags:orderFormRow name="address" label="Address" errors="${checkoutErrors}" oldValues="${oldValues}"/>
        <tr>
          <td>Payment method<span style="color:red">*</span></td>
          <td>
            <p>
              <input type="radio" name="paymentMethod" value="cash" checked="true"/> Cash
            </p>
            <p>
              <input type="radio" name="paymentMethod" value="creditCard"/> Credit card
            </p>
          </td>
        </tr>
      </table>
      <p/>
      <button>
        Place order
      </button>
    </form>
  </c:if>
</tags:master>