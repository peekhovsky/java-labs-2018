<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <h1 align="center">${requestScope.hotel_name}</h1>
    <h1 align="center">
        <c:forEach begin="1" end="${requestScope.hotel_stars}">☆</c:forEach>
    </h1>
    <c:choose>
        <c:when test="${requestScope.status != null}">
    <h4 align="center" style="color: red">${requestScope.status}</h4>
        </c:when>
    </c:choose>
    <form action = "AddUser.do" method="post">
        <table align="center">
            <tr>
                <td align="center">User Name:</td>
                <td align="center"><label>
                    <input type="text" name="login"/>
                </label></td>
            </tr>
            <tr>
                <td align="center">Password:</td>
                <td align="center"><label>
                    <input type="password" name="password"/>
                </label></td>
            </tr>
            <tr>
                <td align="center">Repeat:</td>
                <td align="center"><label>
                    <input type="password" name="repeatPassword"/>
                </label></td>
            </tr>
        </table>
        <h6 align="center"><input align="center" type="submit" value="Sign in"/></h6>
    </form>
</body>
</html>
