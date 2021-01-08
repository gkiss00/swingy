package model;

import java.util.Scanner;

import exception.BoundException;
import model.artefact.Armor;
import model.artefact.Artefact;
import model.artefact.Helm;
import model.artefact.Weapon;
import model.database.Database;
import model.enemy.Enemy;
import model.hero.Hero;
import model.hero.HeroesArtefacts;

import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class Model {
    private final Database db;
    private final List<Hero> heroes = new ArrayList<Hero>();
    private final List<Artefact> artefacts = new ArrayList<Artefact>();
    private final List<HeroesArtefacts> heroes_artefacts = new ArrayList<HeroesArtefacts>();
    private Hero current_hero = null;
    private Map map = null;

    public Model() throws Exception {
        db = new Database();
        requestGetAllHeroes();
        requestGetAllArtefacts();
        requestGetAllHeroesArtefacts();
        equipAllHeroes();
    }
    //******************************************************************************************************
    //******************************************************************************************************
    //MAP
    //******************************************************************************************************
    //******************************************************************************************************
    public void setMap(){

        map = new Map(current_hero.getLevel());
        map.fillWithEnemies(current_hero.getLevel(), artefacts);
    }

    public int updateMap(int x, int y){
        return map.update(x, y);
    }

    public int getMapSize(){
        return (map.getSize());
    }

    public void printMap(){
        System.out.println(map.toString());
    }

    public boolean updateMap(String direction){
        return (map.update(direction));
    }

    public int getPosX(){
        return (map.getPosX());
    }

    public int getPosY(){
        return (map.getPosY());
    }

    public Enemy getEnemy(){
        return map.getEnemy();
    }
    //******************************************************************************************************
    //******************************************************************************************************
    //HEROES
    //******************************************************************************************************
    //******************************************************************************************************

    public List<Hero> getAllHeroes(){
        return heroes;
    }

    public int getNbHeroes(){
        return (heroes.size());
    }

    public Hero getHeroAt(int i) throws BoundException {
        if (i < 0 || i >= heroes.size())
            throw (new BoundException("Index out of bound"));
        else
            return (heroes.get(i));
    }

    public Hero getCurrentHero(){
        return (current_hero);
    }

    public void setCurrentHero(String name){
        for (int i = 0;i < heroes.size(); ++i) {
            if (name.compareTo(heroes.get(i).getName()) == 0)
                current_hero = heroes.get(i);
        }
    }

    public boolean heroExist(String name){
        for (int i = 0;i < heroes.size(); ++i) {
            if (name.compareTo(heroes.get(i).getName()) == 0)
                return (true);
        }
        return (false);
    }

    private void requestGetAllHeroes() throws Exception {
        heroes.clear();
        ResultSet res = db.getAllHeroes();
        while(res.next()){
            String name = res.getString("H_Name");
            String clas = res.getString("H_Class");
            int lvl = res.getInt("H_Level");
            int ex = res.getInt("H_Experience");
            int attack = res.getInt("H_Attack");
            int defense = res.getInt("H_Defense");
            int hitpoint = res.getInt("H_HitPoints");
            heroes.add(new Hero(name, clas, lvl, ex, attack, defense, hitpoint));
        }
    }

    public void addNewHeroGui(String name, String _class){
        Hero h = new Hero(name, _class, 1, 0, 30, 10, 100);
        requestAddHero(h);
    }

    public void addNewHeroConsole(){
        Scanner scan = new Scanner(System.in);
        String _class;
        String name;
        do {
            System.out.println("Your hero name : ");
            name = scan.nextLine();
        }while(nameAlreadyTaken(name) || isNullOrEmpty(name) || !isGoodChar(name));
        do {
            System.out.println("Your hero class : ");
            _class = scan.nextLine();
        }while(isNullOrEmpty(_class) || !isGoodChar(_class));
        Hero h = new Hero(name, _class, 1, 0, 30, 10, 100);
        requestAddHero(h);
        heroes.add(h);
    }

    private void requestAddHero(Hero h){
        try {
            db.addHero(h);
            requestGetAllHeroes();
        }catch (Exception e) {
            System.out.println("Error while inserting in database");
        }
    }

    public void requestUpdateCurrentHero(){
        try {
            db.updateHero(current_hero);
            requestGetAllHeroes();
            equipAllHeroes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //******************************************************************************************************
    //******************************************************************************************************
    //ARTEFACTS
    //******************************************************************************************************
    //******************************************************************************************************
    private void requestGetAllArtefacts() throws Exception {
        artefacts.clear();
        ResultSet res = db.getAllArtefacts();
        while(res.next()){
            String name = res.getString("A_Name");
            String type = res.getString("A_Type");
            int amount = res.getInt("A_Amount");
            if (type.compareTo("Weapon") == 0)
                artefacts.add(new Weapon(name, amount));
            if (type.compareTo("Armor") == 0)
                artefacts.add(new Armor(name, amount));
            if (type.compareTo("Helm") == 0)
                artefacts.add(new Helm(name, amount));
        }
    }

    //******************************************************************************************************
    //******************************************************************************************************
    //HEROES ARTEFACTS
    //******************************************************************************************************
    //******************************************************************************************************
    private void requestGetAllHeroesArtefacts() throws Exception {
        heroes_artefacts.clear();
        ResultSet res = db.getAllHeroesArtefacts();
        while(res.next()){
            String h_name = res.getString("H_Name");
            String a_name = res.getString("A_Name");
            heroes_artefacts.add(new HeroesArtefacts(h_name, a_name));
        }
    }

    private void equipAllHeroes(){
        Artefact a_tmp = null;
        Hero h_tmp = null;
        for (int i =0; i < heroes_artefacts.size(); ++i){
            a_tmp = null;
            h_tmp = null;
            for (int k = 0; k < heroes.size(); ++k){
                if (heroes.get(k).getName().compareTo(heroes_artefacts.get(i).getHName()) == 0)
                    h_tmp = heroes.get(k);
            }
            for (int k = 0; k < artefacts.size(); ++k){
                if (artefacts.get(k).getName().compareTo(heroes_artefacts.get(i).getAName()) == 0)
                    a_tmp = artefacts.get(k);
            }
            if(h_tmp != null && a_tmp!= null)
                equipHero(h_tmp, a_tmp);
        }
    }

    public void equipHero(Hero h_tmp, Artefact a_tmp){
        if(a_tmp.getClass() == Weapon.class)
            h_tmp.equipWeapon((Weapon)a_tmp);
        if(a_tmp.getClass() == Armor.class)
            h_tmp.equipArmor((Armor)a_tmp);
        if(a_tmp.getClass() == Helm.class)
            h_tmp.equipHelm((Helm)a_tmp);
    }

    public void equipCurrentHero(Artefact art){
        Hero tmp = current_hero;
        try {
            db.addHeroesArtefact(tmp, art);
            for (int i = 0; i < heroes_artefacts.size(); ++i){
                if (tmp.getName().compareTo(heroes_artefacts.get(i).getHName()) == 0 && tmp.getName().compareTo(heroes_artefacts.get(i).getAName()) == 0){
                    heroes_artefacts.remove(heroes_artefacts.get(i));
                    break;
                }
            }
            heroes_artefacts.add(new HeroesArtefacts(tmp.getName(), art.getName()));
            equipHero(tmp, art);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
    //***************************************************************************************************
    //***************************************************************************************************
    //UTILS
    //***************************************************************************************************
    //***************************************************************************************************
    private boolean nameAlreadyTaken(String name){
        for (int i = 0; i < heroes.size(); ++i){
            if (name.compareTo(heroes.get(i).getName()) == 0) {
                System.out.println("This name is already taken");
                return (true);
            }
        }
        return (false);
    }

    private boolean isNullOrEmpty(String name){
        if (name.length() == 0){
            System.out.println("Your input can't be empty");
            return (true);
        }
        for (int i = 0; i < name.length(); ++i){
            if (name.charAt(i) != ' ')
                return (false);
        }
        System.out.println("Your input can't be empty");
        return (true);
    }

    private boolean isGoodChar(String str){
        for (int i = 0; i < str.length(); ++i){
            if (str.charAt(i) == '"' || str.charAt(i) == '\'' || str.charAt(i) == '\\'){
                System.out.println("Your input can not contains specail caractere");
                return false;
            }
        }
        return true;
    }
}