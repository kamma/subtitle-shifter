package cz.kamma.subtitle.shifter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.kamma.subtitle.shifter.ui.ProgressBarUpdator;
import cz.kamma.subtitle.shifter.ui.ShifterApp;

public class ShifterEngine {

  ArrayList<SubtitleLine> lines, origLines;
  int searchIndex = 0;
  String encodingDetected = null;
  ShifterApp shifterApp;

  public ShifterEngine(ShifterApp shifterApp) {
    this.shifterApp = shifterApp;
  }

  private byte[] openFile(String filename) throws Exception {
    FileInputStream fis = new FileInputStream(filename);
    byte[] file = new byte[fis.available()];
    fis.read(file);
    fis.close();
    byte[] res = null;
    // UTF-8
    if ((file[0] == (byte) 0xEF) && (file[1] == (byte) 0xBB) && (file[2] == (byte) 0xBF)) {
      encodingDetected = "UTF-8";
      int offset = 3;
      res = new byte[file.length - offset];
      for (int i = 0; i < file.length - offset; i++) {
        res[i] = file[i + offset];
      }
      // UTF-16BE
    } else if ((file[0] == (byte) 0xFE) && (file[1] == (byte) 0xFF)) {
      encodingDetected = "UTF-16BE";
      int offset = 2;
      res = new byte[file.length - offset];
      for (int i = 0; i < file.length - offset; i++) {
        res[i] = file[i + offset];
      }
      // UTF-16LE
    } else if ((file[0] == (byte) 0xFF) && (file[1] == (byte) 0xFE)) {
      encodingDetected = "UTF-16LE";
      int offset = 2;
      res = new byte[file.length - offset];
      for (int i = 0; i < file.length - offset; i++) {
        res[i] = file[i + offset];
      }
      // UTF-32BE
    } else if ((file[0] == (byte) 0x00) && (file[1] == (byte) 0x00) && (file[2] == (byte) 0xFE) && (file[3] == (byte) 0xFF)) {
      encodingDetected = "UTF-32BE";
      int offset = 4;
      res = new byte[file.length - offset];
      for (int i = 0; i < file.length - offset; i++) {
        res[i] = file[i + offset];
      }
      // UTF-32LE
    } else if ((file[0] == (byte) 0xFF) && (file[1] == (byte) 0xFE) && (file[2] == (byte) 0x00) && (file[3] == (byte) 0x00)) {
      encodingDetected = "UTF-32LE";
      int offset = 4;
      res = new byte[file.length - offset];
      for (int i = 0; i < file.length - offset; i++) {
        res[i] = file[i + offset];
      }
    }
    if (res != null) {
      if (shifterApp != null) {
        shifterApp.setEncoding(encodingDetected);
      }
      return res;
    }
    return file;
  }

  public void readFile(String filename, String encoding, String format) throws Exception {
    origLines = new ArrayList<SubtitleLine>();
    lines = new ArrayList<SubtitleLine>();
    Reader reader = new InputStreamReader(new ByteArrayInputStream(openFile(filename)), encodingDetected != null ? encodingDetected : encoding);
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
      origLines.add(sl);
      lines.add(sl.clone());
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

  public List<SubtitleLine> getLines() {
    return lines;
  }

  public SubtitleLine[] getLinesAsArray() {
    return lines.toArray(new SubtitleLine[0]);
  }

  public List<SubtitleLine> getOrigLines() {
    return origLines;
  }

  public SubtitleLine[] getOrigLinesAsArray() {
    return origLines.toArray(new SubtitleLine[0]);
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

  public String translateTextWithGoogleAPIs(String line, String srcLang, String trgLang) throws Exception {
    URL url = new URL("http://translate.googleapis.com/translate_a/single?client=gtx&sl=" + srcLang + "&tl=" + trgLang + "&dt=t&q=" + URLEncoder.encode(line, "UTF-8"));
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.addRequestProperty("Host", "translate.googleapis.com");
    con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");

    int rc = con.getResponseCode();

    if (rc != 200) {
      throw new Exception("Cannot connect to Google translate. (HTTP " + rc + ")");
    }

    String response = "";

    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
    while ((line = br.readLine()) != null) {
      response += line;
    }
    
    Pattern p = Pattern.compile("\"([^\"]*)\"");
    Matcher m = p.matcher(response);
    while (m.find()) {
        return m.group(1);
    }

    return null;
  }

  public static void main(String[] args) throws Exception {
    ShifterEngine e = new ShifterEngine(null);
    e.translateTextWithGoogleAPIs("just+test", "en", "cs");
  }

  public Runnable translateLine(SubtitleLine sl, ProgressBarUpdator ju) throws Exception {
    return new Runnable() {
      @Override
      public void run() {
        try {
          String orig = sl.getText();
          String trans = translateTextWithGoogleAPIs(orig, "en", "cs");
          if (trans!=null && trans.length()>0)
            sl.setText(trans);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
        ju.addValue();
      }
    };
  }
}
