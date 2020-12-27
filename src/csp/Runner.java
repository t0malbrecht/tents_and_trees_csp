package csp;

import services.CSVReader;

import java.io.IOException;

public class Runner {
    public static void main(String[] args){
        Object[] results;
        try {
            CSVReader.setCsvFilePath(0);
            results = CSVReader.read();
            System.out.println(results[0]);
            System.out.println(results[1]);
            System.out.println(results[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
