package controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.*;
import model.artefact.Artefact;
import model.enemy.Enemy;
import model.hero.Hero;
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

    public Controller(Model model){
        this.model = model;

        if (model.getNbHeroes() == 0)
            select_enable.setValue(false);
        if (model.getCurrentHero() == null)
            play_enable.setValue(false);

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
        return play_enable;
    }

    public SimpleBooleanProperty selectEnableProperty(){
        return select_enable;
    }

    
    //*******************************************************************************************
    //*******************************************************************************************
    //LISTENERS
    //*******************************************************************************************
    //*******************************************************************************************
    private void configureListeners(){

        close_game.addListener((obs, old, newValue) ->{
            if(newValue){
                //CLOSE ALL FRAME
                menu_view_drop.setValue(true);
                create_view_drop.setValue(true);
                select_view_drop.setValue(true);
                play_view_drop.setValue(true);
            }
        });
        create_view_alive.addListener((obs, odl, newValue) ->{
            //IF CREATE VIEW CLOSE OPEN MENU
            if (!newValue){
                menu_view_alive.setValue(true);
            }
        });

        select_view_alive.addListener((obs, odl, newValue) ->{
            //IF SELECT VIEW CLOSE OPEN MENU
            if (!newValue){
                menu_view_alive.setValue(true);
            }
        });

        play_view_alive.addListener((obs, odl, newValue) ->{
            //IF PLAY VIEW CLOSE OPEN MENU
            if (!newValue){
                menu_view_alive.setValue(true);
            }
        });

        play_console.addListener((obs, odl, newValue) ->{
            if (newValue){
                //CLOSE ALL FRAME
                menu_view_drop.setValue(true);
                create_view_drop.setValue(true);
                select_view_drop.setValue(true);
                play_view_drop.setValue(true);
                //LAUNCH CONSOLE MODE
                try {
                    Swingy.launchConsoleGame(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //*******************************************************************************************
    //*******************************************************************************************
    //VALIDATE NEW HERO
    //*******************************************************************************************
    //*******************************************************************************************
    public boolean validateName(String name){
        if (model.heroExist(name)){
            errors_create_view.setValue(1);
            return (false);
        }
        if (isNullOrEmpty(name)) {
            errors_create_view.setValue(1);
            return (false);
        }
        errors_create_view.setValue(0);
        return (true);
    }

    public boolean validateClass(String name){
        if (isNullOrEmpty(name)) {
            errors_create_view.setValue(2);
            return (false);
        }
        return (true);
    }

    public void addNewHero(String name, String _class){
        model.addNewHeroGui(name, _class);
        new_hero_added.setValue(true);
        new_hero_added.setValue(false);
        select_enable.setValue(true);
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
        play_enable.setValue(true);
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