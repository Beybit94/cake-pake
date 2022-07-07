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
    <title>Cake - <fmt:message key="page.users"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.UserList.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <div class="card">
                <div class="card-header">
                    <div class="d-flex flex-row justify-content-between">
                        <h5 class="p-2 justify-content-start"><fmt:message key="page.users"/></h5>
                        <button type="submit" class="btn btn-outline-success" data-toggle="modal" data-target="#addModal"><fmt:message key="button.add"/></button>
                    </div>
                </div>

                <div class="card-body">
                    <table class="table table-striped table-bordered" id="dataTable">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col"><fmt:message key="label.login"/></th>
                            <th scope="col"><fmt:message key="label.sex"/></th>
                            <th scope="col"><fmt:message key="label.address"/></th>
                            <th scope="col"><fmt:message key="label.role"/></th>
                            <th scope="col"><fmt:message key="label.status"/></th>
                            <th style="width: 10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <th scope="row">${user.user.id}</th>
                                <td>${user.user.username}</td>
                                <td>${user.user.sex}</td>
                                <td>${user.user.address}</td>
                                <td>
                                    <c:forEach var="role" items="${user.roles}">
                                        <span class="badge badge-primary">${role.text}</span>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.user.active}">
                                            <span><fmt:message key="label.active"/></span>
                                        </c:when>
                                        <c:otherwise>
                                            <span><fmt:message key="label.blocked"/></span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="d-flex flex-column justify-content-between align-content-start">
                                    <c:choose>
                                        <c:when test="${user.user.active}">
                                            <button type="button" class="btn btn-outline-dark mb-1" data-toggle="modal" data-target="#resetModal${user.user.id}"><fmt:message key="button.resetPassword"/></button>
                                            <button type="button" class="btn btn-outline-primary mb-1" data-toggle="modal" data-target="#updateModal${user.user.id}"><fmt:message key="button.update"/></button>
                                            <button type="button" class="btn btn-outline-danger mb-1" data-toggle="modal" data-target="#deleteModal${user.user.id}"><fmt:message key="button.block"/></button>

                                            <div class="modal fade" id="updateModal${user.user.id}" tabindex="-1" role="dialog" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form action="${ActionNames.UserEdit.name}" method="post">
                                                            <input type="hidden" name="${RequestParameters.id.name}" value="${user.user.id}"/>
                                                            <input type="hidden" name="${RequestParameters.username.name}" value="${user.user.username}"/>
                                                            <div class="modal-body">
                                                                <div class="form-group">
                                                                    <label for="sex"><b><fmt:message key="label.sex"/></b></label>
                                                                    <input type="text" class="form-control" name="${RequestParameters.sex.name}" id="sex" value="${user.user.sex}">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="address"><b><fmt:message key="label.address"/></b></label>
                                                                    <input type="text" class="form-control" name="${RequestParameters.address.name}" id="address" value="${user.user.address}">
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
                                            <div class="modal fade" id="deleteModal${user.user.id}" tabindex="-1" role="dialog" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form action="${ActionNames.UserRemove.name}" method="post">
                                                            <input type="hidden" name="${RequestParameters.id.name}" value="${user.user.id}"/>
                                                            <input type="hidden" name="${RequestParameters.username.name}" value="${user.user.username}"/>
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
                                            <div class="modal fade" id="resetModal${user.user.id}" tabindex="-1" role="dialog" aria-hidden="true">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form action="${ActionNames.UserReset.name}" method="post">
                                                            <input type="hidden" name="${RequestParameters.id.name}" value="${user.user.id}"/>
                                                            <input type="hidden" name="${RequestParameters.username.name}" value="${user.user.username}"/>
                                                            <div class="modal-body">
                                                                <div class="form-group">
                                                                    <label for="password"><b><fmt:message key="label.password"/></b></label>
                                                                    <input type="password" class="form-control" name="${RequestParameters.password.name}" id="password" required>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                <button type="submit" class="btn btn-danger"><fmt:message key="button.resetPassword"/></button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <form class="form-inline" action="${ActionNames.UserUnblock.name}" method="post">
                                                <input type="hidden" name="${RequestParameters.id.name}" value="${user.user.id}"/>
                                                <input type="hidden" name="${RequestParameters.username.name}" value="${user.user.username}"/>
                                                <button type="submit" class="btn btn-outline-warning"><fmt:message key="button.unblock"/></button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <form action="${ActionNames.UserAdd.name}" method="post">
                                <input type="hidden" name="${RequestParameters.id.name}" value=""/>
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.login"/></b></label>
                                        <input type="text" class="form-control" name="${RequestParameters.username.name}" required>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.password"/></b></label>
                                        <input type="password" class="form-control" name="${RequestParameters.password.name}" required>
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.sex"/></b></label>
                                        <input type="text" class="form-control" name="${RequestParameters.sex.name}">
                                    </div>
                                    <div class="form-group">
                                        <label><b><fmt:message key="label.address"/></b></label>
                                        <input type="text" class="form-control" name="${RequestParameters.address.name}">
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
    });
</script>
</html>