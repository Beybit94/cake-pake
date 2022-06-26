<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - <fmt:message key="page.productCategory"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.ProductcategoryList.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <div class="card">
                <div class="card-header">
                    <div class="d-flex flex-row justify-content-between">
                        <h5 class="p-2 justify-content-start"><fmt:message key="page.productCategory"/></h5>
                        <button type="submit" class="btn btn-outline-success" data-toggle="modal" data-target="#addModal"><fmt:message key="button.add"/></button>
                    </div>
                </div>

                <div class="card-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col"><fmt:message key="label.text"/></th>
                            <th scope="col"><fmt:message key="label.parent"/></th>
                            <th style="width: 10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty productCategories}">
                                <tr>
                                    <td colspan="3" class="text-center"><fmt:message key="label.noData"/></td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="item" items="${productCategories}">
                                    <tr>
                                        <th scope="row">${item.id}</th>
                                        <td>${item.text}</td>
                                        <td>${item.parentText}</td>
                                        <td class="d-flex flex-column justify-content-between align-content-start">
                                            <button type="button" class="btn btn-outline-primary mb-1" data-toggle="modal" data-target="#updateModal${item.id}"><fmt:message key="button.update"/></button>
                                            <button type="button" class="btn btn-outline-danger mb-1" data-toggle="modal" data-target="#deleteModal${item.id}"><fmt:message key="button.delete"/></button>

                                            <div class="modal fade" id="updateModal${item.id}" tabindex="-1" role="dialog" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form action="${ActionNames.ProductcategoryEdit.name}" method="post">
                                                            <input type="hidden" name="id" value="${item.id}"/>
                                                            <div class="modal-body">
                                                                <div class="form-group">
                                                                    <select name="parent" class="form-control">
                                                                        <option value=""></option>
                                                                        <c:forEach items="${productCategories}" var="parent">
                                                                            <option value="${parent.id}" <c:if test="${parent.id == item.parent}"> selected </c:if>>
                                                                                    ${parent.text}
                                                                            </option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label><b><fmt:message key="label.code"/></b></label>
                                                                    <select name="code" class="form-control">
                                                                        <c:forEach items="${locals}" var="local">
                                                                            <option value="${local.code}" <c:if test="${local.code == item.code}"> selected </c:if>>
                                                                                    ${local.message}
                                                                            </option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                <button type="submit" class="btn btn-primary"><fmt:message key="button.update"/></button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal fade" id="deleteModal${item.id}" tabindex="-1" role="dialog" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form action="${ActionNames.ProductcategoryRemove.name}" method="post">
                                                            <input type="hidden" name="id" value="${item.id}"/>
                                                            <div class="modal-header">
                                                                <h5 class="modal-title"><fmt:message key="label.confirmDelete"/></h5>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                <button type="submit" class="btn btn-danger"><fmt:message key="button.delete"/></button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

                <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <form action="${ActionNames.ProductcategoryAdd.name}" method="post">
                                <div class="modal-body">
                                    <select name="parent" class="form-control">
                                        <option value=""></option>
                                        <c:forEach items="${productCategories}" var="parent">
                                            <option value="${parent.id}">
                                                    ${parent.text}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.code"/></b></label>
                                        <select name="code" class="form-control" required>
                                            <c:forEach items="${locals}" var="local">
                                                <option value="${local.code}">
                                                        ${local.message}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary"><fmt:message key="button.add"/></button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>