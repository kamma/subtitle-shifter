package cz.kamma.subtitle.shifter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubtitleLine {

  int lineNum;
  String text;
  Date timeFrom;
  Date timeTo;

  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");

  public SubtitleLine() {
  }

  public SubtitleLine(int lineNum, String timeFromTo, String text) throws Exception {
    super();
    this.lineNum = lineNum;
    this.text = text;
    setTimeFromTo(timeFromTo);
  }

  public int getLineNum() {
    return lineNum;
  }

  public void setLineNum(int lineNum) {
    this.lineNum = lineNum;
  }

  public void setTimeFromTo(String timeFromTo) throws Exception {
    String[] parts = timeFromTo.split(Constants.TIME_DELIMITTER_SRT);
    this.timeFrom = sdf.parse(parts[0].trim());
    this.timeTo = sdf.parse(parts[1].trim());
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getLineFormated(String format) {
    return lineNum + "\n" + getTimeFromTo() + "\n" + text + "\n";
  }

  public String toString() {
    return lineNum + "\n" + sdf.format(timeFrom) + "\n" + sdf.format(timeTo) + "\n" + text;
  }

  public void applyShiftMillis(int shift) {
    long time = timeFrom.getTime();
    time = time + shift;
    timeFrom.setTime(time);
    time = timeTo.getTime();
    time = time + shift;
    timeTo.setTime(time);
  }

  public String getTextAsHTML() {
    return text.replaceAll("\n", "<br/>");
  }

  public String getTimeFromTo() {
    return sdf.format(timeFrom) + " --> " + sdf.format(timeTo);
  }
}
