<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.dao.impl.MealDaoImpl, ru.javawebinar.topjava.model.Meal" %>
<%
    String id = request.getParameter("id");
    String dateTime = "";
    String description = "";
    String calories = "";
    if(id != null){
       Integer mealId = Integer.parseInt(id);
        MealDaoImpl mealDao = new MealDaoImpl();
        Meal meal = mealDao.findById(mealId);
        dateTime = meal.getDateTime().toString();
        description = meal.getDescription();
        calories = String.valueOf(meal.getCalories());
    }
%>
<html lang="ru">
<head>
    <title>List Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${title}</h2>
<form action="meals?id=<%= id %>" method="POST">

    <table>
        <tbody>
        <tr>
            <td>Date :</td>
            <td><input type="datetime-local" name="localDate" value= "<%= dateTime %>" ></td>
        </tr>
        <tr>
            <td>Description :</td>
            <td><input type="text" name="description" value= "<%= description %>" ></td>
        </tr>
        <tr>
            <td>Calories :</td>
            <td><input type="number" name="calories" value= "<%= calories %>" ></td>
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