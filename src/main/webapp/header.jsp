<%@ page import="kz.cake.web.helpers.constants.PageNames" %>
<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow">
    <form class="form-inline my-0 mr-md-auto font-weight-normal" action="${ActionNames.ProductList.name}" method="post">
        <button type="submit" class="btn btn-link text-dark">Home</button>
    </form>

    <div class="d-flex flex-row justify-content-end">
        <c:choose>
            <c:when test="${sessionScope.user eq null}">
                <a class="p-2 text-dark" href="${contextPath}/${PageNames.register.name}"><fmt:message
                        key="page.register"/></a>
                <a class="p-2 text-dark" href="${contextPath}/${PageNames.login.name}"><fmt:message
                        key="page.login"/></a>
            </c:when>
            <c:otherwise>
                <c:if test="${sessionScope.user.roles.contains('admin')}">
                    <form class="form-inline" action="${ActionNames.UserList.name}" method="post"
                          style="margin-left: 5px;">
                        <button type="submit" class="btn btn-link text-dark"><fmt:message
                                key="page.users"/></button>
                    </form>
                    <div class="dropdown">
                        <button class="btn btn-link text-dark dropdown-toggle" type="button" data-toggle="dropdown">
                            <fmt:message key="label.dictionaries"/>
                        </button>
                        <div class="dropdown-menu">
                            <form class="form-inline" action="${ActionNames.LocalList.name}" method="post">
                                <button type="submit" class="btn btn-link text-dark"><fmt:message
                                        key="page.locals"/></button>
                            </form>
                            <div class="dropdown-divider"></div>
                            <form class="form-inline" action="${ActionNames.CityList.name}" method="post">
                                <button type="submit" class="btn btn-link text-dark"><fmt:message
                                        key="page.city"/></button>
                            </form>
                            <div class="dropdown-divider"></div>
                            <form class="form-inline" action="${ActionNames.ProductsizeList.name}" method="post">
                                <button type="submit" class="btn btn-link text-dark"><fmt:message
                                        key="page.productSize"/></button>
                            </form>
                            <div class="dropdown-divider"></div>
                            <form class="form-inline" action="${ActionNames.ProductcategoryList.name}" method="post">
                                <button type="submit" class="btn btn-link text-dark"><fmt:message
                                        key="page.productCategory"/></button>
                            </form>
                        </div>
                    </div>
                </c:if>
                <c:if test="${sessionScope.user.roles.contains('manager')}">
                    <form class="form-inline" action="${ActionNames.ProductMy.name}" method="post"
                          style="margin-left: 5px;">
                        <button type="submit" class="btn btn-link text-dark"><fmt:message key="page.myProducts"/></button>
                    </form>
                    <form class="form-inline" action="${ActionNames.OrderMy.name}" method="post"
                          style="margin-left: 5px;">
                        <button type="submit" class="btn btn-link text-dark"><fmt:message key="page.orders"/></button>
                    </form>
                    <form class="form-inline" action="${ActionNames.AuthProfile.name}" method="post"
                          style="margin-left: 5px;">
                        <button type="submit" class="btn btn-outline-primary">${sessionScope.user.userName}</button>
                    </form>
                </c:if>
                <c:if test="${sessionScope.user.roles.contains('user')}">
                    <form class="form-inline" action="${ActionNames.OrderHistory.name}" method="post"
                          style="margin-left: 5px;">
                        <button type="submit" class="btn btn-link text-dark"><fmt:message key="page.orderHistory"/></button>
                    </form>
                    <form class="form-inline" action="${ActionNames.AuthProfile.name}" method="post"
                          style="margin-left: 5px;">
                        <button type="submit" class="btn btn-outline-primary">${sessionScope.user.userName}</button>
                    </form>
                </c:if>
                <form class="form-inline" action="${ActionNames.AuthLogout.name}" method="post"
                      style="margin-left: 5px;">
                    <button type="submit" class="btn btn-secondary"><fmt:message key="button.logout"/></button>
                </form>
            </c:otherwise>
        </c:choose>

        <form action="${ActionNames.LanguagesChange.name}" method="post" class="form-inline" id="changeLangForm"
              style="margin-left: 5px;">
            <input type="hidden" name="redirect" value="${param.get("redirect")}"/>
            <div class="form-group">
                <select class="form-control" name="code" onchange="javascript:$('#changeLangForm').submit()">
                    <c:choose>
                        <c:when test="${sessionScope.language eq 'en'}">
                            <option value="en" selected><fmt:message key="label.en"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="en"><fmt:message key="label.en"/></option>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${sessionScope.language eq 'ru'}">
                            <option value="ru" selected><fmt:message key="label.ru"/></option>
                        </c:when>
                        <c:otherwise>
                            <option value="ru"><fmt:message key="label.ru"/></option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>
        </form>
    </div>
</div>