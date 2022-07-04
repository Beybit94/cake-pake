<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<fmt:message key="label.comment" var="Comment" />

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - ${item.name}</title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.ProductDetail.name}"/>
    </jsp:include>
    <div class="row">
        <div class="col-12 mx-auto">
           <div class="modal-body">
               <div class="row">
                   <div class="col-6">
                       <div class="slider">
                           <img class="img-thumbnail" src="${contextPath}/static/img/no_image.png"/>
                           <c:choose>
                               <c:when test="${item.photos[0] eq null}">
                                   <img class="img-thumbnail" src="${contextPath}/static/img/no_image.png"/>
                               </c:when>
                               <c:otherwise>
                                   <img class="img-thumbnail" src="${contextPath}/files${item.photos[0].path}"/>
                               </c:otherwise>
                           </c:choose>
                           <c:if test="${item.photos[1] ne null}">
                               <img class="img-thumbnail" src="${contextPath}/files${item.photos[1].path}"/>
                           </c:if>
                           <c:if test="${item.photos[2] ne null}">
                               <img class="img-thumbnail" src="${contextPath}/files${item.photos[2].path}"/>
                           </c:if>
                       </div>
                   </div>
                   <div class="col-6">
                       <ul class="list-group list-group-flush">
                           <c:if test="${sessionScope.user != null && sessionScope.user.roles.contains('user')}">
                               <li class="list-group-item">
                                   <button type="button" class="btn btn-lg btn-block btn-dark" onclick="addToCart(${item.id})"><fmt:message
                                           key="button.addToCart"/>
                                   </button>
                               </li>
                           </c:if>
                           <li class="list-group-item"><fmt:message key="label.productPrice"/>
                               : ${item.priceText}</li>
                           <li class="list-group-item"><fmt:message key="label.city"/> : ${item.city.text}</li>
                           <li class="list-group-item"><fmt:message key="label.productSize"/>
                               : ${item.productSize.text}</li>
                           <li class="list-group-item"><fmt:message key="label.productCategory"/>
                               : ${item.productCategory.parentText} - ${item.productCategory.text}</li>
                       </ul>
                       <div class="row">
                           <div class="col-12 mt-2">
                               <dd class="text-dark">
                                   ${item.description}
                               </dd>
                           </div>
                       </div>
                   </div>
               </div>
               <div class="row border-top pt-3 mt-3">
                   <div class="col-4">
                       <form method="post" action="${ActionNames.CommentAdd.name}">
                           <c:choose>
                               <c:when test="${sessionScope.user != null && sessionScope.user.roles.contains('user')}">
                                   <fieldset>
                                       <input type="hidden" name="id" value="${item.id}"/>
                                       <div class="form-group">
                                           <textarea name="comment" class="form-control" rows="2" placeholder="${Comment}"></textarea>
                                       </div>
                                       <button type="submit" class="btn btn-primary"><fmt:message key="button.send"/></button>
                                   </fieldset>
                               </c:when>
                               <c:otherwise>
                                   <fieldset disabled>
                                       <input type="hidden" name="id" value="${item.id}"/>
                                       <div class="form-group">
                                           <textarea name="comment" class="form-control" rows="2" placeholder="${Comment}"></textarea>
                                       </div>
                                       <button type="submit" class="btn btn-primary"><fmt:message key="button.send"/></button>
                                   </fieldset>
                               </c:otherwise>
                           </c:choose>
                       </form>
                   </div>
                   <div class="col-8">
                       <c:forEach var="comment" items="${item.comments}">
                           <div class="card  mb-2">
                               <div class="card-body flex-column align-items-star">
                                   <div class="d-flex w-100 justify-content-between border-bottom">
                                       <h5 class="mb-1">${comment.user.username}</h5>
                                       <small>${comment.commentDate}</small>
                                   </div>
                                   <p class="mb-1">${comment.comment}</p>
                               </div>
                           </div>
                       </c:forEach>
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
        $(".slider").slick({
            arrows: false,
            infinite: true,
            autoplay: true,
            speed: 100,
        });
    });
</script>
</html>