package model.artefact;

public abstract class Artefact {
    protected String name;
    protected String type;
    protected int amount;

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public int getAmount(){
        return amount;
    }

    public String toString(){
        return name;
    }
     
}