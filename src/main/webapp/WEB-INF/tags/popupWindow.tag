<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="productId" required="true" %>

<a
  href=''
  onclick='javascript:window.open("${pageContext.servletContext.contextPath}/products/price-history/${productId}", "_blank", "scrollbars=0,resizable=0,height=500,width=450");'
  title='Pop Up'>
    <jsp:doBody/>
</a>