/**
 * Provides the starter code for the <strong>cs1302-omega</strong> project.
 */
module cs1302uga.api {
    requires transitive java.logging;
    requires transitive java.net.http;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.web;
    requires transitive com.google.gson;
    opens cs1302.api;
    opens cs1302.api.models to com.google.gson;  // Allow Gson to access model classes
    exports cs1302.api;
    exports cs1302.api.models;
    exports cs1302.api.services;
    exports cs1302.api.ui;
} // module