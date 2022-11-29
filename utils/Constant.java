package utils;

public final class Constant {

  private Constant() {}

  public static final String APP_NAME = "Slang Dictionary";
  public static final int APP_WIDTH = 1000;
  public static final int APP_HEIGHT = 1000;
  public static final String[] SEARCH_TYPES = { "Definition", "Slang", "All" };

  public static enum SlangType {
    SLANG,
    MEANING,
    KEYWORD,
  }

  public static String DATASOURCE = "assets/slang.txt";
  public static String USER_SLANG_DATASOURCE = "assets/userSlang.txt";

  public static String SLANG_HISTORY_PATH = "log/history/slang.txt";
  public static String KEYWORD_HISTORY_PATH = "log/history/keyword.txt";

  public static String TIME_LOG_FORMAT = "yyyy/MM/dd HH:mm:ss";

  public static String LOG_SEPARATOR_TIME_CONTENT = " ----- ";
  public static String LOG_SEPARATOR_SLANG_DEFINITION = ":";
  public static String DB_SEPARATOR_SLANG_DEFINITION = "`";
  public static String LOG_SEPARATOR_DEFINITION_DEFINITION = "\\| ";
}
