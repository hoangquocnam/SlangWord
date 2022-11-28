package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DatasourceManager {

  public ArrayList<String> getData(String filePath) {
    try {
      BufferedReader bufferedReader = new BufferedReader(
        new FileReader(filePath)
      );
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

  public void copyFile(String source, String dest) {
    try {
      File sourceFile = new File(source);
      File destFile = new File(dest);

      BufferedReader bufferedReader = new BufferedReader(
        new FileReader(sourceFile)
      );
      BufferedWriter bufferedWriter = new BufferedWriter(
        new FileWriter(destFile)
      );

      String line = bufferedReader.readLine();
      while (line != null) {
        bufferedWriter.write(line);
        bufferedWriter.newLine();
        line = bufferedReader.readLine();
      }

      bufferedReader.close();
      bufferedWriter.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public boolean checkFileExist(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
      try {
        file.createNewFile();
        return false;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  public boolean addDefinition(
    String filePath,
    String slang,
    String data,
    boolean isOverwrite
  ) {
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        file.createNewFile();
      }

      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      String line = bufferedReader.readLine();
      String fileContent = "";
      while (line != null) {
        String newLine = "";
        if (line.contains(slang)) {
          if (!isOverwrite) {
            newLine = line + "| " + data;
          }
          else {
            newLine = slang + "`" + data;
          }
        } else {
          newLine = line;
        }
        fileContent+= newLine;
        fileContent += System.lineSeparator();
        line = bufferedReader.readLine();
      }
      bufferedReader.close();

      BufferedWriter bufferedWriter = new BufferedWriter(
        new FileWriter(filePath)
      );
      bufferedWriter.write(fileContent);
      bufferedWriter.close();
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
    return true;
  }
}
