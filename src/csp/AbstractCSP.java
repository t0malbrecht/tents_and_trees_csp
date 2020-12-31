package csp;

/**
 * How to solve CSP:
 *  1. Define variables
 *  2. Define constraints
 *  3. Use general solution strategy (seperated from model)
 */

import tentsAndTrees.Cell;

import java.util.ArrayList;

/**
 * Ein Constraint-Satisfaction Problem (CSP) ist beschrieben
 * durch (V,D,C)
 * V = {V1, .. , Vn} ist endliche Menge von Variablen = Menge an Zellen auf dem Grid
 * D = {D1, .. , Dn} ist endliche Menge von Wertebereichen (Domains) = Zelt, Baum oder Leer
 * jede Variable Vi kann nur Werte aus Domain Di annehmen
 * C ist endliche Menge von Restriktionen (Constraints) / Relationen zwischen Variablen Ein Constraint C ist k-stellige Relation
 * Ziel: Finde für jede Variable xi Î V einen Wert wi Î Di so dass alle
 * Constraints aus C erfüllt sind.
 */

public abstract class AbstractCSP {

    public abstract void addVariableToDomain();

    public abstract void deleteVariableFromDomain();

    public abstract void addVariable();

    public abstract void deleteVariable();

    public abstract  ArrayList<Object> getVariables();


}
