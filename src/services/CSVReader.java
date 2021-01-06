package services;

import tentsAndTrees.Cell;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CSVReader {
    private static String CSV_FILE_PATH = "./ressources/tents_and_trees_puzzles/";
    private static String[] FILENAMES = {"tents_trees_0.csv",
            "tents_trees_1.csv",
            "tents_trees_2.csv",
            "tents_trees_3.csv",
            "tents_trees_4.csv",
            "tents_trees_5.csv",
            "tents_trees_6.csv",
            "tents_trees_7.csv",
            "tents_trees_8.csv",
            "tents_trees_9.csv",
            "tents_trees_10.csv",
            "20x20.csv",
            "20x20-solution.csv",
            "0-solution.csv"};
    private static String CSV_FILE_PATH_AND_NAME;
    private static ArrayList<Integer> columnsTents;
    private static ArrayList<Integer> rowTents;
    private static Cell[][] cells;

    public static void setCsvFilePath(int file){
        CSV_FILE_PATH_AND_NAME = CSV_FILE_PATH+FILENAMES[file];
    }

    public static Object[] read() throws IOException {
        Object[] results = new Object[3];
        columnsTents = new ArrayList<>();
        rowTents = new ArrayList<>();
        int row = 0;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH_AND_NAME));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)
        ) {
            ArrayList<CSVRecord> records = (ArrayList<CSVRecord>) csvParser.getRecords();
            cells = new Cell[records.size()-1][records.get(0).size()-1];
            for (CSVRecord csvRecord : records) {
                if(row == 0){
                    for(int i=1; i<csvRecord.size(); i++){
                        columnsTents.add(Integer.parseInt(csvRecord.get(i)));
                    }
                }else{
                    rowTents.add(Integer.parseInt(csvRecord.get(0)));
                    for(int i=1; i<csvRecord.size(); i++){
                        cells[row-1][i-1] = new Cell(csvRecord.get(i).equals("t"), row-1, i-1, csvRecord.get(i).equals("Z"));
                    }
                }
                row++;
            }
        }
        results[0] = cells;
        results[1] = columnsTents;
        results[2] = rowTents;

        return results;
    }

}