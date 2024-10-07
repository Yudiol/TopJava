package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.impl.MealDaoImpl;
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
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private MealDaoImpl mealDao;
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() {
        this.mealDao = new MealDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        List<MealTo> meals = MealsUtil.filteredByStreams(mealDao.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        request.setAttribute("meals", meals);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
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
        } else if (Objects.equals(id, "null")) {
            log.debug("Create meal");
            mealDao.create(new Meal(0,
                    LocalDateTime.parse(request.getParameter("localDate")),
                    description,
                    Integer.parseInt(request.getParameter("calories"))));
        } else {
            log.debug("Update meal");
            mealDao.update(new Meal(Integer.parseInt(id),
                    LocalDateTime.parse(request.getParameter("localDate")),
                    description,
                    Integer.parseInt(request.getParameter("calories"))));
        }
        response.sendRedirect("/topjava/meals");
    }
}