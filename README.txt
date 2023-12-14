# Запуск программы
Для запуска программы необходимо колонировать репозиторий и открыть его в Intellij IDEA. Может возникнуть необходимость
добавить драйвер MySQL базы данных. Скачайте его с сайта https://dev.mysql.com/downloads/connector/j/5.1.html для своей
операционной системы и добавьте в проект. Создайте базу данных по инструкции ниже


# Создание базы данных

Для работы приложения необходимо установить MySQL Server и создать базу данных (можно использовать MySQL Workbench).
Название базы данных должно быть spbgut.
SQL код для создания базы данных: CREATE SCHEMA `spbgut` ;

База данных должна содержать таблицы:

    1. groups с полями:
        id (int PK NN AI)
        group (VARCHAR(45) NN)
    SQL код для создания таблицы:
    CREATE TABLE `spbgut`.`groups` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `group` VARCHAR(45) NOT NULL,
      PRIMARY KEY (`id`));

    2. practical_works с полями:
            id (int PK NN AI)
            practical_work (VARCHAR(45) NN)
        SQL код для создания таблицы:
        CREATE TABLE `spbgut`.`practical_works` (
          `id` INT NOT NULL AUTO_INCREMENT,
          `practical_work` VARCHAR(45) NOT NULL,
          PRIMARY KEY (`id`));

    3. classes с полями:
        id (int PK NN AI)
        class (VARCHAR(45) NN)
    SQL код для создания таблицы:
    CREATE TABLE `spbgut`.`classes` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `class` VARCHAR(45) NOT NULL,
      PRIMARY KEY (`id`));

Импорт базы данных находится в папке DumpMSQL

# Подключения к базе данных

Этот репозиторий содержит XML-файл (`src/main/resources/DataBaseConfigs.xml`), предназначенный для хранения параметров
подключения к базе данных. Следуйте инструкциям ниже, чтобы заполнить необходимую информацию.

## Инструкции:

   **Заполните параметры подключения:**
   Замените значения-заполнители на реальные данные подключения к вашей базе данных.

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <databaseConnection>
       <host>Ваш_Хост_Базы_Данных</host>
       <port>Ваш_Порт_Базы_Данных</port>
       <databaseName>spbgut</databaseName>
       <username>Ваше_Имя_Пользователя</username>
       <password>Ваш_Пароль</password>
   </databaseConnection>

   Обратите внимание, что имя базы данных должно быть spbgut!!!