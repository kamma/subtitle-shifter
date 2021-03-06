package cz.kamma.subtitle.shifter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubtitleLine {

  private int lineNum;
  private String text;
  private Date timeFrom;
  private Date timeTo;

  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");

  public SubtitleLine() {
  }

  public SubtitleLine(int lineNum, String timeFromTo, String text) throws Exception {
    this.lineNum = lineNum;
    this.text = text;
    
    setTimeFromTo(timeFromTo);
  }
  
  public SubtitleLine clone() {
    SubtitleLine sl = new SubtitleLine();
    sl.setLineNum(lineNum);
    sl.setText(text);
    sl.setTimeFrom(timeFrom);
    sl.setTimeTo(timeTo);
    return sl;
  }

  private void setTimeTo(Date timeTo2) {
    timeTo = new Date(timeTo2.getTime());
  }

  private void setTimeFrom(Date timeFrom2) {
    timeFrom = new Date(timeFrom2.getTime());
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
    return text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>");
  }

  public String getTimeFromTo() {
    return sdf.format(timeFrom) + " --> " + sdf.format(timeTo);
  }

  public boolean contains(String searchStr) {
    return toString().contains(searchStr);
  }

  public void setTimeFrom(String time) throws Exception {
    timeFrom = sdf.parse(time);
  }
  
  public void setTimeTo(String time) throws Exception {
    timeTo = sdf.parse(time);
  }

  public String getTimeFromFormatted() {
    return sdf.format(timeFrom);
  }
  
  public String getTimeToFormatted() {
    return sdf.format(timeTo);
  }

  public void incLineNum() {
    lineNum++;    
  }
  
  public void decLineNum() {
    lineNum--;    
  }
  
}
