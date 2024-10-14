package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        return service.save(authUserId(), meal);
    }

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public void delete(int id) {
        log.info("Delete id={}", id);
        service.delete(authUserId(), id);
    }

    public Collection<MealTo> getAll() {
        return service.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public List<MealTo> filter(String fromDate, String toDate, String fromTime, String toTime) {
        log.info("filter");
        return service.filter(fromDate, toDate, fromTime, toTime, authUserId(), authUserCaloriesPerDay());
    }
}