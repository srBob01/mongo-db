package ru.arsentiev.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.arsentiev.application.model.Expense;

import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    Optional<Expense> findExpenseByExpenseName(String expenseName);
}
