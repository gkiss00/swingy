package view;

import controller.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateView {
    private final Controller controller;
    //DROP CREATE
    private final SimpleBooleanProperty drop_create = new SimpleBooleanProperty(false);
    //ERRORS
    private final SimpleIntegerProperty input_errors = new SimpleIntegerProperty(0);

    //the view
    private final JFrame frame;
    private final int frame_size = 1000;
    private final JLabel name_label = new JLabel("Name  : ");
    private final JTextArea name_input = new JTextArea();
    private final JLabel class_label = new JLabel("Class : ");
    private final JTextArea class_input = new JTextArea();
    private final JLabel errors = new JLabel("");
    private final JButton validate = new JButton("Validate");

    //error messages
    private final String error_msg0 = "";
    private final String error_msg1 = "Pseudo already taken";
    private final String error_msg2 = "Your hero class can't be empty";

    public CreateView(Controller controller){
        this.controller = controller;
        frame = new JFrame("SWING NEW HERO");

        configureErrors();
        configureFrame();
        configureBindings();
        configureValidate();
        configureListener();

        frame.setVisible(true);
    }

    private void configureFrame(){
        //To close the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(frame_size, frame_size));
        frame.pack();

        frame.add(name_label);
        frame.add(name_input);
        frame.add(class_label);
        frame.add(class_input);
        frame.add(errors);
        frame.add(validate);
        frame.setLayout(new GridLayout(3, 2));
        //center on screen
        frame.setLocationRelativeTo(null);
    }

    private void configureErrors(){
        errors.setForeground(Color.RED);
    }

    private void configureValidate(){
        validate.addActionListener(e -> {
            controller.addNewHero(name_input.getText(), class_input.getText());
        });
    }

    private void configureBindings(){
        this.input_errors.bindBidirectional(controller.inputErrorsProperty());

        this.drop_create.bindBidirectional(controller.dropCreateProperty());
    }

    private void configureListener(){
        this.input_errors.addListener((obs, old, newValue) -> {
            if ((int)newValue == 0)
                errors.setText(error_msg0);
            else if ((int)newValue == 1)
                errors.setText(error_msg1);
            else
                errors.setText(error_msg2);
        });

        this.drop_create.addListener((obl, old, newValue) -> {
            if (newValue)
                this.frame.dispose();
        });
    }
}
