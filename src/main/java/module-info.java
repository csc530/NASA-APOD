module space.nasa.spaceapi {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;
	requires com.google.gson;
	requires java.net.http;
	opens space.nasa.spaceapi to javafx.fxml;
	exports space.nasa.spaceapi;
	exports space.nasa.spaceapi.controllers to javafx.fxml;
	opens space.nasa.spaceapi.controllers to javafx.fxml;
	opens space.nasa.spaceapi.models to com.google.gson;
	exports space.nasa.spaceapi.models to com.google.gson;
}