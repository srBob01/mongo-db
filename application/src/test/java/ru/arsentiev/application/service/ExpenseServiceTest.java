package ru.arsentiev.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;
import ru.arsentiev.application.model.Expense;
import ru.arsentiev.application.model.ExpenseCategory;
import ru.arsentiev.application.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Testcontainers
class ExpenseServiceTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    @BeforeEach
    void cleanDataBase() {
        expenseRepository.deleteAll();
    }

    @Test
    void shouldSaveExpense() {
        // Создаём объект расхода
        Expense expense = new Expense(null, "Groceries", ExpenseCategory.GROCERIES, BigDecimal.valueOf(100.50));

        // Сохраняем через сервис
        expenseService.addExpense(expense);

        // Проверяем, что запись появилась в базе
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(1);
        assertThat(expenses.getFirst().getExpenseName()).isEqualTo("Groceries");
    }

    @Test
    void shouldFindAllExpenses() {
        // Добавляем несколько записей
        expenseRepository.save(new Expense(null, "Shopping", ExpenseCategory.SHOPPING, BigDecimal.valueOf(200.00)));
        expenseRepository.save(new Expense(null, "Transport", ExpenseCategory.TRANSPORT, BigDecimal.valueOf(50.00)));

        // Получаем все расходы через сервис
        List<Expense> expenses = expenseService.getAllExpenses();

        // Проверяем, что они есть
        assertThat(expenses).hasSize(2);
    }

    @Test
    void shouldFindExpenseByName() {
        // Создаём и сохраняем расход
        Expense expense = expenseRepository.save(new Expense(null, "Entertainment", ExpenseCategory.ENTERTAINMENT, BigDecimal.valueOf(120.00)));

        // Находим по имени
        Expense foundExpense = expenseService.getExpenseByName("Entertainment");

        // Проверяем, что найден правильный объект
        assertThat(foundExpense).isNotNull();
        assertThat(foundExpense.getExpenseName()).isEqualTo("Entertainment");
    }

    @Test
    void shouldUpdateExpense() {
        // Сохраняем расход
        Expense expense = expenseRepository.save(new Expense(null, "Shopping", ExpenseCategory.SHOPPING, BigDecimal.valueOf(250.00)));

        // Обновляем сумму
        expense.setExpenseAmount(BigDecimal.valueOf(300.00));
        expenseService.updateExpense(expense);

        // Проверяем обновление
        Optional<Expense> updatedExpense = expenseRepository.findById(expense.getId());
        assertThat(updatedExpense).isPresent();
        assertThat(updatedExpense.get().getExpenseAmount()).isEqualTo(BigDecimal.valueOf(300.00));
    }

    @Test
    void shouldDeleteExpense() {
        // Создаём и сохраняем расход
        Expense expense = expenseRepository.save(new Expense(null, "Transport", ExpenseCategory.TRANSPORT, BigDecimal.valueOf(50.00)));

        // Удаляем через сервис
        expenseService.deleteExpense(expense.getId());

        // Проверяем, что удалилось
        Optional<Expense> deletedExpense = expenseRepository.findById(expense.getId());
        assertThat(deletedExpense).isEmpty();
    }
}