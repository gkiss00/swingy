package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import exception.BoundException;
import model.artefact.Artefact;
import model.enemy.*;

public class Map{
    private int size;
    private int[][] map;
    private int pos_x = 0;
    private int pos_y = 0;
    private final List<Enemy> enemies = new ArrayList<>();

    public Map(int level) {
        this.size = getMapSize(level);
        this.map = new int[size][size];
        this.pos_x = (size / 2);
        this.pos_y = (size / 2);
    }

    public void fillWithEnemies(int lvl, List<Artefact> artefacts){
        enemies.clear();
        Random rand = new Random();

        //FOREACH CASE
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                //1 CHANCE ON 4
                if (rand.nextInt(4) == 0){
                    setEnemy(lvl, artefacts, x, y);
                }
            }
        }
    }

    private void setEnemy(int lvl, List<Artefact> artefacts, int x, int y){
        Random rand = new Random();
        Artefact tmp = null;
        int ratio = lvl < 10 ? (3) : (4);
        int r = rand.nextInt(ratio);

        int arm = rand.nextInt(5);
        if (arm == 0)
            tmp = artefacts.get(rand.nextInt(artefacts.size()));
        if(r == 0)
            enemies.add(new Goblin( rand.nextInt(lvl + 2) + 1, x, y, tmp));
        else if(r == 1)
            enemies.add(new Orc( rand.nextInt(lvl + 2) + 1, x, y, tmp));
        else if (r == 2)
            enemies.add(new Troll( rand.nextInt(lvl + 2) + 1, x, y, tmp));
        else
            enemies.add(new Dragon( rand.nextInt(lvl + 2) + 1, x, y, tmp));
        map[y][x] = 2;
    }

    private int getMapSize(int size){
        int map_size = ((size - 1) * 5) + 10;
        if (map_size % 2 == 0)
            map_size -= 1;
        return (map_size);
    }

    private boolean isEnemy(){
        if (map[pos_y][pos_x] == 2)
            return (true);
        return (false);
    }

    @Override
    public String toString(){
        String str = "";
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                if (pos_x == x && pos_y == y)
                    str += "0";
                else if (map[y][x] == 0 || map[y][x] == 2)
                    str += "#";
                else
                    str += " ";
                if (x != size -1)
                    str += " ";
            }
            if (y != size -1)
                str += "\n";
        }
        return (str);
    }

    public boolean update(String direction){
        map[pos_y][pos_x] = 1;
        if (direction.compareTo("NORTH") == 0){
            pos_y -= 1;
        }
        if (direction.compareTo("EAST") == 0){
            pos_x += 1;
        }
        if (direction.compareTo("SOUTH") == 0){
            pos_y += 1;
        }
        if (direction.compareTo("WEST") == 0){
            pos_x -= 1;
        }
        return isEnemy();
    }

    public int update(int x, int y){
        int ret = 0;
        if ((x == pos_x + 1 || x == pos_x -1) && y == pos_y){
            map[pos_y][pos_x] = 1;
            pos_x = x;
            ret = 1;
        }else if ((y == pos_y + 1 || y == pos_y - 1) && x == pos_x){
            map[pos_y][pos_x] = 1;
            pos_y = y;
            ret = 1;
        }
        if (ret == 1 && isEnemy())
            ret = 2;
        return ret;
    }

    public Enemy getEnemy(){
        for (int i = 0; i < enemies.size(); ++i){
            int x = enemies.get(i).getX();
            int y = enemies.get(i).getY();
            if (pos_x == x && pos_y == y)
                return (enemies.get(i));
        }
        return (null);
    }

    public int getMapAt(int x, int y) throws Exception{
        if (x > 0 && x < size && y > 0 && y < size)
            return (map[x][y]);
        else
            throw new BoundException("index of map out of bound");
    }

    public int getSize(){
        return (size);
    }

    public int getPosX(){
        return (pos_x);
    }

    public int getPosY(){
        return (pos_y);
    }
}