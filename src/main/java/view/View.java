package view;

import controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class View{
    private final Controller controller;

    private final MenuView menu_view ;
    private final SelectView select_view;
    private final CreateView create_view;
    private final GameView game_view;

    public View(Controller controller){
        this.controller = controller;
        menu_view = new MenuView(controller);
        select_view = new SelectView(controller);
        create_view = new CreateView(controller);
        game_view = new GameView(controller);
    }

}