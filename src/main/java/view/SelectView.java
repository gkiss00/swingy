package view;

import controller.*;
import javafx.beans.property.SimpleBooleanProperty;
import model.hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;

public class SelectView {
    private final Controller controller;

    //LISTES
    private List<Hero> heroes;
    private final List<JButton> buttons = new ArrayList<>();
    private int actual;

    //DROP FRAME
    private final SimpleBooleanProperty drop_select = new SimpleBooleanProperty();

    //frame
    private JFrame frame;
    private final int frame_size = 1000;

    public SelectView(Controller controller){
        this.controller = controller;
        heroes = controller.getAllHeroes();

        configuresButtons();
        configureFrame();
        configureBindings();
        configureListener();

        frame.setVisible(true);
    }

    private void configureFrame(){
        frame = new JFrame("SWING SELECT HERO");
        //To close the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(frame_size, frame_size));
        frame.pack();

        for (int i = 0; i < heroes.size(); ++i){
            frame.add(buttons.get(i));
        }

        frame.setLayout(new GridLayout(heroes.size(), 1));
        //center the screen
        frame.setLocationRelativeTo(null);
    }

    //ONE BUTTON BY HERO, CLICK TO SELECT THE HERO
    private void configuresButtons(){
        buttons.clear();
        for (int i = 0; i < heroes.size(); ++i){
            String text = heroes.get(i).getName();
            text += " lvl ";
            text += Integer.toString(heroes.get(i).getLevel());
            text += " of class ";
            text += heroes.get(i).get_Class();
            buttons.add(new JButton(text));
        }

        for (int i = 0; i < buttons.size(); ++i){
            buttons.get(i).addActionListener(e -> {
                this.frame.dispose();
                controller.selectHero(e.getActionCommand());
            });
        }
    }

    private void configureBindings(){
        this.drop_select.bindBidirectional(this.controller.dropSelectProperty());
    }

    private void configureListener(){

    }
}
