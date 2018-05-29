<%@ page language="java" import="src.hotel.*" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="hotel.css">
    <title>${requestScope.hotel_name}</title>
</head>
<body>
    <h1 align="center">${requestScope.hotel_name}</h1>
    <h1 align="center">
        <c:forEach begin="1" end="${requestScope.hotel_stars}">☆</c:forEach>
    </h1>

    <c:choose>
        <c:when test="${sessionScope.login == null}">

            <form action = "login" method="post">>
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
                </table>
                <h6 align="center"><input align="center" type="submit" value="Sign in"/></h6>
            </form>
            <form action="SignUp.do" method="get">
                <h6 align="center"><input align="center" type="submit" value="Sign up"/></h6>
            </form>
        </c:when>
        <c:when test="${sessionScope.login == 'incorrect'}">
            <h4 style="color:red" align="center">Incorrect login or password!</h4>
            <form action = "login" method="post">>
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
                </table>
                <h6 align="center"><input align="center" soring_type="sortBy" type="submit" value="Sign in"/></h6>
            </form>
        </c:when>
        <c:otherwise>
            <table align="center" border="1">
                <tr>
                    <td align="center" width="150">Login:</td>
                    <td align="center" width="150">${sessionScope.login}</td>
                </tr>
                <tr>
                    <td align="center">Type:</td>
                    <td align="center">${sessionScope.type}</td>
                </tr>
            </table>
            <form action = "logout" method="get">>
                <h6 align="center"><input align="center" type="submit" value="Log out"/></h6>
            </form>
            <form action="booked">
                <h6 align="center"><input align="center" type="submit" value="Booked rooms"/></h6>
            </form>
            <c:choose>
                <c:when test="${sessionScope.type == 'admin'}">
                    <form action="users">
                        <h6 align="center"><input align="center" type="submit" value="Watch Users"></h6>
                    </form>
                </c:when>
            </c:choose>
        </c:otherwise>
    </c:choose>
    <table style="width:50%" align="center" border="1">
        <caption>
            <h2>Available rooms: </h2>
            <form action = "" method="get">
                <table>
                    <tr>
                        <td><button name="room_sorting" value="id" type="submit">Sort by id</button></td>
                        <td><button name="room_sorting" value="amountOfBeds" type="submit">Sort by beds</button></td>
                        <td><button name="room_sorting" value="pricePerDay" type="submit">Sort by price</button></td>
                    </tr>
                </table>
            </form>
        </caption>

        <tr>
            <th width="50">Room ID</th>
            <th width="50">Amount of Beds</th>
            <th width="50">Price per day</th>
            <th width="200">Description</th>
            <th width="200">Book</th>
        </tr>
        <c:forEach items="${requestScope.hotel_rooms}" var="room">
        <tr>
            <td align="center">${room.id}
                <c:choose>
                    <c:when test="${sessionScope.type == 'admin'}">
                        <form action="DeleteRoom.do" method="post">
                            <input type="hidden" name="roomId" value="${room.id}"/>
                            <button>Remove</button>
                        </form>
                        <form action="RoomBookedDates.do">
                            <input type="hidden" name="roomId" value="${room.id}"/>
                            <button>Booked dates</button>
                        </form>
                    </c:when>
                </c:choose>
            </td>
            <td align="center">${room.amountOfBeds}</td>
            <td align="center"> ${room.pricePerDay}</td>
            <td align="center"> ${room.description}</td>

            <td>
                <form action="book" method="post">
                    <input type="hidden" name="roomId" value="${room.id}">
                    <table align="center" style="padding-top:6px;">
                        <tr>
                            <th width="60"></th>
                            <th width="70"></th>
                            <th width="60"></th>
                        </tr>
                        <tr>
                            <td>
                                First date:
                            </td>
                            <td>
                                <input type="date" name="arrival_date" min="${requestScope.min_date}"
                                       max="${requestScope.max_date}" required><br/>
                            </td>
                            <td align="right" rowspan="2">
                                <button>Book</button>
                            </td>

                        </tr>

                        <tr>
                            <td>
                                Last date:
                            </td>
                            <td>
                                <input type="date" name="leaving_date" min="${requestScope.min_date}"
                                       max="${requestScope.max_date}" required>
                            </td>

                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        </c:forEach>
    <table/>

        <h4 align="center">©Pekhovsky 2018</h4>
</body>
</html>

