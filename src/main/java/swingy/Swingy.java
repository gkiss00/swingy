package swingy;

import exception.*;
import exception.BoundException;
import model.*;
import controller.*;
import model.artefact.Artefact;
import model.enemy.Enemy;
import view.*;

import java.util.Scanner;
import java.util.Random;

public class Swingy {

    //CONDITION TO END THE GAME
    private static boolean check_end(Model model){
        int x = model.getPosX();
        int y = model.getPosY();
        int size = model.getMapSize();
        if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
            System.out.println("You reached the border of the map, you won");
            return (true);
        }
        return (false);
    }

    private static void dropWeapon(Model model, Enemy enemy){
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

    //FIGHT IF U FALL ON AN ENEMY
    private static boolean fight(Model model, Enemy enemy){
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
                dropWeapon(model, enemy);
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
    //RUN
    private static boolean run(Model model, Enemy enemy){
        Random rand = new Random();
        int chances = enemy.getLevel() - model.getCurrentHero().getLevel();
        int result;

        if (chances <= 0)
            chances = 1;
        result = rand.nextInt(chances);
        if (result == 0){
            System.out.println("U run away");
            return (true);
        }else{
            System.out.println("U can't run away");
            return (fight(model, enemy));
        }
    }
    //IF U FALL ON AN ENEMY
    private static boolean enemyIsHere(Model model){
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();
        Enemy enemy = model.getEnemy();
        String cmd;

        if (enemy == null)
            return (true);

        System.out.println("U felt on a " + enemy.toString());
        do {
            System.out.println("What do u want to do ? (RUN or FIGHT)");
            cmd = scan.nextLine();
        }while(cmd.compareTo("RUN") != 0 && cmd.compareTo("FIGHT") != 0);
        if (cmd.compareTo("RUN") == 0){
            return (run(model, enemy));
        }else{
            return fight(model, enemy);
        }
    }

    private static boolean isValidDirection(String direction){
        if (direction.compareTo("NORTH") == 0)
            return (true);
        if (direction.compareTo("EAST") == 0)
            return (true);
        if (direction.compareTo("SOUTH") == 0)
            return (true);
        if (direction.compareTo("WEST") == 0)
            return (true);
        return (false);
    }

    //CHOSE A DIRECTION
    private static void play(Model model){
        Scanner scan = new Scanner(System.in);
        String direction;
        Boolean enemyFound;
        int end = 1;
        model.setMap();
        model.getCurrentHero().restaureHp();
        //WHILE U ARE ALIVE AND IN THE MAP
        while(end == 1){
            model.printMap();
            //TAKE A DIRECTION
            do {
                System.out.println("Enter a direction : \"NORTH\" \"EAST\" \"SOUTH\" \"WEST\"");
                direction = scan.nextLine();
            }while(isValidDirection(direction) == false);
            //CHECK IF THERE IS AN ENEMY
            enemyFound = model.updateMap(direction);
            if (enemyFound) {
                //IF U DIE
                if (enemyIsHere(model) == false){
                    end = 0;
                    break;
                }
            }
            //IF U REACH THE BORDER
            if (check_end(model)) {
                model.printMap();
                end = 0;
                model.requestUpdateCurrentHero();
            }
        }
    }

    //SELECT YOUR HERO
    private static void selectAnHero(Model model){
        Scanner scan = new Scanner(System.in);
        System.out.println("Here is all your heroes : ");
        String name;

        //PRINT ALL HEROES
        for (int i = 0; i < model.getNbHeroes(); ++i){
            try {
                System.out.println(model.getHeroAt(i));
                System.out.println("**********************");
            } catch (BoundException e) {
                e.printStackTrace();
            }
        }
        //SELECT ONE
        do {
            System.out.println("To select an hero, type his name");
            name = scan.nextLine();
        }while(model.heroExist(name) == false);
        model.setCurrentHero(name);
    }

    //LAUNCH THE GAME
    public static void launchConsoleGame(Model _model) throws Exception {
        Scanner scan = new Scanner(System.in);
        Model model = _model;
        String command = "";
        while(true) {
            //IF U DON'T GET ANY HERO CREATE ONE
            if (model.getNbHeroes() == 0) {
                System.out.println("Tou have no hero, to start the game u must create a new one");
                model.addNewHeroConsole();
            } else {
                //SELECT CRATE OR PLAY
                do {
                    System.out.println("To select an hero, type SELECT. " +
                                        "To create a new hero, type CREATE. " +
                                        "To play the game, type PLAY. " +
                                        "To switch for gui mode, type GUI. " +
                                        "To exit, type EXIT");
                    command = scan.nextLine();
                } while (command.compareTo("CREATE") != 0 && command.compareTo("SELECT") != 0 && command.compareTo("PLAY") != 0 && command.compareTo("GUI") != 0 && command.compareTo("EXIT") != 0);
                if (command.compareTo("EXIT") == 0 || command.compareTo("GUI") == 0)
                    break;
                else if (command.compareTo("CREATE") == 0)
                    model.addNewHeroConsole();
                else if (command.compareTo("SELECT") == 0) {
                    selectAnHero(model);
                } else {
                    if (model.getCurrentHero() == null) {
                        System.out.println("You have to select an hero to play the game");
                        selectAnHero(model);
                    }
                    play(model);
                }
            }
        }
        if (command.compareTo("GUI") == 0)
            launchGuiGame(model);
        System.out.println("See you next time");
    }

    public static void launchGuiGame(Model model) throws Exception {
        Controller controller = new Controller(model);
        MenuView view = new MenuView(controller);
    }
    //ENTRY POINT
    public static void main(String[] args){
        try{
            if (args.length != 1)
                throw (new BadArgsException("Bad number of args"));
            if (args[0].compareTo("console") != 0 && args[0].compareTo("gui") != 0)
                throw (new BadArgsException("Bad argument, it should be \"console\" or \"gui\""));
            Model model = new Model();
            //CONSOLE GAME
            if (args[0].compareTo("console") == 0)
                launchConsoleGame(model);
            //GUI GAME
            else
                launchGuiGame(model);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
