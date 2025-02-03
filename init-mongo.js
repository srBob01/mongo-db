db = db.getSiblingDB('expense_tracker');

db.createUser({
    user: "admin",
    pwd: "secret",
    roles: [
        {role: "readWrite", db: "expense_tracker"},// Разрешает чтение и запись в 'expense_tracker'
        {role: "dbOwner", db: "expense_tracker"},  // Полный контроль над базой
        {role: "root", db: "admin"}  // Полный административный доступ
    ]
});

print("User 'admin' created successfully with full access to 'expense_tracker' database");
