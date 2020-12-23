package view;

import controller.*;
import javafx.beans.property.SimpleBooleanProperty;
import model.enemy.Enemy;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.List;
import java.util.ArrayList;

public class GameView {
    private final Controller controller;
    private int map_size;
    //CLOSE GAME
    private final SimpleBooleanProperty close_game = new SimpleBooleanProperty(false);
    //CLOSE FRAME
    private final SimpleBooleanProperty drop_game = new SimpleBooleanProperty(false);
    //VISIBILITY
    private final SimpleBooleanProperty alive = new SimpleBooleanProperty();
    //UPDATE WHEN THE SELECTED HERO CHANGE
    private final SimpleBooleanProperty hero_changed = new SimpleBooleanProperty();

    private final List<JButton> buttons = new ArrayList<>();

    private JFrame frame;
    private final int frame_size = 1000;

    private JLabel advert;
    private TextField input;
    private JButton run;
    private JButton fight;

    private JPanel input_validate;
    private JPanel board;
    private JPanel panel;

    private Enemy enemy = null;

    public GameView(Controller controller){
        this.controller = controller;

        configureLabel();
        configureInput();
        configureRun();
        configureFight();
        configureInputValidate();

        configureBindings();
        configureListener();
    }

    private void configureRun(){
        run = new JButton("Run");
        run.setEnabled(false);
        run.addActionListener(e ->{
            if (enemy != null){
                boolean ret = controller.run();
                if (ret){
                    enemy = null;
                    updateButtonActivated();
                }else{
                    alive.setValue(false);
                }
            }
        });
    }

    private void configureFight(){
        fight = new JButton("Fight");
        fight.setEnabled(false);
        fight.addActionListener(e ->{
            if (enemy != null){
                boolean ret = controller.fight();
                if (ret){
                    enemy = null;
                    updateButtonActivated();
                }else{
                    alive.setValue(false);
                }
            }
        });
    }

    private void configureInput(){
        input = new TextField();
    }

    private void configureInputValidate(){
        input_validate = new JPanel();
        input_validate.setLayout(new BoxLayout(input_validate, BoxLayout.X_AXIS));
        input_validate.add(run);
        input_validate.add(fight);
    }

    private void configureLabel(){
        advert = new JLabel("Advert will apear here");
    }

    private void configureBoard(){
        map_size = controller.getMapSize();
        board = new JPanel(new GridLayout(map_size, map_size));
        for (int i = 0; i < map_size * map_size; ++i){
            board.add(buttons.get(i));
        }
    }

    private void configurePanel(){
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(advert);
        panel.add(input_validate);
        panel.add(board);
    }

    private void configureFrame(){
        if (frame != null)
            frame.dispose();
        frame = new JFrame("Swing play");
        //To close the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(frame_size, frame_size));
        frame.pack();


        frame.add(panel);
        //frame.setLayout(new GridLayout(map_size, map_size));
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

    private void configureButtons(){
        buttons.clear();
        for (int y = 0; y < map_size; ++y){
            for (int x = 0; x < map_size; ++x){
                JButton b = new JButton();
                b.setBackground(Color.WHITE);
                if (y == map_size / 2 && x == map_size / 2)
                    b.setBackground(Color.GREEN);
                String cmd = "";
                cmd = Integer.toString(x);
                cmd += " ";
                cmd += Integer.toString(y);
                b.setActionCommand(cmd);
                buttons.add(b);
            }
        }

        for (int i = 0; i < buttons.size(); ++i){
            buttons.get(i).addActionListener(e ->{
                String args[] = e.getActionCommand().split("\\s+");
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int ret = controller.goTo(e.getActionCommand());
                if (ret == 1) {
                    buttons.get((y * map_size) + x).setBackground(Color.GREEN);
                    enemy = null;
                    advert.setText("You are safe here");
                }else if (ret == 2){
                    buttons.get((y * map_size) + x).setBackground(Color.GREEN);
                    enemy = controller.getEnemy();
                    advert.setText("Enemy is here what u gonna do ?");
                }
                checkEnd();
                updateButtonActivated();
            });
        }
    }

    private void checkEnd(){
        boolean ret = controller.checkEnd();
        if (ret){
            alive.setValue(false);
        }else{

        }
    }

    private void updateButtonActivated(){
        if (enemy == null){
            run.setEnabled(false);
            fight.setEnabled(false);
            enableBoard(true);
        }else{
            run.setEnabled(true);
            fight.setEnabled(true);
            enableBoard(false);
        }
    }

    private void enableBoard(Boolean b){
        for (int y = 0; y < map_size; ++y){
            for (int x = 0; x < map_size; ++x){
                buttons.get((y * map_size) + x).setEnabled(b);
            }
        }
    }

    private void configureBindings(){
        //VISIBILITY
        this.alive.bindBidirectional(controller.playViewAliveProperty());
        //HERA HAS BEEN SELECTED
        this.hero_changed.bindBidirectional(controller.newHeroSelectedProperty());
        //DELETE VIEW
        this.drop_game.bindBidirectional(controller.dropGameViewProperty());
        //DELETE ALL VIEW
        this.close_game.bindBidirectional(controller.closeGameProperty());
    }

    private void configureListener(){
        this.alive.addListener((obs, old, newValue) -> {
            frame.setVisible(newValue);
        });

        this.hero_changed.addListener((obs, old, newValue) -> {
            map_size = controller.getMapSize();
            configureLabel();
            configureButtons();
            configureBoard();
            configurePanel();
            configureFrame();
        });

        this.drop_game.addListener((obs, old, newValue) ->{
            if (newValue) {
                if (frame != null)
                    frame.dispose();
            }
        });
    }
}