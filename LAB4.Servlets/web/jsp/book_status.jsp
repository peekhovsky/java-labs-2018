<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Booking</title>
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

            <form action = "logout" method="get">
            <h6 align="center"><input align="center" type="submit" value="Log out"/></h6>
        </form>

    <c:choose>
        <c:when test="${requestScope.status == 'correct'}">
            <h3 align="center">Room has been booked!</h3>
        </c:when>
        <c:otherwise>
            <h3 align="center">Cannot book room!</h3>
        </c:otherwise>
    </c:choose>

    <form action="/">
        <h6 align="center"><input align="center" type="submit" value="Go to main page"/></h6>
    </form>

    </body>
</html>
