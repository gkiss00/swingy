package model.database;

import model.hero.Hero;
import model.artefact.Artefact;

import java.sql.*;

public class Database{
    private Connection connection;

    public Database(){
        try{

            String url = "jdbc:mysql://localhost:3306/swingy";
            String user = "root";

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, user);
            System.out.println("Connection with database etablished");
        }catch(Exception e){
            System.out.println(e + "\n" + e.getMessage());
        }
    }

    //*****************************************************************************************
    //*****************************************************************************************
    //HEROES
    //*****************************************************************************************
    //*****************************************************************************************

    public ResultSet getAllHeroes() throws Exception{
        ResultSet res;
        String request = "SELECT * FROM heroes";

        Statement statement = connection.createStatement();
        res = statement.executeQuery(request);

        return (res);
    }

    public void addHero(Hero hero) throws SQLException {
        String request = "INSERT INTO heroes VALUES (";
        request += "'";
        request += hero.getName();
        request += "'";
        request += ", ";
        request += "'";
        request += hero.get_Class();
        request += "'";
        request += ", ";
        request += Integer.toString(hero.getLevel());
        request += ", ";
        request += Integer.toString(hero.getXp());
        request += ", ";
        request += Integer.toString(hero.getAttack());
        request += ", ";
        request += Integer.toString(hero.getDefense());
        request += ", ";
        request += Integer.toString(hero.getHitPoints());
        request += ")";

        Statement statement = connection.createStatement();
        statement.executeUpdate(request);
    }

    public void updateHero(Hero hero) throws SQLException {
        String request = "UPDATE heroes SET ";
        request += "H_Level = ";
        request += Integer.toString(hero.getLevel());
        request += ", ";
        request += "H_Experience = ";
        request += Integer.toString(hero.getXp());
        request += ", ";
        request += "H_Attack = ";
        request += Integer.toString(hero.getAttack());
        request += ", ";
        request += "H_Defense = ";
        request += Integer.toString(hero.getDefense());
        request += ", ";
        request += "H_HitPoints = ";
        request += Integer.toString(hero.getHitPoints());
        request += " WHERE H_Name = '";
        request += hero.getName();
        request += "'";

        Statement statement = connection.createStatement();
        statement.executeUpdate(request);
    }

    //*****************************************************************************************
    //*****************************************************************************************
    //ARTEFACTS
    //*****************************************************************************************
    //*****************************************************************************************

    public ResultSet getAllArtefacts() throws Exception{
        ResultSet res;
        String request = "SELECT * FROM artefacts";

        Statement statement = connection.createStatement();
        res = statement.executeQuery(request);

        return (res);
    }

    public void addArtefact(Artefact artefact) throws SQLException {
        String request = "INSERT INTO artefacts VALUES (";
        request += "'";
        request += artefact.getName();
        request += "'";
        request += ", ";
        request += "'";
        request += artefact.getType();
        request += "'";
        request += ", ";
        request += Integer.toString(artefact.getAmount());
        request += ")";

        Statement statement = connection.createStatement();
        statement.executeUpdate(request);
    }

    //*****************************************************************************************
    //*****************************************************************************************
    //ARTEFACTS
    //*****************************************************************************************
    //*****************************************************************************************

    public ResultSet getAllHeroesArtefacts() throws Exception{
        ResultSet res;
        String request = "SELECT * FROM heroesartefacts";

        Statement statement = connection.createStatement();
        res = statement.executeQuery(request);

        return (res);
    }

    public void addHeroesArtefact(Hero hero, Artefact artefact) throws SQLException {
        String pre_request = "DELETE FROM heroesartefacts WHERE H_Name = ";
        pre_request += "'";
        pre_request += hero.getName();
        pre_request += "'";
        pre_request += " and A_Type = ";
        pre_request += "'";
        pre_request += artefact.getType();
        pre_request += "'";

        Statement statement = connection.createStatement();
        statement.executeUpdate(pre_request);


        String request = "INSERT INTO heroesartefacts VALUES (";
        request += "'";
        request += hero.getName();
        request += "'";
        request += ", ";
        request += "'";
        request += artefact.getName();
        request += "'";
        request += ", ";
        request += "'";
        request += artefact.getType();
        request += "'";
        request += ")";

        statement.executeUpdate(request);
    }
}