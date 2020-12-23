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

    private final SimpleBooleanProperty close_game = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty drop_create = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty alive = new SimpleBooleanProperty();
    private final SimpleIntegerProperty errors_p = new SimpleIntegerProperty(0);

    private final JFrame frame;
    private final int frame_size = 1000;

    private final JLabel name_label = new JLabel("Name  : ");
    private final JTextArea name_input = new JTextArea();
    private final JLabel class_label = new JLabel("Class : ");
    private final JTextArea class_input = new JTextArea();
    private final JLabel errors = new JLabel("");
    private final JButton validate = new JButton("Validate");

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
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                close_game.setValue(true);
                e.getWindow().dispose();
            }
        });

    }

    private void configureErrors(){
        errors.setForeground(Color.RED);
    }

    private void configureValidate(){
        validate.addActionListener(e -> {
            if (controller.validateName(name_input.getText()) && controller.validateClass(name_input.getText())) {
                controller.addNewHero(name_input.getText(), class_input.getText());
                name_input.setText("");
                class_input.setText("");
                alive.setValue(false);
                frame.dispose();
            }
        });
    }

    private void configureBindings(){
        //VISIBILITY
        this.alive.bindBidirectional(controller.createViewAliveProperty());
        //ERRORS INPUT
        this.errors_p.bindBidirectional(controller.errorsCreateViewProperty());
        //DELETE VIEW
        this.drop_create.bindBidirectional(controller.dropCreateViewProperty());
        //DELETE ALL VIEW
        this.close_game.bindBidirectional(controller.closeGameProperty());
    }

    private void configureListener(){
        this.alive.addListener((obs, old, newValue) -> {
            frame.setVisible(newValue);
        });

        this.errors_p.addListener((obs, old, newValue) -> {
            if ((int)newValue == 0)
                errors.setText(error_msg0);
            else if ((int)newValue == 1)
                errors.setText(error_msg1);
            else
                errors.setText(error_msg2);
        });

        this.drop_create.addListener((obs, old, newValue) ->{
            if (newValue)
                frame.dispose();
        });
    }
}
