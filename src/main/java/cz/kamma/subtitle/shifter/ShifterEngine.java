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

public class ShifterEngine {

	ArrayList<SubtitleLine> lines;
	ArrayList<SubtitleLine> transLines;
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
		URL url = new URL("http://translate.googleapis.com/translate_a/single?client=gtx&sl=" + srcLang + "&tl="
				+ trgLang + "&dt=t&text=" + URLEncoder.encode(line, "UTF-8"));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
		con.setRequestProperty("Host", "translate.googleapis.com");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");

		con.setDoInput(true);

		con.connect();

		StringBuffer response = new StringBuffer();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		while ((line = br.readLine()) != null) {
			response.append(line);
		}
		
		String res = response.substring(response.indexOf("[[[\"")+4, response.indexOf("\",\""));

		return res;
	}

	public static void main(String[] args) throws Exception {
		ShifterEngine e = new ShifterEngine();
		System.out.println(e.translateTextWithGoogleAPIs("<i>hello my \"good\", friend !!!</i>", "en", "cs"));
		/*
		 * String filename = args[0]; String encoding = args[1]; String format =
		 * args[2]; int shift = Integer.parseInt(args[3]); ShifterEngine a = new
		 * ShifterEngine(); a.readFile(filename, encoding, format);
		 * a.applyShiftMillis(shift, 0, true); a.writeFile(filename + ".new", encoding,
		 * format);
		 */
	}

	public void translateAll() throws Exception {
		transLines = new ArrayList<SubtitleLine>();
		for (SubtitleLine line:lines) {
			String[] linesStr = line.getText().split("\n");
			String resLine = "";
			for (String l:linesStr) {
				String res = translateTextWithGoogleAPIs(l, "en", "cs");
				resLine = resLine.concat(res).concat("\n");
			}
			transLines.add(new SubtitleLine(line.getLineNum(), line.getTimeFromTo(), resLine));
			if (line.getLineNum()>100)
				break;
		}
	}

	public SubtitleLine[] getTransLinesAsArray() {
		return transLines.toArray(new SubtitleLine[0]);
	}
}
