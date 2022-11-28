package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeMap;
import utils.Constant;

public class SlangManager {

  static TreeMap<String, ArrayList<String>> slangMap = new TreeMap<String, ArrayList<String>>();
  static DatasourceManager datasourceManager = new DatasourceManager();

  public void addSlang(String slang, ArrayList<String> meaning) {
    if (slangMap.containsKey(slang)) {
      System.out.println("Slang word: " + slang + " already exists");
    } else {
      slangMap.put(slang, meaning);
      String data = slang + " : " + String.join(", ", meaning);
      datasourceManager.writeData(Constant.USER_SLANG_DATASOURCE, data);
    }
  }

  public void removeSlang(String slang) {
    slangMap.remove(slang);
  }

  public void logHistory(Constant.SlangType type, String data) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
      Constant.TIME_LOG_FORMAT
    );
    LocalDateTime now = LocalDateTime.now();
    String timeLog = '[' + dtf.format(now) + ']' + " ----- ";

    switch (type) {
      case SLANG:
        String meaning = "";
        for (String m : getMeaning(data)) {
          meaning += m + ", ";
        }
        String historySlang = timeLog + data + ":" + meaning;
        datasourceManager.writeData(Constant.SLANG_HISTORY_PATH, historySlang);
        break;
      case MEANING:
        datasourceManager.writeData(Constant.SLANG_HISTORY_PATH, data);
        break;
      case KEYWORD:
        String historyKeyWord = timeLog + data;
        datasourceManager.writeData(
          Constant.KEYWORD_HISTORY_PATH,
          historyKeyWord
        );
        break;
      default:
        break;
    }
  }

  public ArrayList<String> getMeaning(String slang) {
    return slangMap.get(slang);
  }

  public ArrayList<String> searchBySlang(String keyword) {
    ArrayList<String> result = new ArrayList<String>();
    for (String slang : slangMap.keySet()) {
      if (slang.contains(keyword)) {
        result.add(slang);
      }
    }
    logHistory(Constant.SlangType.SLANG, keyword);
    return result;
  }

  public ArrayList<String> searchByKeyword(String keyword) {
    try {
      ArrayList<String> slangList = new ArrayList<String>();
      for (String slang : slangMap.keySet()) {
        ArrayList<String> meaningOfSlang = getMeaning(slang);
        for (String meaning : meaningOfSlang) {
          if (meaning.contains(keyword)) {
            slangList.add(slang);
          }
        }
      }
      logHistory(Constant.SlangType.KEYWORD, keyword);
      return slangList;
    } catch (Exception e) {
      return null;
    }
  }

  public void setUserSlang() {
    datasourceManager.copyFile(
      Constant.DATASOURCE,
      Constant.USER_SLANG_DATASOURCE
    );
  }

  public void loadSlangs() {
    ArrayList<String> data = new ArrayList<String>();
    if (datasourceManager.checkFileExist(Constant.USER_SLANG_DATASOURCE)) {
      System.out.println("User slang file exists");
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    } else {
      setUserSlang();
      System.out.println("User slang file not exists");

      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    }

    for (int i = 0; i < data.size(); i++) {
      String[] slang = data.get(i).split("`");
      if (slang.length == 2) {
        String slangName = slang[0];
        String[] slangMeaningArray = slang[1].split("\\| ");
        ArrayList<String> slangMeaning = new ArrayList<String>();
        for (int j = 0; j < slangMeaningArray.length; j++) {
          slangMeaning.add(slangMeaningArray[j]);
        }
        addSlang(slangName, slangMeaning);
      }
    }
  }

  public void showHistory() {
    ArrayList<String> slangHistoryList = datasourceManager.getData(
      Constant.SLANG_HISTORY_PATH
    );
    ArrayList<String> keywordHistoryList = datasourceManager.getData(
      Constant.KEYWORD_HISTORY_PATH
    );

    System.out.println("Slang History:");
    for (String slang : slangHistoryList) {
      System.out.println(slang);
    }
    System.out.println("Keyword History:");
    for (String keyword : keywordHistoryList) {
      System.out.println(keyword);
    }
  }
}
