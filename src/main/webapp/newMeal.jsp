<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Edit or create meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit Meals</h2>
<form action="meals?action=${action}&id=${meal.id}" method="POST">

    <table>
        <tbody>
        <tr>
            <td>Date :</td>
            <td><input type="datetime-local" name="localDate" value=${meal.dateTime.toString()}></td>
        <tr/>
        <tr>
            <td>Description :</td>
            <td><input type="text" name="description" value=${meal.description}></td>
        <tr/>
        <tr>
            <td>Calories :</td>
            <td><input type="number" name="calories" value=${meal.calories}></td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="Save">
                <button onclick="window.history.back()" type="button">Cancel</button>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>