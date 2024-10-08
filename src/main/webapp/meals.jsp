<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html lang="ru">
<head>
    <title>List Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<form action="meals" method="GET">
<input type="hidden" name="action" value="create">
<button type="submit">Add meal</button>
</form>
<table border="1">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <tr style="color:${meal.excess ? 'red' : 'green'}">
            <td>${meal.dateTime.toString().replace('T',' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <form action="meals" method="GET">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${meal.id}">
                <button type="submit">Edit</button>
                </form>
            </td>
            <td>
                <form action="meals?action=delete&id=${meal.id}" method="POST">
                   <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>