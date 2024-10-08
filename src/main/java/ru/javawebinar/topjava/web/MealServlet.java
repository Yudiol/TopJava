package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private final MealDao mealDao = new MealDaoInMemory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        String forward = "/newMeal.jsp";
        if ("update".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("meal", mealDao.findById(id));
            request.setAttribute("action", "update");
            log.debug("Edit Meal");
        } else if ("create".equalsIgnoreCase(action)) {
            request.setAttribute("action", "create");
            log.debug("Add property new meal");
        } else {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealDao.findAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealsTo);
            forward = "/meals.jsp";
            log.debug("Show all meals");
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String description = request.getParameter("description");

        if ("delete".equalsIgnoreCase(action)) {
            log.debug("Delete Meal");
            mealDao.delete(Integer.parseInt(id));
        } else if ("create".equalsIgnoreCase(action)) {
            log.debug("Create meal");
            mealDao.create(new Meal(0,
                    LocalDateTime.parse(request.getParameter("localDate")),
                    description,
                    Integer.parseInt(request.getParameter("calories"))));
        } else if ("update".equalsIgnoreCase(action)) {
            log.debug("Update meal");
            mealDao.update(new Meal(Integer.parseInt(id),
                    LocalDateTime.parse(request.getParameter("localDate")),
                    description,
                    Integer.parseInt(request.getParameter("calories"))));
        }
        response.sendRedirect("/meals");
    }
}