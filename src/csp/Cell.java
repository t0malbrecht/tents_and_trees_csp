package csp;

public class Cell {
    private boolean tree;
    private int row, col;

    public Cell(boolean tree, int row, int col){
        this.tree = tree;
        this.row = row;
        this.col = col;
    }


















    //Getter and Setter
    public int getCol() {
        return col;
    }

    public int getRow(){
        return row;
    }

    public boolean isTree(){
        return tree;
    }
}
