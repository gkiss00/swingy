package model.hero;

import model.artefact.Helm;
import model.artefact.Armor;
import model.artefact.Weapon;

public class Hero {
    private String name;
    private String _class;
    private int level;
    private int experience;
    private int attack;
    private int defense;
    private int hit_points;
    private int hp;

    private Helm helm = null;
    private Armor armor = null;
    private Weapon weapon = null;

    public Hero(String name, String _class, int lvl, int xp, int attack, int defense, int hit){
        this.name = name;
        this._class = _class;
        this.level = lvl;
        this.experience = xp;
        this.attack = attack;
        this.defense = defense;
        this.hit_points = hit;
        this.hp = hit_points;
    }

    @Override
    public String toString(){
        String str = "Name : ";
        str += this.name;
        str += "\nClass : ";
        str += this._class;
        str += "\nLevel : ";
        str += Integer.toString(this.level);
        str += "\nExp   : ";
        str += Integer.toString(this.experience);
        str += "\nAttack: ";
        str += Integer.toString(this.attack);
        str += "\nDef   : ";
        str += Integer.toString(this.defense);
        str += "\nHP    : ";
        str += Integer.toString(this.hit_points);
        if (weapon != null) {
            str += "\nWeapon: ";
            str += weapon.toString();
        }
        if (armor != null) {
            str += "\nArmor : ";
            str += armor.toString();
        }
        if (helm != null) {
            str += "\nHelm  : ";
            str += helm.toString();
        }
        return (str);
    }

    public String getName(){
        return (this.name);
    }

    public String get_Class(){
        return (this._class);
    }

    public int getLevel(){
        return (this.level);
    }

    public int getXp(){
        return (this.experience);
    }

    public int getAttack(){
        return (this.attack);
    }

    public int getTotalAttack(){
        int total = attack;
        if (weapon != null)
            total += weapon.getAmount();
        return (total);
    }

    public int getDefense(){
        return (this.defense);
    }

    public int getHitPoints(){
        return (this.hit_points);
    }

    public int getHp(){
        return hp;
    }

    public void restaureHp(){
        hp = hit_points;
        if (helm != null)
            hp += helm.getAmount();
    }

    public void takeDamages(int damages){
        int total_defense = defense;
        if (armor != null)
            total_defense += armor.getAmount();
        int damages_taken = damages - total_defense;
        if (damages_taken <= 0)
            damages_taken = 1;
        hp -= damages_taken;
    }

    public void gainXP(int xp){
        System.out.println("You gain some XP");
        int xp_needed = (level * 1000) + (((level - 1) * (level - 1)) * 450);
        this.experience += xp;

        while(this.experience >= xp_needed){
            this.level += 1;
            System.out.println("You level up to level " + level);
            this.experience -= xp_needed;
            xp_needed = (level * 1000) + (((level - 1) * (level - 1)) * 450);
            increaseStats();
        }
    }

    private void increaseStats(){
        this.attack += level * 20;
        this.defense += level * 10;
        this.hit_points += level * 75;
    }

    public void equipWeapon(Weapon w){
        weapon = w;
    }

    public void equipArmor(Armor a){
        armor = a;
    }

    public void equipHelm(Helm h){
        helm = h;
    }
}