package view;

import controller.*;
import javafx.beans.property.SimpleBooleanProperty;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuView {
    private final Controller controller;
    //CLOSE GAME
    private final SimpleBooleanProperty close_game = new SimpleBooleanProperty(false);
    //CLOSE FRAME
    private final SimpleBooleanProperty drop_menu = new SimpleBooleanProperty(false);
    //GO CONSOLE MODE
    private final SimpleBooleanProperty play_console = new SimpleBooleanProperty(false);
    //VISIBILITY
    private final SimpleBooleanProperty alive = new SimpleBooleanProperty();
    //PLAY ENABLE
    private final SimpleBooleanProperty play_enable = new SimpleBooleanProperty();
    //SELECT ENABLE
    private final SimpleBooleanProperty select_enable = new SimpleBooleanProperty();
    private final SimpleBooleanProperty create_view_alive = new SimpleBooleanProperty();
    private final SimpleBooleanProperty select_view_alive = new SimpleBooleanProperty();
    private final SimpleBooleanProperty play_view_alive = new SimpleBooleanProperty();

    private final SimpleBooleanProperty new_hero_selected = new SimpleBooleanProperty(false);

    private final JFrame frame;
    private final int frame_size = 1000;

    private final JButton console_button = new JButton("Console");
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

        frame.setLocationRelativeTo(null);
        //TO CLOSE VERY OTHERS WINDOWS
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

    private void configureMenuView(){
        configureConsoleButton();
        configureNewHeroButton();
        configureSelectHeroButton();
        configurePlayButton();
    }

    //GO IN CONSOLE MODE
    private void configureConsoleButton(){
        console_button.setPreferredSize(new Dimension(100, 100));
        console_button.addActionListener(e -> play_console.setValue(true));
    }

    private void configureNewHeroButton(){
        new_hero_button.addActionListener(e -> {
            alive.setValue(false);
            create_view_alive.setValue(true);
        });
    }

    private void configureSelectHeroButton(){
        select_hero_button.addActionListener(e -> {
            alive.setValue(false);
            select_view_alive.setValue(true);
        });
    }

    private void configurePlayButton(){
        play_button.addActionListener(e -> {
            if (controller.getModel().getCurrentHero() != null) {
                new_hero_selected.setValue(!new_hero_selected.getValue());
                controller.setMap();
                alive.setValue(false);
                play_view_alive.setValue(true);
            }
        });
    }

    private void configureBindings(){
        //VISIBILITY
        this.alive.bindBidirectional(controller.menuViewAliveProperty());
        //CREATE HERO VISIBILITY
        this.create_view_alive.bindBidirectional(controller.createViewAliveProperty());
        //SELECT HERO VISIBILITY
        this.select_view_alive.bindBidirectional(controller.selectViewAliveProperty());
        //PLAT VISIBILITY
        this.play_view_alive.bindBidirectional(controller.playViewAliveProperty());
        //DELETE MENU
        this.drop_menu.bindBidirectional(controller.dropMenuViewProperty());
        //GO TO CONSOLE MODE
        this.play_console.bindBidirectional(controller.playConsoleProperty());
        //CLOSE GAME
        this.close_game.bindBidirectional(controller.closeGameProperty());
        //ENABLE SELECT HERO
        this.play_enable.bindBidirectional(controller.playEnableProperty());
        //ENABLE PLAY
        this.select_enable.bindBidirectional(controller.selectEnableProperty());

        this.new_hero_selected.bindBidirectional(controller.newHeroSelectedProperty());
    }

    private void configureListener(){
        this.alive.addListener((obs, old, newValue) -> {
            frame.setVisible(newValue);
        });

        this.drop_menu.addListener((obs, old, newValue) ->{
            if (newValue) {
                frame.dispose();
            }
        });

        this.select_enable.addListener((obs, old, newValue) ->{
            if (newValue){
                select_hero_button.setEnabled(true);
            }else{
                select_hero_button.setEnabled(false);
            }

        });

        this.play_enable.addListener((obs, old, newValue) ->{
            if (newValue){
                play_button.setEnabled(true);
            }else{
                play_button.setEnabled(false);
            }
        });
    }
}
