<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Web app</title>
  </head>
  <body>
    <h1 align="center">${message}</h1>

    <table style="width:100%">
      <tr>
        <th>Firstname</th>
        <th>Lastname</th>
        <th>Age</th>
      </tr>
      <tr>
        <td>Jill</td>
        <td>Smith</td>
        <td>50</td>
      </tr>
      <tr>
        <td>Eve</td>
        <td>Jackson</td>
        <td>94</td>
      </tr>
    </table>

    <form method="POST"
          action="SelectBeer.do">
      Select beer characteristics<p>
      Color:
      <select name="color" size="1">
        <option value="light">light</option>
        <option value="amber">amber</option>
        <option value="brown">brown</option>
        <option value="dark">dark</option>
      </select>
     <input type="SUBMIT">
    </form>
  </body>
</html>
