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
    <title>Cake - ${item.name}</title>
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

            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${ActionNames.ProductMy.name}">Home</a></li>
                    <li class="breadcrumb-item active">${item.name}</li>
                </ol>
            </nav>

            <div class="card">
                <div class="card-header">
                </div>

                <div class="card-body">
                    <div class="modal-body">
                        <form action="${ActionNames.ProductEdit.name}" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="id" value="${item.id}"/>
                            <div class="row d-flex flex-row align-content-center justify-content-between">
                                <div class="card p-2">
                                    <div class="card-body" style="position: relative">
                                        <c:choose>
                                            <c:when test="${item.photos[0] eq null}">
                                                <label for="file1">
                                                    <img class="card-img-top img1"
                                                         src="${contextPath}/static/img/no_image.png"/>
                                                </label>
                                            </c:when>
                                            <c:otherwise>
                                                <label class="label1">
                                                    <img class="card-img-top img1"
                                                         src="${contextPath}/files${item.photos[0].path}" width="100%"
                                                         height="100%"/>
                                                    <img src="${contextPath}/static/img/loader.gif" class="loader1"
                                                         style="display:none;width: 100px;position:absolute;top:0;bottom: 0;left: 0;right: 0;margin: auto;text-align: center">
                                                </label>
                                                <button type="button" class="btn btn-danger remove"
                                                        style="position: absolute; top: 0;right: 0"
                                                        data-id="${item.photos[0].id}" data-index="1">X
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                        <input type="file" class="d-none" name="file"
                                               id="file1" onchange="preview(this,'1')"/>
                                    </div>
                                </div>
                                <div class="card p-2">
                                    <div class="card-body" style="position: relative">
                                        <c:choose>
                                            <c:when test="${item.photos[1] eq null}">
                                                <label for="file2">
                                                    <img class="card-img-top img2"
                                                         src="${contextPath}/static/img/no_image.png"/>
                                                </label>
                                            </c:when>
                                            <c:otherwise>
                                                <label class="label2">
                                                    <img class="card-img-top img2"
                                                         src="${contextPath}/files${item.photos[1].path}" width="100%"
                                                         height="100%"/>
                                                    <img src="${contextPath}/static/img/loader.gif" class="loader2"
                                                         style="display:none;width: 100px;position:absolute;top:0;bottom: 0;left: 0;right: 0;margin: auto;text-align: center">
                                                </label>
                                                <button type="button" class="btn btn-danger remove"
                                                        style="position: absolute; top: 0;right: 0"
                                                        data-id="${item.photos[1].id}" data-index="2">X
                                                </button>
                                            </c:otherwise>
                                        </c:choose>

                                        <input type="file" class="d-none" name="file"
                                               id="file2" onchange="preview(this,'2')"/>
                                    </div>
                                </div>
                                <div class="card p-2">
                                    <div class="card-body" style="position: relative">
                                        <c:choose>
                                            <c:when test="${item.photos[2] eq null}">
                                                <label for="file3">
                                                    <img class="card-img-top img3"
                                                         src="${contextPath}/static/img/no_image.png"/>
                                                </label>
                                            </c:when>
                                            <c:otherwise>
                                                <label class="label3">
                                                    <img class="card-img-top img3"
                                                         src="${contextPath}/files${item.photos[2].path}" width="100%"
                                                         height="100%"/>
                                                    <img src="${contextPath}/static/img/loader.gif" class="loader3"
                                                         style="display:none;width: 100px;position:absolute;top:0;bottom: 0;left: 0;right: 0;margin: auto;text-align: center">
                                                </label>
                                                <button type="button" class="btn btn-danger remove"
                                                        style="position: absolute; top: 0;right: 0"
                                                        data-id="${item.photos[2].id}" data-index="3">X
                                                </button>
                                            </c:otherwise>
                                        </c:choose>

                                        <input type="file" class="d-none" name="file"
                                               id="file3" onchange="preview(this,'3')"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label><b><fmt:message key="label.productName"/></b></label>
                                <input type="text" class="form-control" name="name"
                                       value="${item.name}" required>
                            </div>
                            <div class="form-group">
                                <label><b><fmt:message
                                        key="label.productPrice"/></b></label>
                                <input type="number" class="form-control" name="price"
                                       value="${item.price}" required>
                            </div>
                            <div class="form-group">
                                <label><b><fmt:message key="label.productDescription"/></b></label>
                                <textarea name="description" class="form-control"
                                          rows="3">${item.description}</textarea>
                            </div>
                            <div class="form-group">
                                <label>
                                    <fmt:message key="label.city"/>
                                </label>
                                <select name="city" class="form-control local"
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
                                <select name="productSize" class="form-control local"
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
                                <select name="productCategory" class="form-control local"
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
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary"><fmt:message
                                        key="button.update"/></button>
                            </div>
                        </form>
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
        $('.remove').on('click', function () {
            const btn = $(this);
            const id = btn.data("id");
            const index = btn.data("index");
            const loader = $(".loader" + index);
            loader.show();
            $.ajax({
                url: "${contextPath}/${ActionNames.PhotoRemove.name}",
                method: "POST",
                data: {id: id},
                success: function () {
                    $(".img" + index).attr('src', "${contextPath}/static/img/no_image.png");
                    $(".label" + index).attr("for", "file" + index);
                    loader.hide();
                    btn.hide();
                }
            })
        })
        $('.local').select2();
    });
</script>
</html>