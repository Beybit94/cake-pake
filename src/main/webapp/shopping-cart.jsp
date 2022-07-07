<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>
<%@ page import="kz.cake.web.helpers.constants.RequestParameters" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:message key="label.cash" var="Cash" />

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - <fmt:message key="button.cart"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.CartList.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
            <table class="table table-striped table-bordered" id="dataTable">
                <thead>
                <tr>
                    <th><fmt:message key="label.productName"/></th>
                    <th><fmt:message key="label.productPrice"/></th>
                    <th><fmt:message key="label.quantity"/></th>
                    <th style="display: none"></th>
                    <th></th>
                </tr>
                </thead>
                <c:if test="${sessionScope.orderDraft ne null && sessionScope.orderDraft.orderDetail.size() > 0}">
                    <tfoot>
                    <tr>
                        <th colspan="2" class="text-left">
                            <button type="button" class="btn tn-block btn-secondary" data-toggle="modal" data-target="#exampleModal">
                                <fmt:message key="button.checkout"/>
                            </button>
                            <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <form method="post" action="${ActionNames.OrderAdd.name}">
                                            <input type="hidden" name="${RequestParameters.id.name}" value="${sessionScope.orderDraft.id}"/>
                                            <div class="modal-body">
                                                <div class="form-group">
                                                    <label><fmt:message key="label.paymentType"/></label>
                                                    <input type="text" class="form-control" value="${Cash}" name="${RequestParameters.payment.name}" readonly/>
                                                </div>
                                                <div class="form-group">
                                                    <label><fmt:message key="label.address"/></label>
                                                    <input type="text" class="form-control" value="${sessionScope.orderDraft.user.address}" name="${RequestParameters.address.name}"/>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="submit" class="btn btn-primary"><fmt:message key="button.send"/></button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </th>
                        <th colspan="2" class="text-right"><fmt:message key="label.total"/></th>
                        <th></th>
                    </tr>
                    </tfoot>
                </c:if>
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
        processing: true,
        serverSide: true,
        ajax: {
            url: "${contextPath}/${ActionNames.CartList.name}",
            type: "POST",
            dataSrc: 'orderDetail'
        },
        columns: [
            {data: 'product.name'},
            {data: 'product.priceText'},
            {data: 'quantity'},
            {
                "data": function (orderDetail) {
                    return orderDetail.quantity * orderDetail.product.price;
                },
                visible: false,
            },
            {
                "data": function (orderDetail) {
                    return '<button type="button" class="btn btn-secondary align-self-center align-content-center mr-1" onclick="removeFromCart(' + orderDetail.product.id + ',reload)">-</button>' +
                        '<button type="button" class="btn btn-secondary align-self-center align-content-center mr-1" onclick="addToCart(' + orderDetail.product.id + ',reload)">+</button>' +
                        '<button type="button" class="btn btn-danger align-self-center align-content-center" onclick="removeFromOrder(' + orderDetail.id + ',reload)">x</button>';
                },
                "width": "20%",
                "orderable": false
            }
        ],
        footerCallback: function (row, data, start, end, display) {
            const api = this.api();

            const intVal = function (i) {
                return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
            };

            const pageTotal = api
                .column(3, {page: 'current'})
                .data()
                .reduce(function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0);
            $(api.column(4).footer()).html(pageTotal);
        },
        "order": [
            [2, "asc"]
        ],
        language: {
            url: address
        },
        searching: false,
        paging: false,
        info: false,
    });

    const reload = function () {
        table.ajax.reload();
        table.footerCallback();
    }
</script>
</html>
