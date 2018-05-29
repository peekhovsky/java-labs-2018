<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Booked dates</title>
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
        <h2>Booked rooms (room id: ${requestScope.roomId})</h2>
    </caption>
    <tr>
        <th width="100">Date</th>
        <th width="100">User</th>
        <th width="100">Remove booking</th>
    </tr>
    <c:forEach items="${requestScope.datesAndLogins}" var="dateAndLogin">
        <tr>
            <td align="center"><fmt:formatDate pattern = "dd.MM.yyyy"
                                               value = "${dateAndLogin.date}"/>
            </td>
            <td align="center">
                    ${dateAndLogin.login}
            </td>
            <td align="center">
                <form action="DeleteBooking.do" method="post">
                    <input type="hidden" name="date" value="<fmt:formatDate pattern = "dd.MM.yyyy"
                        value = "${dateAndLogin.date}"/>}">
                    <input type="hidden" name="roomId" value="${requestScope.roomId}">
                    <input type="hidden" name="type" value="admin">
                    <h6>
                        <br><button>Cancel</button>
                    </h6>
                </form>
            </td>
            </tr>
        </c:forEach>
    </table>

    <form action="/">
        <h6 align="center"><input align="center" type="submit" value="Go to main page"/></h6>
    </form>
</body>
</html>
