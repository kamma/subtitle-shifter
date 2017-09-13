package cz.kamma.subtitle.shifter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ShifterEngine {

  ArrayList<SubtitleLine> lines;
  int searchIndex = 0;

  private byte[] openFile(String filename) throws Exception {
    FileInputStream fis = new FileInputStream(filename);
    byte[] file = new byte[fis.available()];
    fis.read(file);
    fis.close();
    return file;
  }

  public void readFile(String filename, String encoding, String format) throws Exception {
    lines = new ArrayList<SubtitleLine>();
    Reader reader = new InputStreamReader(new ByteArrayInputStream(openFile(filename)), encoding);
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

  public void applyShiftMillis(int shift, int index, boolean after) {
    if (after) {
      for (int i = index; i < lines.size(); i++) {
        SubtitleLine sl = lines.get(i);
        sl.applyShiftMillis(shift);
      }
    } else {
      for (int i = index; i >= 0; i--) {
        SubtitleLine sl = lines.get(i);
        sl.applyShiftMillis(shift);
      }
    }
  }

  public void applyShiftMillis(int shift, int[] selectedIndices) {
    for (int i : selectedIndices) {
      SubtitleLine sl = lines.get(i);
      sl.applyShiftMillis(shift);
    }
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

    for (int i = searchIndex; i < lines.size(); i++) {
      searchIndex++;
      SubtitleLine sl = lines.get(i);
      if (sl.contains(searchStr))
        return i;
    }

    searchIndex = 0;
    return -1;
  }

  public void insertSubtitleLine(int index, SubtitleLine sl) {
    int orig = lines.get(index).getLineNum();
    sl.setLineNum(orig);
    for (int i = index; i < lines.size(); i++) {
      lines.get(i).incLineNum();
    }
    lines.add(index, sl);
  }

  public void deleteSubtitle(int index) {
    for (int i = index; i < lines.size(); i++) {
      lines.get(i).decLineNum();
    }
    lines.remove(index);
  }

  public void translateLine(int lineNum, String srcLang, String destLang) throws Exception {
    String line = lines.get(lineNum).getText();
    String res = translateTextWithGoogleAPIs(line, srcLang, destLang);
  }

  public String translateTextWithGoogleAPIs(String line, String srcLang, String trgLang) throws Exception {
    URL url = new URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl="+srcLang+"&tl="+trgLang+"&dt=t&q="+line);
    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.setDoInput(false);
    con.setDoOutput(true);
    con.setRequestMethod("GET");
    
    int rc = con.getResponseCode();
    
    String response = "";
    
    BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
    while ((line=br.readLine()) != null) {
        response+=line;
    }
    
    return response;
  }
  
  public static void main(String[] args) throws Exception {
    ShifterEngine e = new ShifterEngine();
    e.translateTextWithGoogleAPIs("aa", "aa", "aa");
    /*
    String filename = args[0];
    String encoding = args[1];
    String format = args[2];
    int shift = Integer.parseInt(args[3]);
    ShifterEngine a = new ShifterEngine();
    a.readFile(filename, encoding, format);
    a.applyShiftMillis(shift, 0, true);
    a.writeFile(filename + ".new", encoding, format);
    */
  }
}
