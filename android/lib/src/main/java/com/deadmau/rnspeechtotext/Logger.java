package com.deadmau.rnspeechtotext;

public class Logger {

  public enum LogLevel {
    DEBUG,
    INFO,
    ERROR,
    OFF
  }

  public interface LoggerDelegate {
    void error(String tag, String message);
    void error(String tag, String message, Throwable exception);
    void debug(String tag, String message);
    void info(String tag, String message);
  }

  private LogLevel mLogLevel = BuildConfig.DEBUG ? LogLevel.DEBUG : LogLevel.OFF;

  private LoggerDelegate mDelegate = new DefaultLogger();

  private Logger() { }

  private static class SingletonHolder {
    private static final Logger instance = new Logger();
  }

  public static void resetLoggerDelegate() {
    synchronized (Logger.class) {
      SingletonHolder.instance.mDelegate = new DefaultLogger();
    }
  }

  public static void setLoggerDelegate(LoggerDelegate delegate) {
    if (delegate == null)
      throw new IllegalArgumentException("delegate MUST not be null!");

    synchronized (Logger.class) {
      SingletonHolder.instance.mDelegate = delegate;
    }
  }

  public static void setLogLevel(LogLevel level) {
    synchronized (Logger.class) {
      SingletonHolder.instance.mLogLevel = level;
    }
  }

  public static void error(String tag, String message) {
    if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.ERROR) <= 0) {
      SingletonHolder.instance.mDelegate.error(tag, message);
    }
  }

  public static void error(String tag, String message, Throwable exception) {
    if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.ERROR) <= 0) {
      SingletonHolder.instance.mDelegate.error(tag, message, exception);
    }
  }

  public static void info(String tag, String message) {
    if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.INFO) <= 0) {
      SingletonHolder.instance.mDelegate.info(tag, message);
    }
  }

  public static void debug(String tag, String message) {
    if (SingletonHolder.instance.mLogLevel.compareTo(LogLevel.DEBUG) <= 0) {
      SingletonHolder.instance.mDelegate.debug(tag, message);
    }
  }
}
