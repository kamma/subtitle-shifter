package cz.kamma.subtitle.shifter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class ShifterEngine {

	ArrayList<SubtitleLine> lines = new ArrayList<SubtitleLine>();
	
	public void readFile(String filename, String encoding, String format) throws Exception {
		Reader reader = new InputStreamReader(new FileInputStream(filename), encoding);
		BufferedReader br = new BufferedReader(reader);
		
		while (br.ready()) {
			String num = br.readLine();
			String time = br.readLine();
			String line = br.readLine();
			String text = "";
			while (line!=null && !"".equals(line)) {
				text += line.trim()+"\n";
				line = br.readLine();
			}
			
			SubtitleLine sl = new SubtitleLine();
			sl.setLineNum(Integer.parseInt(num));
			sl.setTimeFromTo(time);
			sl.setText(text);
			
			//System.out.println(sl.getLineFormated("srt"));
			lines.add(sl);
		}

		br.close();
	}

	public void writeFile(String filename, String encoding, String format) throws Exception {
		Writer writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

		for (SubtitleLine sl:lines) {
			writer.write(sl.getLineFormated(format));
		}
		
		writer.flush();
		writer.close();
	}
	
	public void applyShiftMillis(int shift) {
		for (SubtitleLine sl:lines) {
			sl.applyShiftMillis(shift);
		}
	}

	public static void main(String[] args) throws Exception {
		String filename = args[0];
		String encoding = args[1];
		String format = args[2];
		int shift = Integer.parseInt(args[3]);
		ShifterEngine a = new ShifterEngine();
		a.readFile(filename, encoding, format);
		a.applyShiftMillis(shift);
		a.writeFile(filename+".new", encoding, format);
	}

	public String getFileText(String format) {
		String tmp = "";
		for (SubtitleLine sl:lines) {
			tmp += sl.getLineFormated(format);
		}
		return tmp;
	}

	public boolean isFileOpen() {
		return lines!=null && !lines.isEmpty();
	}
}
