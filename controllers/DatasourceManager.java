package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DatasourceManager {


  public ArrayList<String> getData(String filePath) {
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

  public void writeData(String filePath, String data) {
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        file.createNewFile();
      }

      FileWriter fileWriter = new FileWriter(file, true);
      // write data in the new line
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(data);
      bufferedWriter.newLine();
      bufferedWriter.close();

    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
