module com.example.lesothotriviagame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.lesothotriviagame to javafx.fxml;
    exports com.example.lesothotriviagame;
}