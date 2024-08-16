package com.game.spaceinvaders;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {

    private ArrayList<GameObject> invaders = new ArrayList<GameObject>();
    private ArrayList<GameObject> invadersShoots = new ArrayList<GameObject>();
    private ArrayList<GameObject> playerShoots = new ArrayList<GameObject>();
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private GameObject player;
    private IntegerProperty score = new SimpleIntegerProperty(0);
    private IntegerProperty numLives = new SimpleIntegerProperty(3);

    private int numberForMove= 0;
    private boolean moveRight = true;   //zaciname totiz zleva
    private static final int INVADER_JUMP_COL =  25;
    private static final int INVADER_MOVE_SIDE = 15;
    private static final int PLAYER_MOVE_SIZE = 4;

    public ArrayList<GameObject> SHOOTS_TO_REMOVE = new ArrayList<GameObject>();
    public ArrayList<GameObject> SHOOTS_TO_ADD = new ArrayList<GameObject>();


    private int PROJECTILE_SIZE = 4;
    //private int SPACE_SHIP_SIZE = 64;
    //private int SPACE_SHIP_SIZE = 30;
    private int ALIEN_SHIP_WIDTH = 64;
    private int ALIEN_SHIP_HEIGHT = 48;

    //TODO: zmenit
    private int PLAYER_SHIP_WIDTH = 70;
    private int PLAYER_SHIP_HEIGHT = 86;

    public GameState(){
        player = new GameObject(350,650,PLAYER_SHIP_WIDTH,PLAYER_SHIP_HEIGHT);
        createAddInvaders();
        createAddWalls();
    }

    public void createAddWalls(){
        for(int i = 0, w = 60; i < 4; i++, w = w + 150) {
            walls.add(new Wall(550, w, 100, 20));
        }
    }
    private void createAddInvaders() {
        int startX = 50;  // počáteční X pozice pro první invader
        int startY = 80;  // počáteční Y pozice pro první řádek
        int spacingX = ALIEN_SHIP_WIDTH + 20;  // horizontální mezera mezi invadery
        int spacingY = ALIEN_SHIP_HEIGHT + 20;  // vertikální mezera mezi řádky

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int x = startX + col * spacingX;
                int y = startY + row * spacingY;
                invaders.add(new GameObject(x, y, ALIEN_SHIP_WIDTH, ALIEN_SHIP_HEIGHT));
            }
        }
    }

    //strileni
    public GameObject createProjectile(double x, double y){
        GameObject newProjectile = new GameObject((int)x, (int)y,PROJECTILE_SIZE, PROJECTILE_SIZE);
        SHOOTS_TO_ADD.add(newProjectile);
        return newProjectile;
    }

    public GameObject playerFire(){
        GameObject projectile = createProjectile(player.getRow(), player.getCol() - ((double) player.getWidth() / 2));
        playerShoots.add(projectile);
        return projectile;
    }
    public GameObject invaderFireRandom(){
        Random random = new Random();
        int index = random.nextInt( invaders.size()-1);
        return invaderFire(index);

    }
    public GameObject invaderFire(int index){
        //GameObject projectile  = createProjectile(invaders.get(index).getRow(), invaders.get(index).getRow() + ((double) invaders.get(index).getWidth() / 2));
        //System.out.println();
        GameObject projectile  = createProjectile(invaders.get(index).getRow(), invaders.get(index).getCol() + ((double) invaders.get(index).getWidth() / 2));

        invadersShoots.add(projectile);
        return projectile;
    }

    //
    public boolean isPlayerKilled() {
        for (GameObject shoot: invadersShoots) {
            if(isProjectileInSpaceShip(player,shoot)){

                setLives(getLives()-1);
                GameObject plShot  = shoot;
                plShot.setActive(false);
                SHOOTS_TO_REMOVE.add(plShot);
                invadersShoots.remove(invadersShoots.indexOf(shoot));
                return true;
            }
        }
        if(getLives() == 0){
            return false;
        }
        return true;
    }

    public boolean isInvaderKilled(){
        for (GameObject invader: invaders) {
            for (GameObject shoot: playerShoots) {
                if(isProjectileInSpaceShip(invader,shoot)){
                    GameObject plShot  = shoot;
                    playerShoots.remove(playerShoots.indexOf(shoot));
                    //!!!!
                    invader.setActive(false);
                    addPoints();
                    plShot.setActive(false);
                    SHOOTS_TO_REMOVE.add(plShot);
                    invaders.remove(invader);
                    return true;
                }
            }
        }
        return false;
    }

    public void moveLeft(){
        int col = (int)getPlayer().getRow();
        if (col > 0) getPlayer().setRow(col - PLAYER_MOVE_SIZE);
    }
    public void moveRight(){
        int col = (int)getPlayer().getRow();
        if (col < 700) getPlayer().setRow(col + PLAYER_MOVE_SIZE);

    }
    public void isWallDestroied(){
        for (Wall wall: walls) {
            for (GameObject shoot: playerShoots) {
                if(isProjectileInWall(wall,shoot)){
                    GameObject plShot  = shoot;
                    plShot.setActive(false);
                    SHOOTS_TO_REMOVE.add(plShot);
                    playerShoots.remove(playerShoots.indexOf(shoot));
                    if(!wall.isActive()){
                        walls.remove(wall);
                    }
                    else{
                        wall.minusLive();
                    }
                    return;
                }
            }

            for (GameObject shoot: invadersShoots) {
                if(isProjectileInWall(wall,shoot)){
                    GameObject plShot  = shoot;
                    plShot.setActive(false);
                    SHOOTS_TO_REMOVE.add(plShot);
                    invadersShoots.remove(invadersShoots.indexOf(shoot));
                    if(!wall.isActive()){
                        walls.remove(wall);
                    }
                    else{
                        wall.minusLive();
                    }
                    return;
                }
            }
        }
    }

    public void invadersMove() {
        double currCenterVal;
        if(moveRight){ //do prava
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getRow();
                invader.setRow((int)currCenterVal + INVADER_MOVE_SIDE);
            }
            numberForMove = numberForMove + 1;
        }
        else{//do leva
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getRow();
                invader.setRow((int)currCenterVal - INVADER_MOVE_SIDE);
            }
            numberForMove = numberForMove - 1;
        }
        if(numberForMove >= 12){
            moveRight = false;
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getCol();
                invader.setCol((int)currCenterVal + INVADER_JUMP_COL);
            }
        }
        else if(numberForMove <= 0){
            moveRight = true;
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getCol();
                invader.setCol((int)currCenterVal + INVADER_JUMP_COL);
            }
        }
    }


    //zasahy
    public boolean isProjectileInWall(Wall wall,GameObject projectile){
        double width = wall.getWidth();
        double height = wall.getHeight();
        boolean isHit = (wall.getColumn() <= projectile.getRow() && projectile.getRow() <= (wall.getColumn() + width))  //x
                && (wall.getRow() <= projectile.getCol() && projectile.getCol() <= (wall.getRow() + height));   //y
        return isHit;
    }


    public boolean isProjectileInSpaceShip(GameObject ship, GameObject projectile) {
        /*
        double shipX = ship.getRow();
        double shipY = ship.getCol();
        double shipWidth = ship.getWidth();
        double shipHeight = ship.getHeight();

        double projectileX = projectile.getRow();
        double projectileY = projectile.getCol();

        // Kontrola, zda je střela v horizontálním rozsahu lodi
        boolean isInHorizontalRange = isBetween(shipX, shipX + shipWidth, projectileX);

        // Kontrola, zda je střela ve spodní polovině lodi
        boolean isInBottomHalf = isBetween(shipY + shipHeight / 2, shipY + shipHeight, projectileY);



        if(isInHorizontalRange && isInBottomHalf){
            System.out.println("--------------");
            System.out.println(shipX);
            System.out.println(shipY);
            System.out.println(shipWidth);
            System.out.println(shipHeight);
            System.out.println("--------------");
        }
        return isInHorizontalRange && isInBottomHalf;

         */
        double shipWidth = ship.getWidth();
        double shipHeight = ship.getHeight();






        boolean isHit = isBetween(ship.getRow() - shipWidth / 2, ship.getRow() + shipWidth / 2, projectile.getRow())
                && isBetween(ship.getCol() - shipHeight / 2, ship.getCol() + shipHeight / 2, projectile.getCol());




        /*
        boolean isHit = isBetween(ship.getRow(), ship.getRow() + shipWidth, projectile.getRow())
                && isBetween(ship.getCol() + shipHeight / 2, ship.getCol() + shipHeight, projectile.getCol());


         */
        return isHit;



    }



    private boolean isBetween(double min, double max, double value) {
        return value >= min && value <= max;
    }

    //gettery

    public List<GameObject> getInvadersShoots() {
        return invadersShoots;
    }

    public List<GameObject> getPlayerShoots() {
        return playerShoots;
    }

    public List<GameObject> getInvaders() {
        return invaders;
    }

    public GameObject getPlayer() {
        return player;
    }

    public void addPoints(){
        setScore(getScore()+100);
    }

    public ArrayList<Wall> getWalls(){
        return walls;
    }




    ///
    public final IntegerProperty scoreProperty() {
        return this.score;
    }
    public final int getScore() {
        return this.scoreProperty().get();
    }
    public final void setScore(final int score) {
        this.scoreProperty().set(score);
    }


    public final IntegerProperty livesProperty() {
        return this.numLives;
    }
    public final int getLives() {
        return this.livesProperty().get();
    }
    public final void setLives(final int lives) {
        this.livesProperty().set(lives);
    }

}
