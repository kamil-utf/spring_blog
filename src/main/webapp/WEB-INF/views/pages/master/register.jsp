<%@ page contentType="text/html; UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign Up</title>

    <link rel="stylesheet" href="<c:url value='/static/css/bootstrap.min.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <p>Sign Up</p>
                <form:form action="/register" method="post" modelAttribute="user">
                    <form:input type="text" class="form-control" path="username" placeholder="Username"/>
                    <form:input type="text" class="form-control" path="email" placeholder="Email"/>
                    <form:input type="password" class="form-control" path="password" placeholder="Password"/>
                    <form:input type="password" class="form-control" path="confirmPassword" placeholder="Confirm Password"/>

                    <button type="submit" class="btn btn-primary">Sign Up</button>
                </form:form>
            </div>
        </div>
    </div>

    <script src="<c:url value='/static/js/jquery.min.js'/>"></script>
    <script src="<c:url value='/static/js/bootstrap.min.js'/>"></script>
</body>
</html>