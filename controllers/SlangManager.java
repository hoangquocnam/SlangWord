package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import utils.Constant;

public class SlangManager {

  public class Slang {
    private String slang;
    private ArrayList<String> meaning;

    public Slang(String slang, ArrayList<String> meaning) {
      this.slang = slang;
      this.meaning = meaning;
    }

    public String getSlang() {
      return slang;
    }

    public void setSlang(String slang) {
      this.slang = slang;
    }

    public ArrayList<String> getMeaning() {
      return meaning;
    }

    public void setMeaning(ArrayList<String> meaning) {
      this.meaning = meaning;
    }
  }

  static TreeMap<String, ArrayList<String>> slangMap = new TreeMap<String, ArrayList<String>>();
  static TreeMap<String, ArrayList<String>> historyMap = new TreeMap<String, ArrayList<String>>();
  static DatasourceManager datasourceManager = new DatasourceManager();


  public ArrayList<String> getMeaning(String slang) {
    return slangMap.get(slang);
  }

  public void setUserSlang() {
    datasourceManager.copyFile(
      Constant.DATASOURCE,
      Constant.USER_SLANG_DATASOURCE
    );
  }

  public void setSlangList(ArrayList<String> slangList) {
    for (int i = 0; i < slangList.size(); i++) {
      String[] slang = slangList.get(i).split("`");
      if (slang.length == 2) {
        String slangName = slang[0];
        String[] slangMeaningArray = slang[1].split("\\| ");
        ArrayList<String> slangMeaning = new ArrayList<String>();
        for (int j = 0; j < slangMeaningArray.length; j++) {
          slangMeaning.add(slangMeaningArray[j]);
        }
        slangMap.put(slangName, slangMeaning);
      }
    }
  }

  public void loadHistory() {
    ArrayList<String> historyLog = datasourceManager.getData(
      Constant.SLANG_HISTORY_PATH
    );

    
    for (String log : historyLog) {
      ArrayList<String> history = new ArrayList<String>();
      String slangHistory = log.split(Constant.LOG_SEPARATOR_TIME_CONTENT)[1];
      String slang = slangHistory.split(
        Constant.LOG_SEPARATOR_SLANG_DEFINITION
      )[0];
      String meanings = slangHistory.split(
        Constant.LOG_SEPARATOR_SLANG_DEFINITION
      )[1];

      String[] meaningArray = meanings.split(
        Constant.LOG_SEPARATOR_DEFINITION_DEFINITION
        );
      for (String meaning : meaningArray) {
        history.add(meaning);
      }

      historyMap.put(slang, history);
    }
  }

  public void loadSlangs() {
    ArrayList<String> data = new ArrayList<String>();
    if (datasourceManager.checkFileExist(Constant.USER_SLANG_DATASOURCE)) {
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    } else {
      setUserSlang();
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    }

    setSlangList(data);
    loadHistory();
  }

  public void logHistory(Constant.SlangType type, String data) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
      Constant.TIME_LOG_FORMAT
    );
    LocalDateTime now = LocalDateTime.now();
    String timeLog =
      '[' + dtf.format(now) + ']' + Constant.LOG_SEPARATOR_TIME_CONTENT;

    switch (type) {
      case SLANG:
        String meaning = "";
        if (getMeaning(data) != null) {
          for (String m : getMeaning(data)) {
            if (m == getMeaning(data).get(0)) {
              meaning += m;
            } else {
              meaning += Constant.LOG_SEPARATOR_DEFINITION_DEFINITION + m;
            }
          }
          String historySlang =
            timeLog + data + Constant.LOG_SEPARATOR_SLANG_DEFINITION + meaning;
          datasourceManager.writeData(
            Constant.SLANG_HISTORY_PATH,
            historySlang
          );
        }

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

  public void showHistory(Constant.SlangType type) {
    if (type == null) {
      return;
    }
    switch (type) {
      case SLANG:
        for (String slang : historyMap.keySet()) {
          System.out.println(slang);
          for (String meaning : historyMap.get(slang)) {
            System.out.println(meaning);
          }
        }
        break;
      case KEYWORD:
        ArrayList<String> keywordLog = datasourceManager.getData(
          Constant.KEYWORD_HISTORY_PATH
        );
        for (String log : keywordLog) {
          System.out.println(log);
        }
        break;
      default:
        break;
    }
  }

  public boolean addDefinition(String slang, String meaning) {
    try {
      ArrayList<String> meaningList = new ArrayList<String>();
      meaningList.add(meaning);
      slangMap.put(slang, meaningList);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean addSlang(String slang, ArrayList<String> meaning) {
    try {
      if (slangMap.containsKey(slang)) {
        ArrayList<String> oldMeaning = slangMap.get(slang);
        String meaningText = "";

        System.out.println("Slang word: " + slang + " already exists");
        System.out.println("Do you want to update the meaning? (Y/N)");
        //TODO: handle with UI later - get text from UI
        String answer = System.console().readLine();

        if (
          answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes")
        ) {
          for (String m : meaning) {
            if (m != meaning.get(meaning.size() - 1)) {
              meaningText += m + "| ";
            } else {
              meaningText += m;
            }
          }
          slangMap.put(slang, meaning);
          datasourceManager.addDefinition(
            Constant.USER_SLANG_DATASOURCE,
            slang,
            meaningText,
            true
          );
        } else {
          for (String m : meaning) {
            oldMeaning.add(m);
            if (m != meaning.get(meaning.size() - 1)) {
              meaningText += m + "| ";
            } else {
              meaningText += m;
            }
          }

          slangMap.put(slang, oldMeaning);
          datasourceManager.addDefinition(
            Constant.USER_SLANG_DATASOURCE,
            slang,
            meaningText,
            false
          );
        }
      } else {
        slangMap.put(slang, meaning);
        String data = slang + "`" + String.join("| ", meaning);
        datasourceManager.writeData(Constant.USER_SLANG_DATASOURCE, data);
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public void removeSlang(String slang) {
    try {
      // Ask user to confirm
      System.out.println("Are you sure you want to delete this slang? (Y/N)");
      String answer = System.console().readLine();
      if (
        answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes")
      ) {
        slangMap.remove(slang);
        datasourceManager.removeLine(Constant.USER_SLANG_DATASOURCE, slang);
      }
    } catch (Exception e) {
      System.out.println("Slang word: " + slang + " does not exist");
    }
  }

  public void resetSlang() {
    try {
      slangMap.clear();
      datasourceManager.clearFile(Constant.USER_SLANG_DATASOURCE);
      setUserSlang();
      ArrayList<String> data = new ArrayList<String>();
      setSlangList(data);
    } catch (Exception e) {
      System.out.println("Error when reset slang");
    }
  }

  public Slang getRandomSlang(){
    Random rand = new Random();
    int randomIndex = rand.nextInt(slangMap.size());
    int i = 0;
    for (String key : slangMap.keySet()) {
      if (i == randomIndex) {
        return new Slang(key, slangMap.get(key));
      }
      i++;
    }
    return null;
  }
}
