package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Store;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    {
        repository.put(1, new ConcurrentHashMap<>());
        repository.put(2, new ConcurrentHashMap<>());
        Store.meals0.forEach(meal -> save(1, meal));
        Store.meals1.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        return repository.get(userId).computeIfPresent(meal.getId(), (id, meals) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return repository.get(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.get(userId).values();
    }

    @Override
    public Collection<Meal> filter(int userId, LocalDate from, LocalDate to) {
        return repository.get(userId).values().stream()
                .filter(meal -> meal.getDateTime().toLocalDate().isAfter(from) && meal.getDateTime().toLocalDate().isBefore(to))
                .collect(Collectors.toList());
    }
}

