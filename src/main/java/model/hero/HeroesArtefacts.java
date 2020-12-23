package model.hero;

public class HeroesArtefacts {
    private final String H_Name;
    private final String A_Name;

    public HeroesArtefacts(String hname, String aname){
        H_Name = hname;
        A_Name = aname;
    }

    public String getHName(){
        return H_Name;
    }

    public String getAName(){
        return A_Name;
    }
}
