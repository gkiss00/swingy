package model.enemy;

import model.artefact.Artefact;

public class Dragon extends Enemy{
    public Dragon(int lvl, int x, int y){
        this.type = "Dragon";
        this.level = lvl;
        this.attack = lvl * 150;
        this.defense = lvl * 400;
        this.hp = lvl * 300;

        this.x = x;
        this.y = y;
    }

    public Dragon(int lvl, int x, int y, Artefact art){
        this.type = "Dragon";
        this.level = lvl;
        this.attack = lvl * 150;
        this.defense = lvl * 400;
        this.hp = lvl * 300;
        this.artefact = art;

        this.x = x;
        this.y = y;
    }

    public int dropXp(){
        return this.level * 10000;
    }
}
