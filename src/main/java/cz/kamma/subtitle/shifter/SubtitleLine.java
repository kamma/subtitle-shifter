package cz.kamma.subtitle.shifter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubtitleLine {
	
	int lineNum;
	String timeFromTo;
	String text;
	Date timeFrom;
	Date timeTo;
	
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
	
	public SubtitleLine() {
	}
	
	public SubtitleLine(int lineNum, String timeFromTo, String text) {
		super();
		this.lineNum = lineNum;
		this.timeFromTo = timeFromTo;
		this.text = text;
	}
	
	public int getLineNum() {
		return lineNum;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	public String getTimeFromTo() {
		return timeFromTo;
	}
	public void setTimeFromTo(String timeFromTo) throws Exception {
		this.timeFromTo = timeFromTo;
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
		return lineNum+"\n"+sdf.format(timeFrom)+" --> "+sdf.format(timeTo)+"\n"+text+"\n";
	}

	public String toString() {
		return lineNum+"\n"+sdf.format(timeFrom)+"\n"+sdf.format(timeTo)+"\n"+text;
	}

	public void applyShiftMillis(int shift) {
		long time = timeFrom.getTime();
		time = time + shift;
		timeFrom.setTime(time);
		time = timeTo.getTime();
		time = time + shift;
		timeTo.setTime(time);
	}
}
