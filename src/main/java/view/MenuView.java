package view;

import controller.*;
import javafx.beans.property.SimpleBooleanProperty;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuView {
    private final Controller controller;
    //DROP FRAME
    private final SimpleBooleanProperty drop_menu = new SimpleBooleanProperty(false);

    //GO CONSOLE MODE
    private final SimpleBooleanProperty console_mode = new SimpleBooleanProperty(false);
    //CREATE NEW HERO
    private final SimpleBooleanProperty new_hero = new SimpleBooleanProperty(false);
    //SELECT HERO
    private final SimpleBooleanProperty select_hero = new SimpleBooleanProperty(false);
    //PLAY
    private final SimpleBooleanProperty play = new SimpleBooleanProperty(false);

    //SELECT ENABLE
    private final SimpleBooleanProperty enable_select = new SimpleBooleanProperty();
    //PLAY ENABLE
    private final SimpleBooleanProperty enable_play = new SimpleBooleanProperty();
    
    private final JFrame frame;
    private final int frame_size = 1000;

    //Buttons
    private final JButton console_mode_button = new JButton("Console");
    private final JButton new_hero_button = new JButton("New Hero");
    private final JButton select_hero_button = new JButton("Select an Hero");
    private final JButton play_button = new JButton("Play");

    public MenuView(Controller controller){
        this.controller =controller;
        frame = new JFrame("Swing my swing MENU!!!");

        configureMenuView();
        configureFrame();
        configureBindings();
        configureListener();

        frame.setVisible(true);
    }

    private void configureFrame(){
        //To close the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(frame_size, frame_size));
        frame.pack();

        frame.add(console_button);
        frame.add(new_hero_button);
        frame.add(select_hero_button);
        frame.add(play_button);
        frame.setLayout(new GridLayout(4, 1));
        //center on the screen
        frame.setLocationRelativeTo(null);
    }

    private void configureMenuView(){
        configureConsoleButton();
        configureNewHeroButton();
        configureSelectHeroButton();
        configurePlayButton();
    }

    private void configureConsoleButton(){
        console_mode_button.addActionListener(e -> {
            console_mode.setValue(true)
            this.frame.dispose();
        });
    }

    private void configureNewHeroButton(){
        new_hero_button.addActionListener(e -> {
            new_hero.setValue(true);
            this.frame.dispose();
        });
    }

    private void configureSelectHeroButton(){
        select_hero_button.addActionListener(e -> {
            select_hero.setValue(true);
            this.frame.dispose();
        });
    }

    private void configurePlayButton(){
        play_button.addActionListener(e -> {
            if (controller.getModel().getCurrentHero() != null) {
                play.setValue(true);
                this.frame.dispose();
            }
        });
    }

    private void configureBindings(){
        this.drop_menu.bindBidirectional(this.controller.dropMenuProperty());

        this.console_mode.bindBidirectional(this.controller.consoleModeProperty());
        this.new_hero.bindBidirectional(this.controller.newHeroProperty());
        this.select_hero.bindBidirectional(this.controller.selectHeroProperty());
        this.play.bindBidirectional(this.controller.playProperty());

        this.play_enable.bindBidirectional(this.controller.playEnableProperty());
        this.select_enable.bindBidirectional(this.controller.selectEnableProperty());
    }

    private void configureListener(){
        this.enable_select.addListener((obs, old, newValue) ->{
            if (newValue){
                select_hero_button.setEnabled(true);
            }else{
                select_hero_button.setEnabled(false);
            }
        });

        this.enable_play.addListener((obs, old, newValue) ->{
            if (newValue){
                play_button.setEnabled(true);
            }else{
                play_button.setEnabled(false);
            }
        });
    }
}
