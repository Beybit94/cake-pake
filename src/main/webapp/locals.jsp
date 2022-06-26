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
    <title>Cake - <fmt:message key="page.locals"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.LocalList.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <div class="card">
                <div class="card-header">
                    <div class="d-flex flex-row justify-content-between">
                        <h5 class="p-2 justify-content-start"><fmt:message key="page.locals"/></h5>
                        <button type="submit" class="btn btn-outline-success" data-toggle="modal"
                                data-target="#addModal"><fmt:message key="button.add"/></button>
                    </div>
                </div>

                <div class="card-body">
                    <table class="table table-striped table-bordered" id="dataTable">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col"><fmt:message key="label.code"/></th>
                            <th scope="col"><fmt:message key="label.text"/></th>
                            <th scope="col"><fmt:message key="label.language"/></th>
                            <th style="width: 10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${locals}">
                            <tr>
                                <th scope="row">${item.id}</th>
                                <td>${item.code}</td>
                                <td>${item.text}</td>
                                <td>${item.language}</td>
                                <td class="d-flex flex-column justify-content-between align-content-start">
                                    <button type="button" class="btn btn-outline-primary mb-1" data-toggle="modal"
                                            data-target="#updateModal${item.id}"><fmt:message
                                            key="button.update"/></button>
                                    <button type="button" class="btn btn-outline-danger mb-1" data-toggle="modal"
                                            data-target="#deleteModal${item.id}"><fmt:message
                                            key="button.delete"/></button>

                                    <div class="modal fade" id="updateModal${item.id}" tabindex="-1" role="dialog"
                                         aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <form action="${ActionNames.LocalEdit.name}" method="post">
                                                    <input type="hidden" name="id" value="${item.id}"/>
                                                    <div class="modal-body">
                                                        <div class="form-group">
                                                            <label><b><fmt:message key="label.code"/></b></label>
                                                            <input type="text" class="form-control" name="code"
                                                                   value="${item.code}" required/>
                                                        </div>
                                                        <div class="form-group">
                                                            <label><b><fmt:message key="label.text"/></b></label>
                                                            <input type="text" class="form-control" name="text"
                                                                   value="${item.text}" required/>
                                                        </div>
                                                        <div class="form-group">
                                                            <label><b><fmt:message key="label.language"/></b></label>
                                                            <select name="languageId" class="form-control">
                                                                <c:forEach var="lang" items="${languages}">
                                                                    <option value="${lang.id}" <c:if
                                                                            test="${lang.code == item.language}"> selected </c:if>>
                                                                            ${lang.code}
                                                                    </option>
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
                                    <div class="modal fade" id="deleteModal${item.id}" tabindex="-1" role="dialog"
                                         aria-hidden="true">
                                        <div class="modal-dialog" role="document">
                                            <div class="modal-content">
                                                <form action="${ActionNames.LocalRemove.name}" method="post">
                                                    <input type="hidden" name="id" value="${item.id}"/>
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
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <form action="${ActionNames.LocalAdd.name}" method="post">
                                <input type="hidden" name="id" value=""/>
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.code"/></b></label>
                                        <input type="text" class="form-control" name="code" required/>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.text"/></b></label>
                                        <input type="text" class="form-control" name="text" required/>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.language"/></b></label>
                                        <select name="languageId" class="form-control">
                                            <c:forEach var="lang" items="${languages}">
                                                <option value="${lang.id}">
                                                        ${lang.code}
                                                </option>
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
    $(document).ready(function () {
        let address = '${contextPath}/static/datatable/json/ru.json';
        const lang = '${sessionScope.language}'
        if (lang === 'en') {
            address = '';
        }

        $('#dataTable').DataTable({
            columnDefs: [
                {
                    "targets": 4,
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
    });
</script>
</html>