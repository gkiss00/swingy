package model.enemy;

import model.artefact.Artefact;

public class Troll extends Enemy{
    public Troll(int lvl, int x, int y){
        this.type = "Troll";
        this.level = lvl;
        this.attack = lvl * 20;
        this.defense = lvl * 25;
        this.hp = lvl * 100;

        this.x = x;
        this.y = y;
    }

    public Troll(int lvl, int x, int y, Artefact art){
        this.type = "Troll";
        this.level = lvl;
        this.attack = lvl * 20;
        this.defense = lvl * 25;
        this.hp = lvl * 100;
        this.artefact = art;

        this.x = x;
        this.y = y;
    }

    public int dropXp(){
        return this.level * 1500;
    }
}
