package controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.*;
import model.artefact.Artefact;
import model.enemy.Enemy;
import model.hero.Hero;
import view.*;
import swingy.Swingy;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Controller {
    private final Model model;

    //DROP FRAME --(MENU)--
    private final SimpleBooleanProperty drop_menu = new SimpleBooleanProperty(false);
    //GO CONSOLE MODE --(MENU)--
    private final SimpleBooleanProperty console_mode = new SimpleBooleanProperty(false);
    //CREATE NEW HERO --(MENU)--
    private final SimpleBooleanProperty new_hero = new SimpleBooleanProperty(false);
    //SELECT HERO --(MENU)--
    private final SimpleBooleanProperty select_hero = new SimpleBooleanProperty(false);
    //PLAY --(MENU)--
    private final SimpleBooleanProperty play = new SimpleBooleanProperty(false);
    //SELECT ENABLE --(MENU)--
    private final SimpleBooleanProperty enable_select = new SimpleBooleanProperty(false);
    //PLAY ENABLE --(MENU)--
    private final SimpleBooleanProperty enable_play = new SimpleBooleanProperty(false);

    //DROP FRAME --(CREATE)--
    private final SimpleBooleanProperty drop_create = new SimpleBooleanProperty(false);
    //INPUT ERRORS --(CEATE)--
    private final SimpleIntegerProperty input_errors = new SimpleIntegerProperty(0);

    //DROP FRAME --(SELECT)--
    private final SimpleBooleanProperty drop_select = new SimpleBooleanProperty();

    //DROP FRAME --(GAME)--
    private final SimpleBooleanProperty drop_game = new SimpleBooleanProperty();
    //END GAME --(GAME)--
    private final SimpleBooleanProperty end_game = new SimpleBooleanProperty(false);
    //MESSAGE --(GAME)--
    private final SimpleStringProperty message = new SimpleStringProperty("");
    //ARTEFACT DROPPED --(GAME)--
    private final SimpleBooleanProperty artefact_dropped = new SimpleBooleanProperty(false);

    private Artefact dropped_artefact = null;

    //*******************************************************************************************
    //*******************************************************************************************
    //COMSTRUCTOR
    //*******************************************************************************************
    //*******************************************************************************************
    public Controller(Model model){
        this.model = model;

        if (model.getNbHeroes() == 0)
            enable_select.setValue(false);
        if (model.getCurrentHero() == null)
            enable_play.setValue(false);

        configureListeners();
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //GETTER
    //*******************************************************************************************
    //*******************************************************************************************

    public Model getModel(){
        return model;
    }

    public int getMapSize(){
        return model.getMapSize();
    }

    public int getPosX(){
        return model.getPosX();
    }

    public int getPosY(){
        return model.getPosY();
    }

    public List<Hero> getAllHeroes(){
        return model.getAllHeroes();
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //PROPERTIES
    //*******************************************************************************************
    //*******************************************************************************************

    //MENU
    public SimpleBooleanProperty dropMenuProperty(){
        return drop_menu;
    }

    public SimpleBooleanProperty consoleModeProperty(){
        return console_mode;
    }

    public SimpleBooleanProperty newHeroProperty(){
        return new_hero;
    }

    public SimpleBooleanProperty selectHeroProperty(){
        return select_hero;
    }

    public SimpleBooleanProperty playProperty(){
        return play;
    }
    public SimpleBooleanProperty playEnableProperty(){
        return enable_play;
    }

    public SimpleBooleanProperty selectEnableProperty(){
        return enable_select;
    }

    //CREATE
    public SimpleBooleanProperty dropCreateProperty(){
        return drop_create;
    }

    public SimpleIntegerProperty inputErrorsProperty(){
        return input_errors;
    }

    //SELECT
    public SimpleBooleanProperty dropSelectProperty(){
        return drop_create;
    }

    //GAME
    public SimpleBooleanProperty dropGameProperty(){
        return drop_game;
    }
    public SimpleBooleanProperty endGameProperty(){
        return end_game;
    }
    public SimpleStringProperty messageProperty(){
        return message;
    }
    public SimpleBooleanProperty artefactDropped(){
        return artefact_dropped;
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //LISTENERS
    //*******************************************************************************************
    //*******************************************************************************************
    
    private void configureListeners(){
        //MENU
        this.console_mode.addListener((obs, old, newValue) ->{
            if (newValue){
                //go in console mode
                try{
                    Swingy.launchConsoleGame(model);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                
            }
        });

        this.new_hero.addListener((obs, old, newValue) ->{
            if (newValue){
                //open the new hero view
                resetProperty(new_hero);
                CreateView cv = new CreateView(this);
            }else{
                
            }
        });

        this.select_hero.addListener((obs, old, newValue) ->{
            if (newValue){
                //open the select hero view
                resetProperty(select_hero);
                SelectView cv = new SelectView(this);
            }else{

            }
        });

        this.play.addListener((obs, old, newValue) ->{
            if (newValue){
                //open the play view
                setMap();
                resetProperty(play);
                model.getCurrentHero().restaureHp();
                GameView cv = new GameView(this);
            }else{
                
            }
        });
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //CREATE NEW HERO
    //*******************************************************************************************
    //*******************************************************************************************
    private boolean validateInputs(String name, String _class){
        if(validateName(name)){ //if bad name
            input_errors.setValue(1);
            return false;
        }
        if (validateClass(_class)){ //if bad class
            input_errors.setValue(2);
            return false;
        }
        if (!isGoodChar(name) || !isGoodChar(_class)){
            input_errors.setValue(3);
            return false;
        }
        return true;
    }
    private boolean validateName(String name){
        return (model.heroExist(name) || isNullOrEmpty(name));
    }

    private boolean validateClass(String _class){
        return (isNullOrEmpty(_class));
    }

    public void addNewHero(String name, String _class){
        if (!validateInputs(name, _class)){
            return ;
        }else{
            model.addNewHeroGui(name, _class);
            enable_select.setValue(true);
            input_errors.setValue(0);
            drop_create.setValue(true);
            MenuView mv = new MenuView(this);
        }
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //SELECT NEW HERO
    //*******************************************************************************************
    //*******************************************************************************************
    public void selectHero(String text){
        String[] args = text.split("\\s+");
        model.setCurrentHero(args[0]);
        enable_play.setValue(true);
        MenuView mv = new MenuView(this);
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //MAP
    //*******************************************************************************************
    //*******************************************************************************************
    public void setMap(){
        model.setMap();
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //GAME
    //*******************************************************************************************
    //*******************************************************************************************
    public int goTo(String cmd){
        String args[] = cmd.split("\\s+");
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        return model.updateMap(x, y);
    }

    public Enemy getEnemy(){
        return model.getEnemy();
    }
    //Run from the fight
    public boolean run(){
        Random rand = new Random();
        int chances = model.getEnemy().getLevel() - model.getCurrentHero().getLevel();
        int result;

        if (chances <= 0)
            chances = 1;
        result = rand.nextInt(chances);
        if (result == 0){
            return (true);
        }else{
            return (fight());
        }
    }
    //fight
    public boolean fight(){
        Enemy enemy = model.getEnemy();
        Random rand = new Random();

        while(model.getCurrentHero().getHp() > 0 && enemy.getHp() > 0) {
            //You attack first
            if (rand.nextInt(4) != 0)
                enemy.takeDamages(model.getCurrentHero().getTotalAttack());
            //Check if enemy is dead
            if(enemy.getHp() <= 0) {
                model.getCurrentHero().gainXPGUI(enemy.dropXp());
                dropWeapon();
                return (true);
            }
            //Enemy attacks
            if (rand.nextInt(2) == 0)
                model.getCurrentHero().takeDamages(enemy.getAttack());
            //Check if you are dead
            if(model.getCurrentHero().getHp() <= 0) {
                end_game.setValue(true);
                resetProperty(end_game);
                MenuView cv = new MenuView(this);
                return (false);
            }
        }
        //this point is never reached
        return (true);
    }
    //equip weapon if one is dropped
    private void dropWeapon(){
        Enemy enemy = model.getEnemy();
        Random rand = new Random();
        int r;

        if (enemy.getArtefact() != null){
            r = rand.nextInt(5); //rise to minimise the chances
            if (r == 0){
                dropped_artefact = enemy.dropArtefact();
                this.artefact_dropped.setValue(true);
            }
        }
    }

    public void equip(){
        if (dropped_artefact != null){
            model.equipCurrentHero(dropped_artefact);
            notEquip();
        }
    }

    public void notEquip(){
            dropped_artefact = null;
            artefact_dropped.setValue(false);
    }

    public void checkEnd(){
        int x = model.getPosX();
        int y = model.getPosY();
        int size = model.getMapSize();
        if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
            model.requestUpdateCurrentHero();
            end_game.setValue(true);
            resetProperty(end_game);
            MenuView mv = new MenuView(this);
        }
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //UTILS
    //*******************************************************************************************
    //*******************************************************************************************
    private boolean isNullOrEmpty(String name){
        if (name.length() == 0){
            return (true);
        }
        for (int i = 0; i < name.length(); ++i){
            if (name.charAt(i) != ' ')
                return (false);
        }
        return (true);
    }

    private void resetProperty(SimpleBooleanProperty sbp){
        sbp.setValue(false);
    }

    private boolean isGoodChar(String str){
        for (int i = 0; i < str.length(); ++i){
            if (str.charAt(i) == '"' || str.charAt(i) == '\'' || str.charAt(i) == '\\')
                return false;
        }
        return true;
    }
}