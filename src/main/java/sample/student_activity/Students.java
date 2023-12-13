package sample.student_activity;

/**
 * Класс хранит данные студента
 * Нужен дял вывода списка студентов в таблицу
 */
public class Students {

    private String name;
    private String surname;

    /**
     * Конструктор класса
     * @param name имя
     * @param surname фамилия
     */
    public Students(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    /**
     * Возврящает имя
     * @return имя
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает фамилию
     * @return фамилия
     */
    public String getSurname() {
        return surname;
    }
}
