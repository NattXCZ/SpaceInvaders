package com.game.spaceinvaders;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {

    private final ArrayList<GameObject> invaders = new ArrayList<GameObject>();
    private final ArrayList<GameObject> invadersShoots = new ArrayList<GameObject>();
    private final ArrayList<GameObject> playerShoots = new ArrayList<GameObject>();
    private final ArrayList<Wall> walls = new ArrayList<Wall>();
    private final GameObject player;
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty numLives = new SimpleIntegerProperty(3);

    private int numberForMove= 0;
    private boolean moveRight = true;
    private static final int INVADER_JUMP_COL =  25;
    private static final int INVADER_MOVE_SIDE = 15;
    private static final int PLAYER_MOVE_SIZE = 4;

    public ArrayList<GameObject> SHOOTS_TO_REMOVE = new ArrayList<GameObject>();
    public ArrayList<GameObject> SHOOTS_TO_ADD = new ArrayList<GameObject>();

    private int PROJECTILE_SIZE = 4;
    private int ALIEN_SHIP_WIDTH = 64;
    private int ALIEN_SHIP_HEIGHT = 48;
    private int PLAYER_SHIP_WIDTH = 70;
    private int PLAYER_SHIP_HEIGHT = 86;

    public GameState(){
        player = new GameObject(350,650,PLAYER_SHIP_WIDTH,PLAYER_SHIP_HEIGHT);
        createAddInvaders();
        createAddWalls();
    }

    //creating game objects
    public void createAddWalls(){
        for(int i = 0, w = 60; i < 4; i++, w = w + 150) {
            walls.add(new Wall(550, w, 100, 20));
        }
    }

    private void createAddInvaders() {
        int startX = 50;
        int startY = 80;
        int spacingX = ALIEN_SHIP_WIDTH + 20;
        int spacingY = ALIEN_SHIP_HEIGHT + 20;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int x = startX + col * spacingX;
                int y = startY + row * spacingY;
                invaders.add(new GameObject(x, y, ALIEN_SHIP_WIDTH, ALIEN_SHIP_HEIGHT));
            }
        }
    }

    //shooting
    public GameObject createProjectile(double x, double y){
        GameObject newProjectile = new GameObject((int)x, (int)y,PROJECTILE_SIZE, PROJECTILE_SIZE);
        SHOOTS_TO_ADD.add(newProjectile);
        return newProjectile;
    }

    public void playerFire(){
        GameObject projectile = createProjectile(player.getRow(), player.getCol() - ((double) player.getWidth() / 2));
        playerShoots.add(projectile);
    }

    public void invaderFireRandom(){
        Random random = new Random();
        int index = random.nextInt( invaders.size()-1);
        invaderFire(index);
    }

    public void invaderFire(int index){
        GameObject projectile  = createProjectile(invaders.get(index).getRow(), invaders.get(index).getCol() + ((double) invaders.get(index).getWidth() / 2));
        invadersShoots.add(projectile);
    }

    //zasahy
    public boolean isProjectileInWall(Wall wall,GameObject projectile){
        double width = wall.getWidth();
        double height = wall.getHeight();
        return (wall.getCol() <= projectile.getRow() && projectile.getRow() <= (wall.getCol() + width))
                && (wall.getRow() <= projectile.getCol() && projectile.getCol() <= (wall.getRow() + height));
    }


    public boolean isProjectileInSpaceShip(GameObject ship, GameObject projectile) {
        double shipWidth = ship.getWidth();
        double shipHeight = ship.getHeight();
        return isBetween(ship.getRow() - shipWidth / 2, ship.getRow() + shipWidth / 2, projectile.getRow())
                && isBetween(ship.getCol() - shipHeight / 2, ship.getCol() + shipHeight / 2, projectile.getCol());
    }

    private boolean isBetween(double min, double max, double value) {
        return value >= min && value <= max;
    }

    //TODO: zjedondušit metody
    public boolean isPlayerKilled() {
        for (GameObject shoot: invadersShoots) {
            if(isProjectileInSpaceShip(player,shoot)){
                setLives(getLives()-1);
                shoot.setActive(false);
                SHOOTS_TO_REMOVE.add(shoot);
                invadersShoots.remove(shoot);
                return true;
            }
        }
        return getLives() != 0;
    }

    public boolean isInvaderKilled(){
        for (GameObject invader: invaders) {
            for (GameObject shoot: playerShoots) {
                if(isProjectileInSpaceShip(invader,shoot)){
                    playerShoots.remove(shoot);
                    invader.setActive(false);
                    addPoints();
                    shoot.setActive(false);
                    SHOOTS_TO_REMOVE.add(shoot);
                    invaders.remove(invader);
                    return true;
                }
            }
        }
        return false;
    }

    public void isWallDestroyed(){
        for (Wall wall: walls) {
            for (GameObject shoot: playerShoots) {
                if(isProjectileInWall(wall,shoot)){
                    shoot.setActive(false);
                    SHOOTS_TO_REMOVE.add(shoot);
                    playerShoots.remove(shoot);
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
                    shoot.setActive(false);
                    SHOOTS_TO_REMOVE.add(shoot);
                    invadersShoots.remove(shoot);
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

    //move
    public void moveLeft(){
        int col = getPlayer().getRow();
        if (col > 0) getPlayer().setRow(col - PLAYER_MOVE_SIZE);
    }

    public void moveRight(){
        int col = (int)getPlayer().getRow();
        if (col < 700) getPlayer().setRow(col + PLAYER_MOVE_SIZE);

    }

    public void invadersMove() {
        double currCenterVal;
        if(moveRight){
            for (GameObject invader : getInvaders()) {
                currCenterVal = invader.getRow();
                invader.setRow((int)currCenterVal + INVADER_MOVE_SIDE);
            }
            numberForMove = numberForMove + 1;
        }
        else{
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

    //getters and setters
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
