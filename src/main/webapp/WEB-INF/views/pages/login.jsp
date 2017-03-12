<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>Sign In</p>
<form action="<c:url value='/login'/>" method="post">
    <input type="text" class="form-control" name="username" placeholder="Username"/>
    <input type="password" class="form-control" name="password" placeholder="Password"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button type="submit" class="btn btn-primary">Sign In</button>
</form>