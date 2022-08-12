package ru.msaggik.spring.dao;

import org.springframework.stereotype.Component;
import ru.msaggik.spring.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PeopleDAO {
    private static int PEOPLE_COUNT; // id пользователя

    // подключение базы данных:
    // 1) константы
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    // 2) создание соединения
    private static Connection connection;
    // 3) инициализация соединения
    static {
        try {
            Class.forName("org.postgresql.Driver"); // подгрузка драйвера
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // использование драйвера для подключения БД
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() { // возврат всех пользователей из БД
        List<Person> people = new ArrayList<>();

        try {
            // объект для sql запроса к БД
            Statement statement = connection.createStatement();
            // sql запрос
            String SQL = "SELECT * FROM Person";
            // выполнение sql запроса к БД и принятие его на объект resultSet
            ResultSet resultSet = statement.executeQuery(SQL);
            // перевод полученных строк в JAVA объект
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    public Person show(int id) {
        Person person = null;

        try {
            // объект для sql запроса к БД
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Person WHERE id=?");

            preparedStatement.setInt(1, id);

            // выполнение sql запроса
            ResultSet resultSet = preparedStatement.executeQuery();

            // получение первой строки из БД
            resultSet.next();

            // перевод полученной строки в JAVA объект
            person = new Person();

            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return person;
    }

    public void save(Person person) {
        try {
            // объект для sql запроса к БД
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());

            // выполнение sql запроса
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // обновление данных пользователя
    public void update(int id, Person updatedPerson) {
        try {
            // объект для sql запроса к БД
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?");

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            // выполнение sql запроса
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    // удаление данных пользователя
    public void delete(int id) {
        try {
            // объект для sql запроса к БД
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM Person WHERE id=?");

            preparedStatement.setInt(1, id);

            // выполнение sql запроса
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
