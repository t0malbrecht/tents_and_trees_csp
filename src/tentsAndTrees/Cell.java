package tentsAndTrees;

import java.util.ArrayList;

public class Cell {
    private boolean isTree, isSet, isNothing, isTent;
    private int row, col;

    //0=nothing, 1=tent
    private ArrayList<Integer> domains = new ArrayList<>();

    private ArrayList<Cell> hvNeighborsWithoutTrees = new ArrayList<>(); //horizontal and vertical neighbors
    private ArrayList<Cell> hvdNeighborsWithoutTrees = new ArrayList<>(); //horizontal, vertical and diagonal neighbors !!!is not set when Cell isTree
    private ArrayList<Cell> trees = new ArrayList<>(); // trees which are horizontally or vertically next to this cell

    public Cell(boolean tree, int row, int col){
        this.isTree = tree;
        this.row = row;
        this.col = col;
        this.isSet = false;
        this.isTent = false;
        this.isNothing = false;
    }

    public void removeOptionFromDomains(int option){
        this.domains.removeIf(obj -> obj == option);
    }

    public void addOptionToDomains(int option){
        this.domains.add(option);
    }








    //Getter and Setter
    public void setHvNeighborsWithoutTrees(ArrayList<Cell> hvNeighborsWithoutTrees){
        this.hvNeighborsWithoutTrees = hvNeighborsWithoutTrees;
    }

    public ArrayList<Cell> getHvNeighborsWithoutTrees() {
        return hvNeighborsWithoutTrees;
    }

    public void setHvdNeighborsWithoutTrees(ArrayList<Cell> hvdNeighborsWithoutTrees){
        this.hvdNeighborsWithoutTrees = hvdNeighborsWithoutTrees;
    }

    public ArrayList<Cell> getHvdNeighborsWithoutTrees() {
        return hvdNeighborsWithoutTrees;
    }

    public void initiallySetDomains(int[] domains){
        for(int domain: domains){
            this.domains.add(domain);
        }
    }

    public void setTrees(ArrayList<Cell> trees){
        this.trees = trees;
    }

    public ArrayList<Cell> getTrees(){
        return trees;
    }

    public int getCol() {
        return col;
    }

    public int getRow(){
        return row;
    }

    public boolean isTree(){
        return isTree;
    }

    public ArrayList<Integer> getDomain(){
        return domains;
    }

    public boolean isSet(){
        return isSet;
    }

    public boolean isTent(){
        return isTent;
    }

    public boolean isNothing(){
        return isNothing;
    }
}
