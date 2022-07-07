<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>
<%@ page import="kz.cake.web.helpers.constants.RequestParameters" %>

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
    <title>Cake - <fmt:message key="page.myProducts"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.ProductMy.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <c:if test="${requestScope.errors != null}">
                <c:forEach var="error" items="${errors}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong><fmt:message key="${error.text}"/></strong>
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                </c:forEach>
            </c:if>
            <div class="card">
                <div class="card-header">
                    <div class="d-flex flex-row justify-content-between">
                        <h5 class="p-2 justify-content-start"><fmt:message key="page.myProducts"/></h5>
                        <button type="submit" class="btn btn-outline-success" data-toggle="modal"
                                data-target="#addModal"><fmt:message key="button.add"/></button>
                    </div>
                </div>

                <div class="card-body">
                    <table class="table table-striped table-bordered" id="dataTable">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col"><fmt:message key="label.productName"/></th>
                            <th scope="col"><fmt:message key="label.productPrice"/></th>
                            <th scope="col"><fmt:message key="label.city"/></th>
                            <th scope="col"><fmt:message key="label.productSize"/></th>
                            <th scope="col"><fmt:message key="label.productCategory"/></th>
                            <th style="width: 10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${products}">
                            <tr>
                                <th scope="row">${item.id}</th>
                                <td>${item.name}</td>
                                <td>${item.priceText}</td>
                                <td>${item.city.text}</td>
                                <td>${item.productSize.text}</td>
                                <td>${item.productCategory.text}</td>
                                <td class="d-flex flex-column justify-content-between align-content-start">
                                    <form class="form-inline" action="${ActionNames.ProductRead.name}" method="post">
                                        <input type="hidden" name="${RequestParameters.id.name}" value="${item.id}"/>
                                        <button type="submit" class="btn btn-outline-primary mb-1"><fmt:message key="button.update"/></button>
                                    </form>
                                    <button type="button" class="btn btn-outline-danger mb-1" data-toggle="modal"
                                            data-target="#deleteModal${item.id}"><fmt:message
                                            key="button.delete"/></button>

                                    <div class="modal fade" id="updateModal${item.id}" tabindex="-1"
                                         role="dialog" aria-hidden="true">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <form action="${ActionNames.ProductEdit.name}" method="post">
                                                    <input type="hidden" name="${RequestParameters.id.name}" value="${item.id}"/>
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="card col-4">
                                                                <div class="card-body">
                                                                    <c:choose>
                                                                        <c:when test="${item.photos[0] eq null}">
                                                                            <label for="file4">
                                                                                <img class="card-img-top img4"
                                                                                     src="${contextPath}/static/img/no_image.png"/>
                                                                            </label>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <label for="file4">
                                                                                <img class="card-img-top img4"
                                                                                     src="${contextPath}/files${item.photos[0].path}"/>
                                                                            </label>
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                    <input type="file" class="d-none" name="file"
                                                                           id="file4" onchange="preview(this,'4')"/>
                                                                </div>
                                                            </div>
                                                            <div class="card col-4">
                                                                <div class="card-body">
                                                                    <c:choose>
                                                                        <c:when test="${item.photos[1] eq null}">
                                                                            <label for="file5">
                                                                                <img class="card-img-top img5"
                                                                                     src="${contextPath}/static/img/no_image.png"/>
                                                                            </label>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <label for="file5">
                                                                                <img class="card-img-top img5"
                                                                                     src="${contextPath}/files${item.photos[1].path}"/>
                                                                            </label>
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                    <input type="file" class="d-none" name="file"
                                                                           id="file5" onchange="preview(this,'5')"/>
                                                                </div>
                                                            </div>
                                                            <div class="card col-4">
                                                                <div class="card-body">
                                                                    <c:choose>
                                                                        <c:when test="${item.photos[2] eq null}">
                                                                            <label for="file6">
                                                                                <img class="card-img-top img6"
                                                                                     src="${contextPath}/static/img/no_image.png"/>
                                                                            </label>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <label for="file6">
                                                                                <img class="card-img-top img6"
                                                                                     src="${contextPath}/files${item.photos[2].path}"/>
                                                                            </label>
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                    <input type="file" class="d-none" name="file"
                                                                           id="file6" onchange="preview(this,'6')"/>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label><b><fmt:message key="label.productName"/></b></label>
                                                            <input type="text" class="form-control" name="${RequestParameters.productName.name}"
                                                                   value="${item.name}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label><b><fmt:message
                                                                    key="label.productPrice"/></b></label>
                                                            <input type="number" class="form-control" name="${RequestParameters.price.name}"
                                                                   value="${item.price}" required>
                                                        </div>
                                                        <div class="form-group">
                                                            <label><b><fmt:message key="label.productDescription"/></b></label>
                                                            <textarea name="${RequestParameters.description.name}" class="form-control"
                                                                      rows="3">${item.description}</textarea>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>
                                                                <fmt:message key="label.city"/>
                                                            </label>
                                                            <select name="${RequestParameters.city.name}" class="form-control local"
                                                                    style="width: 100%">
                                                                <c:forEach items="${cities}" var="city">
                                                                    <option value="${city.id}" <c:if
                                                                            test="${city.id == item.city.id}"> selected </c:if>>
                                                                            ${city.text}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>
                                                                <fmt:message key="label.productSize"/>
                                                            </label>
                                                            <select name="${RequestParameters.productSize.name}" class="form-control local"
                                                                    style="width: 100%">
                                                                <c:forEach items="${productSizes}" var="productSize">
                                                                    <option value="${productSize.id}" <c:if
                                                                            test="${productSize.id == item.productSize.id}"> selected </c:if>>
                                                                            ${productSize.text}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="form-group">
                                                            <label>
                                                                <fmt:message key="label.productCategory"/>
                                                            </label>
                                                            <select name="${RequestParameters.productCategory.name}" class="form-control local"
                                                                    style="width: 100%">
                                                                <c:forEach items="${productCategories}"
                                                                           var="productCategory">
                                                                    <optgroup label="${productCategory.text}">
                                                                        <c:forEach items="${productCategory.children}"
                                                                                   var="children">
                                                                            <option value="${children.id}" <c:if
                                                                                    test="${children.id == item.productCategory.id}"> selected </c:if>>
                                                                                    ${children.text}
                                                                            </option>
                                                                        </c:forEach>
                                                                    </optgroup>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                                data-dismiss="modal">Close
                                                        </button>
                                                        <button type="submit" class="btn btn-primary"><fmt:message
                                                                key="button.update"/></button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal fade" id="deleteModal${item.id}" tabindex="-1"
                                         role="dialog" aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <form action="${ActionNames.ProductRemove.name}" method="post">
                                                    <input type="hidden" name="${RequestParameters.id.name}" value="${item.id}"/>
                                                    <div class="modal-header">
                                                        <h5 class="modal-title"><fmt:message
                                                                key="label.confirmDelete"/></h5>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                                data-dismiss="modal">Close
                                                        </button>
                                                        <button type="submit" class="btn btn-danger"><fmt:message
                                                                key="button.delete"/></button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <form action="${ActionNames.ProductAdd.name}" method="post" enctype="multipart/form-data">
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="card col-4">
                                            <div class="card-body">
                                                <label for="file1">
                                                    <img class="card-img-top img1"
                                                         src="${contextPath}/static/img/no_image.png"/>
                                                </label>
                                                <input type="file" class="d-none" name="file" id="file1"
                                                       onchange="preview(this,'1')"/>
                                            </div>
                                        </div>
                                        <div class="card col-4">
                                            <div class="card-body">
                                                <label for="file2">
                                                    <img class="card-img-top img2"
                                                         src="${contextPath}/static/img/no_image.png"/>
                                                </label>
                                                <input type="file" class="d-none" name="file" id="file2"
                                                       onchange="preview(this,'2')"/>
                                            </div>
                                        </div>
                                        <div class="card col-4">
                                            <div class="card-body">
                                                <label for="file3">
                                                    <img class="card-img-top img3"
                                                         src="${contextPath}/static/img/no_image.png"/>
                                                </label>
                                                <input type="file" class="d-none" name="file" id="file3"
                                                       onchange="preview(this,'3')"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.productName"/></b></label>
                                        <input type="text" class="form-control" name="${RequestParameters.productName.name}" required>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.productPrice"/></b></label>
                                        <input type="number" class="form-control" name="${RequestParameters.price.name}" required>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.productDescription"/></b></label>
                                        <textarea name="${RequestParameters.description.name}" class="form-control" rows="3"></textarea>
                                    </div>
                                    <div class="form-group">
                                        <label>
                                            <fmt:message key="label.city"/>
                                        </label>
                                        <select name="${RequestParameters.city.name}" class="form-control local" style="width: 100%">
                                            <c:forEach items="${cities}" var="city">
                                                <option value="${city.id}">
                                                        ${city.text}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>
                                            <fmt:message key="label.productSize"/>
                                        </label>
                                        <select name="${RequestParameters.productSize.name}" class="form-control local" style="width: 100%">
                                            <c:forEach items="${productSizes}" var="productSize">
                                                <option value="${productSize.id}">
                                                        ${productSize.text}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>
                                            <fmt:message key="label.productCategory"/>
                                        </label>
                                        <select name="${RequestParameters.productCategory.name}" class="form-control local" style="width: 100%">
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
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary"><fmt:message
                                            key="button.add"/></button>
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
<script type="text/javascript">
    function preview(input, n) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                $(".img" + n).attr('src', e.target.result);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }

    $(document).ready(function () {
        let address = '${contextPath}/static/datatable/json/ru.json';
        const lang = '${sessionScope.language}'
        if (lang === 'en') {
            address = '';
        }

        $('#dataTable').DataTable({
            columnDefs: [
                {
                    "targets": 6,
                    "orderable": false
                }
            ],
            "order": [
                [0, "asc"]
            ],
            language: {
                url: address
            },
            searching: true,
        });

        $('.local').select2();
    });
</script>
</html>