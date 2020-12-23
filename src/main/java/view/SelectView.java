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

    private List<Hero> heroes;

    private final List<JButton> buttons = new ArrayList<>();
    private int actual;

    private final SimpleBooleanProperty close_game = new SimpleBooleanProperty(false);
    //CLOSE
    private final SimpleBooleanProperty drop_select = new SimpleBooleanProperty();
    //VISIBILITY
    private final SimpleBooleanProperty alive = new SimpleBooleanProperty();
    //SET CURRENT HERO
    private final SimpleBooleanProperty new_hero_selected = new SimpleBooleanProperty();
    //UPDATE VIEW IF A NEW HERO IS ADDED
    private final SimpleBooleanProperty new_hero_added = new SimpleBooleanProperty();

    private JFrame frame;
    private final int frame_size = 1000;

    public SelectView(Controller controller){
        this.controller = controller;
        heroes = controller.getAllHeroes();


        configuresButtons();
        configureFrame();
        configureBindings();
        configureListener();

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
            actual = i;
            buttons.get(i).addActionListener(e -> {
                controller.selectHero(e.getActionCommand());
                new_hero_selected.setValue(!new_hero_selected.getValue());
                alive.setValue(false);
            });
        }
    }

    private void configureBindings(){
        //VISIBILITY
        this.alive.bindBidirectional(controller.selectViewAliveProperty());
        //NEW HERO SELECTED
        this.new_hero_selected.bindBidirectional(controller.newHeroSelectedProperty());
        //NEW HERA ADDED
        this.new_hero_added.bindBidirectional(controller.newHeroAddedProperty());
        //DELETE VIEW
        this.drop_select.bindBidirectional(controller.dropSelectViewProperty());
        //DELETE ALL VIEW
        this.close_game.bindBidirectional(controller.closeGameProperty());
    }

    private void configureListener(){
        //UPDATE VISIBILITY
        this.alive.addListener((obs, old, newValue) -> {
            frame.setVisible(newValue);
        });
        //UPDATE VIEW
        this.new_hero_added.addListener((obs, old, newValue) -> {
            if(newValue){
                heroes = controller.getAllHeroes();
                configuresButtons();
                configureFrame();
            }
        });
        //CLOSE
        this.drop_select.addListener((obs, old, newValue) ->{
            if (newValue)
                frame.dispose();
        });
    }
}
