<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${requestScope.title}</title>
</head>
<body>
    <h1 align="center">${requestScope.hotel_name}</h1>
    <h1 align="center">
        <c:forEach begin="1" end="${requestScope.hotel_stars}">☆</c:forEach>
    </h1>
    <h3 align="center">${requestScope.message}<h3/>
        <form action="/">
            <h6 align="center"><input align="center" type="submit" value="Go to main page"/></h6>
        </form>
</body>
</html>
