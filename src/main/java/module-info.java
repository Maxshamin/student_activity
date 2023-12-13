module sample.student_activity {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.logging.log4j;


    opens sample.student_activity to javafx.fxml;
    exports sample.student_activity;
}