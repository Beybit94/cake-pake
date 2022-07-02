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
    <title>Cake - Home</title>
    <jsp:include page="css.jsp"/>
    <style>
        #from-handle, #to-handle {
            width: 70px;
            height: 35px;
            top: 50%;
            margin-top: -15px;
            text-align: center;
            line-height: 30px;
            outline: none;
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value=""/>
    </jsp:include>
    <div class="row">
        <div class="col-12">
            <div class="pricing-header mx-auto">
                <form method="POST" action="${ActionNames.ProductList.name}">
                    <div class="row">
                        <div class="col-4">
                            <label>
                                <fmt:message key="label.city"/>
                            </label>
                            <select name="city" class="form-control local" style="width: 100%">
                                <c:forEach items="${cities}" var="city">
                                    <option value="${city.id}">
                                            ${city.text}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-4">
                            <label>
                                <fmt:message key="label.productSize"/>
                            </label>
                            <select name="productSize" class="form-control local" style="width: 100%">
                                <c:forEach items="${productSizes}" var="productSize">
                                    <option value="${productSize.id}">
                                            ${productSize.text}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-4">
                            <label>
                                <fmt:message key="label.productCategory"/>
                            </label>
                            <select name="productCategory" class="form-control local" style="width: 100%">
                                <c:forEach items="${productCategories}" var="productCategory">
                                    <optgroup label="${productCategory.text}">
                                        <c:forEach items="${productCategory.children}" var="children">
                                            <option value="${children.id}">
                                                    ${children.text}
                                            </option>
                                        </c:forEach>
                                    </optgroup>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-6 align-self-center align-content-center ml-2 mr-5">
                            <div id="slider-range">
                                <div id="from-handle" class="ui-slider-handle"></div>
                                <div id="to-handle" class="ui-slider-handle"></div>
                            </div>
                            <input type="hidden" id="fromPrice" name="fromPrice">
                            <input type="hidden" id="toPrice" name="toPrice">
                        </div>
                        <div class="col-4">
                            <button type="submit" class="btn btn-primary"><fmt:message
                                    key="button.search"/></button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-deck">
                <c:forEach var="item" items="${products}">
                    <div class="card box-shadow">
                        <div class="card-header">
                            <h4 class="my-0 font-weight-normal">Free</h4>
                        </div>
                        <div class="card-body">
                            <h1 class="card-title pricing-card-title">$0 <small class="text-muted">/ mo</small></h1>
                            <ul class="list-unstyled mt-3 mb-4">
                                <li>10 users included</li>
                                <li>2 GB of storage</li>
                                <li>Email support</li>
                                <li>Help center access</li>
                            </ul>
                            <button type="button" class="btn btn-lg btn-block btn-outline-primary">Sign up for free
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
</body>
<script type="text/javascript">
    const fromHandle = $("#from-handle");
    const toHandel = $("#to-handle");
    $("#slider-range").slider({
        range: true,
        min: 2000,
        max: 50000,
        step: 1000,
        values: [2000, 10000],
        create: function () {
            fromHandle.text($(this).slider("values")[0]);
            toHandel.text($(this).slider("values")[1]);
        },
        slide: function (event, ui) {
            $('#fromPrice').val(ui.values[0]);
            fromHandle.text(ui.values[0]);
            $('#toPrice').val(ui.values[1]);
            toHandel.text(ui.values[1]);
        }
    });
</script>
</html>
