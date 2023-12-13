package sample.student_activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Работа с базой данных
 */
public class DatabaseHandler extends Configs{
    Connection dbConnection;

    private static final Logger logger = LogManager.getLogger();

    /**
     * Подключение к базе данных
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        logger.info("Создание подключения к базе данных");
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);

        return dbConnection;
    }

    /**
     * Создание новой группы
     * Создает все необходимые таблицы для хранения данных группы
     * @param Group группа
     * @param Students студенты
     */
    public void NewGroup(String Group, String Students){
        logger.info("Создание новой группы");
        //Добавление группы в таблицу с группами
        String insert = "INSERT INTO `spbgut`.`groups` (`group`) VALUES ('"+ Group +"');" ;
        //Создание таблицы оценок новой группы
        String insert2 = "CREATE TABLE `spbgut`.`"+ Group +"` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `name` VARCHAR(45) NOT NULL," +
                "  `surname` VARCHAR(45) NOT NULL," +
                "  PRIMARY KEY (`id`));";

        //Создание таблицы занятий новой группы
        String insert3 = "CREATE TABLE `spbgut`.`"+ Group +"Classes"+"` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT," +
                "  `name` VARCHAR(45) NOT NULL," +
                "  `surname` VARCHAR(45) NOT NULL," +
                "  PRIMARY KEY (`id`));";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.executeUpdate();
            prSt = getDbConnection().prepareStatement(insert2);
            prSt.executeUpdate();
            prSt = getDbConnection().prepareStatement(insert3);
            prSt.executeUpdate();
            //Добавление студентов в таблицу оценок и в таблицу занятий группы
            for (String Student:Students.split("\n")){
                String name = Student.split(" ")[0];
                String surname = Student.split(" ")[1];
                //Таблица оценок
                insert = "INSERT INTO `spbgut`.`"+ Group +"` (`name`, `surname`) VALUES ('"+ name +"', '"+ surname +"');";
                prSt = getDbConnection().prepareStatement(insert);
                prSt.executeUpdate();
                //Таблица занятий
                insert = "INSERT INTO `spbgut`.`"+ Group +"Classes"+"` (`name`, `surname`) VALUES ('"+ name +"', '"+ surname +"');";
                prSt = getDbConnection().prepareStatement(insert);
                prSt.executeUpdate();
            }
            //Добавление в таблицу оценок группы столбцов всех практических работ
            for (String Work: Arrays.asList(getPracticalWork())){
                insert = "ALTER TABLE `spbgut`.`"+ Group +"` ADD `"+ Work +"` INT;";
                prSt = getDbConnection().prepareStatement(insert);
                prSt.executeUpdate();
            }
            //Добавление в таблицу занятий группы столбцов всех занятий
            for (String Class: Arrays.asList(getClasses())){
                insert = "ALTER TABLE `spbgut`.`"+ Group +"Classes"+"` ADD `"+ Class +"` INT DEFAULT 0;";
                prSt = getDbConnection().prepareStatement(insert);
                prSt.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение списка всех ГРУПП
     * @return массив со всеми группами
     */
    public String[] getGroups() {
        logger.info("Запрос списка групп");
        ResultSet Set = null;
        String select = "SELECT * FROM spbgut.groups;";
        ArrayList<String> groups = new ArrayList<>();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Set = prSt.executeQuery();
            while (Set.next()) {
                groups.add(Set.getString(2));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return groups.toArray(new String[0]);
    }

    /**
     * Получение списка группы по ее названию
     * @param Group группа
     * @return таблица группы
     */
    public ResultSet GetStudents(String Group){
        logger.info("Запрос списка студентов");
         ResultSet Set = null;
         String select = "SELECT * FROM spbgut."+ Group +";";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Set = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return  Set;
    }

    /**
     * Добавление новой практической работы
     * @param Work практическая работа
     */
    public void NewPracticalWork(String Work){
        logger.info("Создание новой практической работы");
        //Добавление новой практической работы в таблицу с практическими работами
        String insert = "INSERT INTO `spbgut`.`practical_works` (`practical_work`) VALUES ('"+Work+"');";

        //Добавление столбца новой практической работы в таблицу каждой группы
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.executeUpdate();
            for (String group: Arrays.asList(getGroups())){
                insert = "ALTER TABLE `spbgut`.`"+ group +"` ADD `"+ Work +"` INT;";
                PreparedStatement prSt1 = getDbConnection().prepareStatement(insert);
                prSt1.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение списка практических работ
     * @return массив всех практических работ
     */
    public String[] getPracticalWork(){
        logger.info("Запрос списка практических работ");
        ResultSet Set = null;
        String select = "SELECT * FROM spbgut.practical_works;";
        ArrayList<String> groups = new ArrayList<>();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Set = prSt.executeQuery();
            while (Set.next()) {
                groups.add(Set.getString(2));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return groups.toArray(new String[0]);
    }

    /**
     * Выставление оценки за практическую работу
     * @param Group группа
     * @param Surname фамилия
     * @param Practical_work практическая работа
     * @param Grade оценка
     */
    public void NewPracticalWorkGrade(String Group, String Surname, String Practical_work, String Grade){
        logger.info("Выставление оценки");
        String insert = "UPDATE `spbgut`.`"+Group+"` SET `"+Practical_work+"` = '"+Grade+"' WHERE (`surname` = '"+Surname+"');";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавление нового занятия
     * @param Class занятие
     */

    public void NewClass(String Class){
        logger.info("Создание нового занятия");
        //Добавление нового занятия в таблицу с занятиями
        String insert = "INSERT INTO `spbgut`.`classes` (`class`) VALUES ('"+Class+"');";

        //Добавление столбца нового занятия в таблицу занятий каждой группы
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.executeUpdate();
            for (String group: Arrays.asList(getGroups())){
                insert = "ALTER TABLE `spbgut`.`"+ group +"Classes"+"` ADD `"+ Class +"` INT DEFAULT 0;";
                PreparedStatement prSt1 = getDbConnection().prepareStatement(insert);
                prSt1.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение списка занятий
     * @return массив всех занятий
     */

    public String[] getClasses(){
        logger.info("Запрос списка занятий");
        ResultSet Set = null;
        String select = "SELECT * FROM spbgut.classes;";
        ArrayList<String> classes = new ArrayList<>();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Set = prSt.executeQuery();
            while (Set.next()) {
                classes.add(Set.getString(2));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes.toArray(new String[0]);
    }

    /**
     * Отметить на занятии
     * @param Group группа
     * @param Class занятие
     * @param Surname фамилия
     */
    public void SetClass(String Group, String Class, String Surname){
        logger.info("Отметить на занятии");
        String insert = "UPDATE `spbgut`.`"+Group+"Classes"+"` SET `"+Class+"` = '1' WHERE (`surname` = '"+Surname+"');";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение всех оценок одного ученика
     * @param group гуппа
     * @param surname фамилия
     * @param len кол-во работ
     * @return массив оценок
     */

    public int[] getStudentGrades(String group, String surname, int len){
        logger.info("Запрос списка оценок");
        ResultSet Set = null;
        String select = "SELECT * FROM `spbgut`.`"+group+"` WHERE (`surname` = '"+surname+"');";
        int[] grades = new int[len];

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Set = prSt.executeQuery();
            Set.next();
            for (int i = 0; i < len; i++){
                if (Set.getString(i + 4) != null) {
                    grades[i] = Integer.parseInt(Set.getString(i + 4));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return grades;
    }

    /**
     * Получение занятий на которых был отмечен ученик
     * @param group группа
     * @param surname фамилия
     * @param len кол-во занятий
     * @return массив занятий на которых был отмечен ученик
     */
    public int[] getStudentClasses(String group, String surname, int len){
        logger.info("Запрос списка занятий ученика");
        ResultSet Set = null;
        String select = "SELECT * FROM `spbgut`.`"+group+"Classes"+"` WHERE (`surname` = '"+surname+"');";
        int[] grades = new int[len];

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            Set = prSt.executeQuery();
            Set.next();
            for (int i = 0; i < len; i++){
                if (Set.getString(i + 4) != null) {
                    grades[i] = Integer.parseInt(Set.getString(i + 4));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return grades;
    }
}
