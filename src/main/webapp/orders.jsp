<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>
<%@ page import="kz.cake.web.helpers.constants.LocaleCodes" %>

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
    <title>Cake - <fmt:message key="page.orders"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.OrderList.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <ul class="nav nav-tabs" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#new" role="tab"
                       aria-selected="true"><fmt:message key="label.new"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#completed" role="tab"
                       aria-selected="false"><fmt:message key="label.completed"/></a>
                </li>
            </ul>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade show active" id="new" role="tabpanel">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th><fmt:message key="label.productName"/></th>
                            <th><fmt:message key="label.address"/></th>
                            <th><fmt:message key="label.paymentType"/></th>
                            <th><fmt:message key="label.orderDate"/></th>
                            <th style="width: 10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${orders}">
                            <c:if test="${item.orderStatus.code == LocaleCodes.statusNew.name}">
                                <tr>
                                    <td>${item.user.username}</td>
                                    <td>${item.address}</td>
                                    <td>${item.paymentType}</td>
                                    <td>${item.orderDateText}</td>
                                    <td class="d-flex flex-column justify-content-between align-content-start">
                                        <button type="button" class="btn btn-outline-primary mb-1" data-toggle="modal"
                                                data-target="#detail${item.id}"><fmt:message
                                                key="button.detail"/></button>
                                        <form class="form-inline" method="post"
                                              action="${ActionNames.OrderComplete.name}">
                                            <input type="hidden" name="id" value="${item.id}"/>
                                            <button type="submit" class="btn btn-outline-dark mb-1"><fmt:message
                                                    key="button.complete"/></button>
                                        </form>
                                        <div class="modal fade" id="detail${item.id}" tabindex="-1" role="dialog"
                                             aria-hidden="true">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <table class="table table-striped">
                                                            <thead>
                                                            <tr>
                                                                <th><fmt:message key="label.productName"/></th>
                                                                <th><fmt:message key="label.productCategory"/></th>
                                                                <th><fmt:message key="label.productSize"/></th>
                                                                <th><fmt:message key="label.quantity"/></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:forEach var="product" items="${item.orderDetail}">
                                                               <tr>
                                                                   <td>${product.product.name}</td>
                                                                   <td>${product.product.productCategory.parentText} - ${product.product.productCategory.text}</td>
                                                                   <td>${product.product.productSize.text}</td>
                                                                   <td>${product.quantity}</td>
                                                               </tr>
                                                            </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="tab-pane fade" id="completed" role="tabpanel">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th><fmt:message key="label.productName"/></th>
                            <th><fmt:message key="label.address"/></th>
                            <th><fmt:message key="label.paymentType"/></th>
                            <th><fmt:message key="label.shippingDate"/></th>
                            <th style="width: 10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${orders}">
                            <c:if test="${item.orderStatus.code == LocaleCodes.statusCompleted.name}">
                                <tr>
                                    <td>${item.user.username}</td>
                                    <td>${item.address}</td>
                                    <td>${item.paymentType}</td>
                                    <td>${item.shippingDateText}</td>
                                    <td class="d-flex flex-column justify-content-between align-content-start">
                                        <button type="button" class="btn btn-outline-primary mb-1" data-toggle="modal"
                                                data-target="#detail${item.id}"><fmt:message
                                                key="button.detail"/></button>
                                        <div class="modal fade" id="detail${item.id}" tabindex="-1" role="dialog"
                                             aria-hidden="true">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <table class="table table-striped">
                                                            <thead>
                                                            <tr>
                                                                <th><fmt:message key="label.productName"/></th>
                                                                <th><fmt:message key="label.productCategory"/></th>
                                                                <th><fmt:message key="label.productSize"/></th>
                                                                <th><fmt:message key="label.quantity"/></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:forEach var="product" items="${item.orderDetail}">
                                                                <tr>
                                                                    <td>${product.product.name}</td>
                                                                    <td>${product.product.productCategory.parentText} - ${product.product.productCategory.text}</td>
                                                                    <td>${product.product.productSize.text}</td>
                                                                    <td>${product.quantity}</td>
                                                                </tr>
                                                            </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
