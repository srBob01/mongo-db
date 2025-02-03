package ru.arsentiev.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.arsentiev.application.model.Expense;
import ru.arsentiev.application.repository.ExpenseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public void addExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public void updateExpense(Expense expense) {
        Expense expenseToUpdate = expenseRepository.findById(expense.getId()).orElseThrow(
                () -> new IllegalArgumentException("Expense" + expense.getId() + " not found"));

        expenseToUpdate.setExpenseName(expense.getExpenseName());
        expenseToUpdate.setCategory(expense.getCategory());
        expenseToUpdate.setExpenseAmount(expense.getExpenseAmount());

        expenseRepository.save(expenseToUpdate);
    }

    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseByName(String name) {
        return expenseRepository.findExpenseByExpenseName(name)
                .orElseThrow(() -> new IllegalArgumentException("Expense" + name + " not found"));
    }
}
