package model.enemy;

import model.artefact.Artefact;

public class Goblin extends Enemy{
    public Goblin(int lvl, int x, int y){
        this.type = "Goblin";
        this.level = lvl;
        this.attack = lvl * 5;
        this.defense = lvl * 3;
        this.hp = lvl * 10;

        this.x = x;
        this.y = y;
    }

    public Goblin(int lvl, int x, int y, Artefact art){
        this.type = "Goblin";
        this.level = lvl;
        this.attack = lvl * 5;
        this.defense = lvl * 3;
        this.hp = lvl * 10;
        this.artefact = art;

        this.x = x;
        this.y = y;
    }

    public int dropXp(){
        return this.level * 500;
    }
}
