<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - <fmt:message key="page.orderHistory"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.OrderHistory.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <table class="table table-striped table-bordered" id="dataTable">
                <thead>
                <tr>
                    <th><fmt:message key="label.address"/></th>
                    <th><fmt:message key="label.orderDate"/></th>
                    <th><fmt:message key="label.shippingDate"/></th>
                    <th><fmt:message key="label.orderStatus"/></th>
                    <th style="width: 30%"><fmt:message key="label.productName"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${orders}">
                    <tr>
                        <td>${item.address}</td>
                        <td>${item.orderDateText}</td>
                        <td>${item.shippingDateText}</td>
                        <td>${item.orderStatus.text}</td>
                        <td>
                            <c:forEach var="product" items="${item.orderDetail}">
                                <p>${product.product.name}</p>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
<script type="text/javascript">
    let address = '${contextPath}/static/datatable/json/ru.json';
    const lang = '${sessionScope.language}'
    if (lang === 'en') {
        address = '';
    }

    const table = $('#dataTable').DataTable({
        columnDefs: [
            {
                "targets": 4,
                "orderable": false
            }
        ],
        language: {
            url: address
        },
        searching: false,
    });
</script>
</html>
