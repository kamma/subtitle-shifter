package cz.kamma.subtitle.shifter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ShifterEngine {

  byte[] file;
  ArrayList<SubtitleLine> lines;
  int searchIndex = 0;

  public void openFile(String filename) throws Exception {
    FileInputStream fis = new FileInputStream(filename);
    file = new byte[fis.available()];
    fis.read(file);
    fis.close();
  }

  public void readFile(String encoding, String format) throws Exception {
    lines = new ArrayList<SubtitleLine>();
    Reader reader = new InputStreamReader(new ByteArrayInputStream(file), encoding);
    BufferedReader br = new BufferedReader(reader);

    while (br.ready()) {
      String num = br.readLine();
      String time = br.readLine();
      String line = br.readLine();
      String text = "";
      while (line != null && !"".equals(line)) {
        text += line.trim() + "\n";
        line = br.readLine();
      }

      SubtitleLine sl = new SubtitleLine();
      sl.setLineNum(Integer.parseInt(num));
      sl.setTimeFromTo(time);
      sl.setText(text);

      // System.out.println(sl.getLineFormated("srt"));
      lines.add(sl);
    }

    br.close();
  }

  public void writeFile(String filename, String encoding, String format) throws Exception {
    Writer writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

    for (SubtitleLine sl : lines) {
      writer.write(sl.getLineFormated(format));
    }

    writer.flush();
    writer.close();
  }

  public void applyShiftMillis(int shift) {
    for (SubtitleLine sl : lines) {
      sl.applyShiftMillis(shift);
    }
  }

  public static void main(String[] args) throws Exception {
    String filename = args[0];
    String encoding = args[1];
    String format = args[2];
    int shift = Integer.parseInt(args[3]);
    ShifterEngine a = new ShifterEngine();
    a.openFile(filename);
    a.readFile(encoding, format);
    a.applyShiftMillis(shift);
    a.writeFile(filename + ".new", encoding, format);
  }

  public String getFileText(String format) {
    String tmp = "";
    for (SubtitleLine sl : lines) {
      tmp += sl.getLineFormated(format);
    }
    return tmp;
  }

  public boolean isFileOpen() {
    return lines != null && !lines.isEmpty();
  }
  
  public SubtitleLine[] getLinesAsArray() {
    return lines.toArray(new SubtitleLine[0]);
  }

  public List<SubtitleLine> getLines() {
    return lines;
  }

  public int search(String searchStr) {

    for (int i=searchIndex;i<lines.size();i++) {
      searchIndex++;
      SubtitleLine sl = lines.get(i);
      if (sl.contains(searchStr))
        return i;
    }
    
    searchIndex = 0;
    return -1;
  }
}
