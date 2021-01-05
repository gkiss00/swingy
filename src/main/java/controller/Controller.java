package controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
                CreateView cv = new CreateView(this);
            }else{
                
            }
        });

        this.select_hero.addListener((obs, old, newValue) ->{
            if (newValue){
                //open the select hero view
                SelectView cv = new SelectView(this);
            }else{

            }
        });

        this.play.addListener((obs, old, newValue) ->{
            if (newValue){
                //open the play view
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
        setMap();
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

    public boolean run(){
        Random rand = new Random();
        int chances = model.getEnemy().getLevel() - model.getCurrentHero().getLevel();
        int result;

        if (chances <= 0)
            chances = 1;
        result = rand.nextInt(chances);
        if (result == 0){
            System.out.println("U run away");
            return (true);
        }else{
            System.out.println("U can't run away");
            return (fight());
        }
    }

    public boolean fight(){
        Enemy enemy = model.getEnemy();
        Random rand = new Random();

        System.out.println("U have to fight");
        while(model.getCurrentHero().getHp() > 0 && enemy.getHp() > 0) {
            //You attack first
            if (rand.nextInt(4) == 0) {
                System.out.println("You missed your attack");
            }else {
                System.out.println("You attack your opponent");
                enemy.takeDamages(model.getCurrentHero().getTotalAttack());
            }
            //Check if enemy is dead
            if(enemy.getHp() <= 0) {
                System.out.println("You killed your enemy");
                model.getCurrentHero().gainXP(enemy.dropXp());
                dropWeapon();
                return (true);
            }

            //Enemy attacks
            if (rand.nextInt(2) == 0) {
                System.out.println("Enemy missed his attack");
            }else {
                System.out.println("Your opponent attacks your");
                model.getCurrentHero().takeDamages(enemy.getAttack());
            }
            //Check if you are dead
            if(model.getCurrentHero().getHp() <= 0) {
                System.out.println("You get killed by your enemy");
                return (false);
            }
        }
        return (true);
    }

    private void dropWeapon(){
        Enemy enemy = model.getEnemy();
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();
        int r;
        if (enemy.getArtefact() != null){
            r = rand.nextInt(5);
            if (r == 0){
                String answer = "";
                Artefact tmp = enemy.dropArtefact();
                System.out.println("Your enemy dropped a " + tmp.toString());
                do {
                    System.out.println("Would you like to equip it ? (YES or NO)");
                    answer = scan.nextLine();
                }while(answer.compareTo("YES") != 0 && answer.compareTo("NO") != 0);
                if (answer.compareTo("YES") == 0){
                    model.equipCurrentHero(tmp);
                    System.out.println("Wow, this new piece of equipment look nice on you");
                }
            }
        }
    }

    public boolean checkEnd(){
        int x = model.getPosX();
        int y = model.getPosY();
        int size = model.getMapSize();
        if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
            System.out.println("You reached the border of the map, you won");
            model.requestUpdateCurrentHero();
            return (true);
        }
        return (false);
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
}