package tentsAndTrees;

import csp.AbstractCSP;
import csp.AbstractConstraint;

import java.util.ArrayList;

public class TentsAndTreesCSP extends AbstractCSP {
    private AbstractConstraint[] constraints;
    private ArrayList<Cell> variables;
    private Grid grid;

    public TentsAndTreesCSP(Grid grid){
        this.variables = grid.getOpenCells();
    }
    @Override
    public void addVariableToDomain() {

    }

    @Override
    public void deleteVariableFromDomain() {

    }

    @Override
    public void addVariable() {

    }

    @Override
    public void deleteVariable() {

    }

    @Override
    public ArrayList<Object> getVariables() {
        return null;
    }
}
