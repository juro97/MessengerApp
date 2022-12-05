module fhtw.ode.messengerapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens fhtw.ode.messengerapp to javafx.fxml;
    exports fhtw.ode.messengerapp;
}