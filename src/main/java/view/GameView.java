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
    //END GAME
    private final SimpleBooleanProperty end_game = new SimpleBooleanProperty(false);
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

    //messages
    private final String message_safe = "You are safe here";
    private final String message_enemy = "You felt on an enemy what u gonna do ?";

    public GameView(Controller controller){
        this.controller = controller;
        this.map_size = controller.getMapSize();
        this.frame = new JFrame("SWING GAME");

        
        //configure messages
        configureLabel();
        configureInput();
        //configure action buttons
        configureRun();
        configureFight();
        configureInputValidate();
        //configure board game
        configureButtons();
        configureBoard();
        //configure frame and listener
        configurePanel();
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
                    enableDirections();
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
                    enableDirections();
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
                b.setActionCommand(getCommande(x, y));
                b.setBackground(getColor(x, y));
                b.setOpaque(true);
                buttons.add(b);
            }
        }

        for (int i = 0; i < buttons.size(); ++i){
            buttons.get(i).addActionListener(e ->{
                String args[] = e.getActionCommand().split("\\s+");
                int x = getInt(args[0]);
                int y = getInt(args[1]);
                int ret = controller.goTo(e.getActionCommand());
                buttons.get((y * map_size) + x).setBackground(Color.GREEN);
                if (ret == 1) { //place if safe 
                    enemy = null;
                    advert.setText(message_safe);
                    enableDirections();
                }else if (ret == 2){ //there is an enemy here
                    enemy = controller.getEnemy();
                    advert.setText(message_enemy);
                    updateButtonActivated();
                }
                checkEnd();
                
            });
        }
        enableDirections();
    }

    private void checkEnd(){
        controller.checkEnd();
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
        this.end_game.bindBidirectional(controller.endGameProperty());
    }

    private void configureListener(){
        this.drop_game.addListener((obs, old, newValue) ->{
            if (newValue) {
                frame.dispose();
            }
        });
        this.end_game.addListener((obs, old, newValue) ->{
            if (newValue) {
                frame.dispose();
            }
        });
    }

    //*******************************************************************************************
    //*******************************************************************************************
    //UTILS
    //*******************************************************************************************
    //*******************************************************************************************

    private int getInt(String str){
        return Integer.parseInt(str);
    }

    //get button command
    private String getCommande(int x, int y){
        String cmd = "";
        cmd = Integer.toString(x);
        cmd += " ";
        cmd += Integer.toString(y);
        return cmd;
    }

    //get button color
    private Color getColor(int x, int y){
        if (y == (map_size / 2) && x == (map_size / 2))
            return Color.GREEN;
        return Color.WHITE;
    }

    //enable possibilities
    private void enableDirections(){
        int current_x = controller.getModel().getPosX();
        int current_y = controller.getModel().getPosY();
        for (int y = 0; y < map_size; ++y){
            for (int x = 0; x < map_size; ++x){
                if(x == current_x && y == current_y + 1){
                    buttons.get((y * map_size) + x).setEnabled(true);
                }else if(x == current_x && y == current_y - 1){
                    buttons.get((y * map_size) + x).setEnabled(true);
                }else if(x == current_x + 1 && y == current_y){
                    buttons.get((y * map_size) + x).setEnabled(true);
                }else if(x == current_x - 1 && y == current_y){
                    buttons.get((y * map_size) + x).setEnabled(true);
                }else{
                    buttons.get((y * map_size) + x).setEnabled(false);
                }
            }
        }
    }
}
