<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sortField" required="true" %>
<%@ attribute name="sortOrder" required="true" %>

<a href="?query=${param.query}&sortField=${sortField}&sortOrder=${sortOrder}">
  ${
    sortField eq param.sortField and sortOrder eq param.sortOrder ?
        (sortOrder eq "ascending" ?
            "&#8659;" :
            "&#8657;") :
        (sortOrder eq "ascending" ?
            "&#8595;" :
            "&#8593;")
  }
</a>