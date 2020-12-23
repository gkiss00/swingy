package model.enemy;

import model.artefact.Artefact;

public abstract class Enemy {
    protected String type;
    protected int level;
    protected int attack;
    protected int defense;
    protected int hp;
    protected int x;
    protected int y;

    protected Artefact artefact = null;

    public String getType(){
        return (type);
    }

    public int getLevel(){
        return level;
    }

    public int getAttack(){
        return attack;
    }

    public int getDefense(){
        return defense;
    }

    public int getHp(){
        return hp;
    }

    public int getX(){
        return (x);
    }

    public int getY(){
        return (y);
    }

    public void takeDamages(int damages){
        int damages_taken = damages - defense;
        if (damages_taken <= 0)
            damages_taken = 1;
        hp -= damages_taken;
    }

    public abstract int dropXp();

    public Artefact dropArtefact(){
        return artefact;
    }

    public Artefact getArtefact(){
        return artefact;
    }

    @Override
    public String toString(){
        String res = "";
        res += this.type;
        res += " lvl ";
        res += Integer.toString(level);
        res += " an attack of ";
        res += Integer.toString(attack);
        res += " a defense of ";
        res += Integer.toString(defense);
        res += " an ";
        res += Integer.toString(hp);
        res += " hp";
        res += " in ";
        res += Integer.toString(x);
        res += " ";
        res += Integer.toString(y);
        return res;
    }
}
