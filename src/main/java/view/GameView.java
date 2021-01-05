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

    //CLOSE FRAME
    private final SimpleBooleanProperty drop_game = new SimpleBooleanProperty(false);
    //list buttons for the map
    private final List<JButton> buttons = new ArrayList<>();

    //the frame
    private JFrame frame;
    private final int frame_size = 1000;
    private JLabel advert;
    private TextField input;
    private JButton run;
    private JButton fight;
    private JPanel input_validate;
    private JPanel board;
    private JPanel panel;

    //current enemy
    private Enemy enemy = null;

    public GameView(Controller controller){
        this.controller = controller;
        this.map_size = controller.getMapSize();
        this.frame = new JFrame("SWING GAME");

        //configure board game
        configureButtons();
        configureBoard();
        //configure messages
        configureLabel();
        configureInput();
        //configure action buttons
        configureRun();
        configureFight();
        configureInputValidate();
        //configure frame and listener
        configureFrame();
        configureBindings();
        configureListener();

        frame.setVisible(true);
    }

    private void configureLabel(){
        advert = new JLabel("Advert will apear here");
    }

    private void configureInput(){
        input = new TextField();
    }
    //button run
    private void configureRun(){
        this.run = new JButton("Run");
        run.setEnabled(false);
        run.addActionListener(e ->{
            if (enemy != null){
                boolean ret = controller.run();
                if (ret){
                    enemy = null;
                    updateButtonActivated();
                }else{
                    //alive.setValue(false);
                }
            }
        });
    }
    //button fight
    private void configureFight(){
        this.fight = new JButton("Fight");
        fight.setEnabled(false);
        fight.addActionListener(e ->{
            if (enemy != null){
                boolean ret = controller.fight();
                if (ret){
                    enemy = null;
                    updateButtonActivated();
                }else{
                    //alive.setValue(false);
                }
            }
        });
    }

    private void configureInputValidate(){
        this.input_validate = new JPanel();
        input_validate.setLayout(new BoxLayout(input_validate, BoxLayout.X_AXIS));
        input_validate.add(run);
        input_validate.add(fight);
    }

    private void configureBoard(){
        this.board = new JPanel(new GridLayout(map_size, map_size));
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(frame_size, frame_size));
        frame.pack();

        frame.add(panel);
        //frame.setLayout(new GridLayout(map_size, map_size));
        //center to the screen
        frame.setLocationRelativeTo(null);
    }
    //the map
    private void configureButtons(){
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
            //alive.setValue(false);
        }else{

        }
    }
    //enables buttons
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
    //enable the board
    private void enableBoard(Boolean b){
        for (int y = 0; y < map_size; ++y){
            for (int x = 0; x < map_size; ++x){
                buttons.get((y * map_size) + x).setEnabled(b);
            }
        }
    }

    private void configureBindings(){
        this.drop_game.bindBidirectional(controller.dropGameProperty());
    }

    private void configureListener(){
        this.drop_game.addListener((obs, old, newValue) ->{
            if (newValue) {
                frame.dispose();
            }
        });
    }
}
