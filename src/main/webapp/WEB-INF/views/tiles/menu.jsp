<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<ul class="nav navbar-nav">
    <li><a href="#">Home</a></li>
</ul>
<ul class="nav navbar-nav navbar-right">
    <li>
        <sec:authorize access="isAuthenticated()">
            <a href="#" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
                Logout
            </a>

            <form id="logout-form" action="<c:url value='/logout'/>" method="post" style="display: none;">
                <sec:csrfInput/>
            </form>
        </sec:authorize>
    </li>
</ul>