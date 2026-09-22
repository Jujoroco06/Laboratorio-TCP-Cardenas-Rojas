package co.icesi.buscaminas.services;

import co.icesi.buscaminas.model.BoardGame;
import co.icesi.buscaminas.model.Cell;
public class ServicesImpl{
    private  BoardGame game;

    public ServicesImpl(){
        game = new BoardGame();
        game.initGame(8, 8, 10);
    }

    public BoardGame getGame() {
        return game;
    }
    public int initGame(int n, int m, int mines) {
        return game.initGame(n, m, mines);
    }
    
    public boolean selectCell(int i, int j) {

        
        return game.selectCell(i, j)?game.markCell(i,j):false;
    }

    public void showAll(boolean show) {
        
        game.showAll(show);
    }

    public Cell[][] printBoard() {
        game.printBoard();
        Cell [][] cells = game.getBoard();

        return cells;
    }
<<<<<<< HEAD
    public Cell[][] markCell(int i, int j) {
        game.markCell(i, j);
        return game.getBoard();
    }
=======

    public markCell
>>>>>>> 64004e6eaa929f0a29faf72da2524d7b4de96e07
    
}
