package csp;

import services.CSVReader;

import java.io.IOException;
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

public class Runner {
    public static void main(String[] args){
        Object[] results;
        try {
            CSVReader.setCsvFilePath(0);
            Object[] info = CSVReader.read();
            Grid grid = new Grid((Cell[][]) info[0], (ArrayList<Integer>) info[1], (ArrayList<Integer>) info[2]);
            grid.printField();
            grid.startSolvingMCV();
            grid.printField();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
