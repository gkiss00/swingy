package model.enemy;

import model.artefact.Artefact;

public class Orc extends Enemy {
    public Orc(int lvl, int x, int y){
        this.type = "Orc";
        this.level = lvl;
        this.attack = lvl * 15;
        this.defense = lvl * 5;
        this.hp = lvl * 15;

        this.x = x;
        this.y = y;
    }

    public Orc(int lvl, int x, int y, Artefact art){
        this.type = "Orc";
        this.level = lvl;
        this.attack = lvl * 15;
        this.defense = lvl * 5;
        this.hp = lvl * 15;
        this.artefact = art;

        this.x = x;
        this.y = y;
    }

    public int dropXp(){
        return this.level * 750;
    }
}
