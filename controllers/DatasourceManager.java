package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DatasourceManager {


  public ArrayList<String> getData(String filePath) {
    // get data from file
    // return data
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
      ArrayList<String> data = new ArrayList<String>();

      String line = bufferedReader.readLine();
      while (line != null) {
        data.add(line);
        line = bufferedReader.readLine();
      }
      bufferedReader.close();
      return data;
    } catch (Exception e) {
      System.out.println(e);
    }

    return null;
  }
}
