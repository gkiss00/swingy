package model.artefact;

public class Weapon extends Artefact{
    public Weapon(String name,int amount){
        this.name = name;
        this.type = "Weapon";
        this.amount = amount;
    }
}
