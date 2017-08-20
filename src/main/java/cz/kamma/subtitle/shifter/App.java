package cz.kamma.subtitle.shifter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class App {

	private ArrayList<SubtitleLine> readFile(String filename, String encoding, String format) throws Exception {
		ArrayList<SubtitleLine> lines = new ArrayList<SubtitleLine>();

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
		return lines;
	}

	private void writeFile(ArrayList<SubtitleLine> lines, String filename, String encoding, String format) throws Exception {
		Writer writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);

		for (SubtitleLine sl:lines) {
			writer.write(sl.getLineFormated(format));
		}
		
		writer.flush();
		writer.close();
	}
	
	private static void applyShiftMillis(ArrayList<SubtitleLine> lines, int shift) {
		for (SubtitleLine sl:lines) {
			sl.applyShiftMillis(shift);
		}
	}

	public static void main(String[] args) throws Exception {
		String filename = args[0];
		String encoding = args[1];
		String format = args[2];
		int shift = Integer.parseInt(args[3]);
		App a = new App();
		ArrayList<SubtitleLine> lines = a.readFile(filename, encoding, format);
		System.out.println(lines);
		applyShiftMillis(lines, shift);
		a.writeFile(lines, filename+".new", encoding, format);
	}
}
