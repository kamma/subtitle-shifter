package cz.kamma.subtitle.shifter;

import java.nio.charset.Charset;

public class Constants {
  public static String FORMAT_TYPE_SRT = "SRT";
  public static String TIME_DELIMITTER_SRT = "-->";

  public static String[] SUBTITLE_FORMATS = new String[] { FORMAT_TYPE_SRT };
  public static String[] ENCODING_TYPES = Charset.availableCharsets().keySet().toArray(new String[0]);
  public static String DEFAULT_CHARSET = "windows-1250";
}
