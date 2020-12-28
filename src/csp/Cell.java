package csp;

import java.util.ArrayList;

public class Cell {
    private boolean isTree, isSet, isNothing, isTent;
    private int row, col;

    //0=nothing, 1=tent
    private ArrayList<Integer> domains = new ArrayList<>();
    private ArrayList<Cell> vdNeighborsWithoutTrees = new ArrayList<>(); //horizontal and vertical neighbors
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

    private void updateDomainsForNeighborsAfterSetTent(){
        for(Cell neighbor: this.hvdNeighborsWithoutTrees){
            neighbor.removeOptionFromDomains(1);
        }
    }

    private void updateDomainsForNeighborsAfterSetNothing(){
        if(this.trees == null)
            return;
        for(Cell tree: this.trees){
            tree.updateDomainForNeighborsAsTree();
        }
    }

    public void removeOptionFromDomains(int option){
        this.domains.removeIf(obj -> obj == option);
    }

    public void updateDomainForNeighborsAsTree(){
        if(!this.isTree)
            return;
        ArrayList<Cell> cellsWhichCanBeTent = new ArrayList<>(vdNeighborsWithoutTrees);
        cellsWhichCanBeTent.removeIf(obj -> obj.isSet);
        cellsWhichCanBeTent.removeIf(obj -> !obj.domains.contains(1));
        if(cellsWhichCanBeTent.size() == 1)
            cellsWhichCanBeTent.get(0).removeOptionFromDomains(0);
    }














    //Getter and Setter
    public void setVdNeighborsWithoutTrees(ArrayList<Cell> vdNeighborsWithoutTrees){
        this.vdNeighborsWithoutTrees = vdNeighborsWithoutTrees;
    }

    public void setHvdNeighborsWithoutTrees(ArrayList<Cell> hvdNeighborsWithoutTrees){
        this.hvdNeighborsWithoutTrees = hvdNeighborsWithoutTrees;
    }

    public void initiallySetDomains(int[] domains){
        for(int domain: domains){
            this.domains.add(domain);
        }
    }

    public void setTrees(ArrayList<Cell> trees){
        this.trees = trees;
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

    public int getDomainSize(){
        return domains.size();
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

    public void setSet(){
        if(domains.get(0) == 0){
            this.isNothing = true;
            this.updateDomainsForNeighborsAfterSetNothing();
        }else {
            this.isTent = true;
            this.updateDomainsForNeighborsAfterSetTent();
        }
        this.isSet = true;
    }
}
