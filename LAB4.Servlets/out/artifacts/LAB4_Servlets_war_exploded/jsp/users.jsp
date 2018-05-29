<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<h1 align="center">${requestScope.hotel_name}</h1>
<h1 align="center">
    <c:forEach begin="1" end="${requestScope.hotel_stars}">☆</c:forEach>
</h1>

<table align="center">
    <tr>
        <td align="center">Login:</td>
        <td align="center">${sessionScope.login}</td>
    </tr>
    <tr>
        <td align="center">Type:</td>
        <td align="center">${sessionScope.type}</td>
    </tr>
</table>
<form action = "logout" method="get">>
    <h6 align="center"><input align="center" type="submit" value="Log out"/></h6>
</form>

<table style="width:50%" align="center" border="1">
    <caption>
        <h2>Users: </h2>
    </caption>
    <tr>
        <th width="100">Id</th>
        <th width="100">Login</th>
        <th width="100">Remove user</th>
        <th width="100">Admin access</th>
    </tr>
    <c:forEach items="${requestScope.users}" var="user">
        <tr>
            <td align="center">
                ${user.id}
            </td>
            <td align="center">
                    ${user.login}
            </td>
            <td align="center">
                <form action="DeleteUser.do">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="hidden" name="login" value="${user.login}">
                    <button>Remove</button>
                </form>
            </td>
            <td align="center">
                <c:choose>
                    <c:when test="${user.type == 'admin'}">
                        <h5 align="center" style="color: crimson">Admin</h5>
                        <form action="AdminAccess.do">
                            <input type="hidden" name="doing" value="delete">
                            <input type="hidden" name="id" value="${user.id}">
                            <input type="hidden" name="login" value="${user.login}">
                            <button>Remove admin access</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <h5 align="center">User</h5>
                        <form action="AdminAccess.do">
                            <input type="hidden" name="doing" value="add">
                            <input type="hidden" name="id" value="${user.id}">
                            <input type="hidden" name="login" value="${user.login}">
                            <button>Set admin access</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>

<form action="/">
    <h6 align="center"><input align="center" type="submit" value="Go to main page"/></h6>
</form>
</body>
</html>