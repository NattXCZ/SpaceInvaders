package com.game.spaceinvaders;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {

    private ArrayList<GameObject> invaders = new ArrayList<GameObject>();
    private  ArrayList<GameObject> invadersShoots = new ArrayList<GameObject>();
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
    private int SPACE_SHIP_SIZE = 15;

    public GameState(){
        player = new GameObject(350,650,SPACE_SHIP_SIZE);
        createAddInvaders();
        createAddWalls();
    }

    public void createAddWalls(){
        for(int i = 0, w = 60; i < 4; i++, w = w + 150) {
            walls.add(new Wall(550, w, 100, 20));
        }
    }

    public void createAddInvaders() {
        makeRowInvaders(50,0);
        makeRowInvaders(120,7);
        makeRowInvaders(190, 14);
        makeRowInvaders(260, 21);

    }
    public void makeRowInvaders(int row, int index){
        for(int i = 0, w = 40; i < 7; i++, w += 70) {
            invaders.add(new GameObject(w, row,SPACE_SHIP_SIZE));
        }
    }
    //strileni
    public GameObject createProjectile(double x, double y){
        GameObject newProjectile = new GameObject((int)x, (int)y,PROJECTILE_SIZE);
        SHOOTS_TO_ADD.add(newProjectile);
        return newProjectile;
    }

    public GameObject playerFire(){
        GameObject projectile = createProjectile(player.getX(), player.getY()- player.getSize());
        playerShoots.add(projectile);
        return projectile;
    }
    public GameObject invaderFireRandom(){
        Random random = new Random();
        int index = random.nextInt( invaders.size()-1);
        return invaderFire(index);

    }
    public GameObject invaderFire(int index){
        GameObject projectile  = createProjectile(invaders.get(index).getX(), invaders.get(index).getX() + invaders.get(index).getSize());
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
        int col = (int)getPlayer().getX();
        if (col > 0) getPlayer().setX(col - PLAYER_MOVE_SIZE);
    }
    public void moveRight(){
        int col = (int)getPlayer().getX();
        if (col < 700) getPlayer().setX(col + PLAYER_MOVE_SIZE);

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
                currCenterVal = invader.getX();
                invader.setX((int)currCenterVal + INVADER_MOVE_SIDE);
            }
            numberForMove = numberForMove + 1;
        }
        else{//do leva
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getX();
                invader.setX((int)currCenterVal - INVADER_MOVE_SIDE);
            }
            numberForMove = numberForMove - 1;
        }
        if(numberForMove >= 12){
            moveRight = false;
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getY();
                invader.setY((int)currCenterVal + INVADER_JUMP_COL);
            }
        }
        else if(numberForMove <= 0){
            moveRight = true;
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getY();
                invader.setY((int)currCenterVal + INVADER_JUMP_COL);
            }
        }
    }


    //zasahy
    public boolean isProjectileInWall(Wall wall,GameObject projectile){
        double width = wall.getWidth();
        double height = wall.getHeight();
        boolean isHit = (wall.getColumn() <= projectile.getX() && projectile.getX() <= (wall.getColumn() + width))  //x
                && (wall.getRow() <= projectile.getY() && projectile.getY() <= (wall.getRow() + height));   //y
        return isHit;
    }

    private boolean isBetween( double from, double to, double find){
        if (from <= find && find <= to) {
            return true;
        }
        return false;
    }

    public boolean isProjectileInSpaceShip(GameObject ship , GameObject projectile){
        if(!ship.activeProperty().getValue()){
            return false;
        }
        double radius = ship.getSize();
        boolean isHit = isBetween(ship.getX() - radius, ship.getX() + radius, projectile.getX())
                && isBetween(ship.getY() - radius, ship.getY() + radius, projectile.getY());
        return isHit;
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
