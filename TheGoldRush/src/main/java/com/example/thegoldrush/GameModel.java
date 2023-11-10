package com.example.thegoldrush;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameModel {

    private String selectedLevel;
    private int paramCells;
    private int bombs = 10;
    private int totalCells;
    private int hints=0;
    private Text bombsText;
    private Text timerText;
    private boolean[][] isBomb;
    private boolean[][] isCellOpened;
    private boolean[][] isFlagPlaced;
    private int totalGoldCells;

    public GameModel(String selectedLevel) {
        this.selectedLevel=selectedLevel;
        this.totalGoldCells = totalCells - bombs;
        if (selectedLevel.equals("Easy")) {
            this.hints = 3;
            this.totalCells = 50;
            this.paramCells=50;
            this.bombs = 10;
        } else if (selectedLevel.equals("Medium")) {
            this.hints = 2;
            this.paramCells=70;
            this.totalCells = 98;
            this.bombs = 20;
        } else if (selectedLevel.equals("Hard")) {
            this.hints = 1;
            this.totalCells=162;
            this.paramCells=90;
            this.bombs = 30;
        }

        this.totalGoldCells = totalCells - bombs;
        this.bombsText = new Text("" + bombs);

        initializeGame(paramCells/10,paramCells/5);
    }

    private void setIsBomb(boolean[][] isBomb){
        this.isBomb=isBomb;
    }

    private void setIsCellOpened(boolean[][] isCellOpened){
        this.isCellOpened=isCellOpened;
    }
    private void setIsFlagPlaced(boolean[][] isFlagPlaced){
        this.isFlagPlaced=isFlagPlaced;
    }
    public boolean[][] getIsBomb(){
        return isBomb;
    }
    public boolean[][] getIsCellOpened(){
        return isCellOpened;
    }
    public boolean[][] getIsFlagPlaced(){
        return isFlagPlaced;
    }


    public void initializeGame(int rows, int cols) {

        isBomb = new boolean[rows][cols];
        isCellOpened = new boolean[rows][cols];
        isFlagPlaced = new boolean[rows][cols];

        for (int i = 0; i < bombs; i++) {
            int randRow, randCol;
            do {
                randRow = (int) (Math.random() * rows);
                randCol = (int) (Math.random() * cols);
            } while (isBomb[randRow][randCol]);

            isBomb[randRow][randCol] = true;
        }

        setIsBomb(isBomb);
        setIsCellOpened(isCellOpened);
        setIsFlagPlaced(isFlagPlaced);
    }



    public int calculateBombsNearby(int row, int col) {
        int bombsNearby = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < paramCells/10 && c >= 0 && c < paramCells/5 && isBomb[r][c]) {
                    bombsNearby++;
                }
            }
        }
        return bombsNearby;
    }



    public boolean checkWinCondition(int flagsPlaced, int goldCellsOpened) {

        System.out.println(flagsPlaced);
        System.out.println(bombs);
        System.out.println(goldCellsOpened);
        System.out.println(totalGoldCells);

        if (flagsPlaced == (bombs) && goldCellsOpened == totalGoldCells) {
            return true;
        }

        return false;
    }


}
