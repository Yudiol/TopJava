package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String mealId = request.getParameter("id");
        Integer id = mealId.isEmpty() ? null : Integer.valueOf(mealId);
        Meal meal = new Meal(id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (!meal.isNew()) {
            log.info("Update {}", meal);
            mealRestController.update(id, meal);
        } else {
            log.info("Create {}", meal);
            mealRestController.save(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                log.info("filter");
                String fromDate = request.getParameter("fromDate");
                String toDate = request.getParameter("toDate");
                String fromTime = request.getParameter("fromTime");
                String toTime = request.getParameter("toTime");
                request.setAttribute("meals", mealRestController.filter(
                                getLocalDate("from", fromDate),
                                getLocalDate("to", toDate),
                                getLocalTime("from", fromTime),
                                getLocalTime("to", toTime)
                        )
                );
                request.setAttribute("dateTime", Arrays.asList(fromDate, toDate, fromTime, toTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    private LocalDate getLocalDate(String pointer, String localDate) {
        try {
            return LocalDate.parse(localDate);
        } catch (Exception e) {
            return Objects.equals(pointer, "from") ? LocalDate.MIN : LocalDate.MAX;
        }
    }

    private LocalTime getLocalTime(String pointer, String localTime) {
        try {
            return LocalTime.parse(localTime);
        } catch (Exception e) {
            return Objects.equals(pointer, "from") ? LocalTime.MIN : LocalTime.MAX;
        }
    }
}
