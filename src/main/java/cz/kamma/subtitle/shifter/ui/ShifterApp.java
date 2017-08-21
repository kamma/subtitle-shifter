package cz.kamma.subtitle.shifter.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cz.kamma.subtitle.shifter.ShifterEngine;
import cz.kamma.subtitle.shifter.SubtitleLine;

public class ShifterApp {

	private JFrame frmSubtitleshifter;
	private JPanel panel;
	private JTextArea textArea;
	private JPanel panel_1;
	private JTextField textField;
	private JButton btnNewButton;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmSave;
	private JMenuItem mntmExit;
	private ShifterEngine app;
	private JScrollPane scrollPane;

	final JFileChooser fc = new JFileChooser();

	ArrayList<SubtitleLine> lines;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShifterApp window = new ShifterApp();
					window.frmSubtitleshifter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ShifterApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		app = new ShifterEngine();
		frmSubtitleshifter = new JFrame();
		frmSubtitleshifter.setTitle("SubtitleShifter");
		frmSubtitleshifter.setBounds(100, 100, 532, 400);
		frmSubtitleshifter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSubtitleshifter.getContentPane()
				.setLayout(new BoxLayout(frmSubtitleshifter.getContentPane(), BoxLayout.X_AXIS));

		panel = new JPanel();
		frmSubtitleshifter.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		panel.add(scrollPane, BorderLayout.CENTER);

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);

		btnNewButton = new JButton("Apply Shift");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String shiftStr = textField.getText();
				int shift = 0;
				try {
					shift = Integer.parseInt(shiftStr);
					app.applyShiftMillis(shift);
					textArea.setText(app.getFileText("srt"));
					textArea.setCaretPosition(0);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel_1.add(btnNewButton);

		menuBar = new JMenuBar();
		frmSubtitleshifter.setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmNewMenuItem = new JMenuItem("Open");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(frmSubtitleshifter);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						app.readFile(file.getAbsolutePath(), "cp1250", "srt");
						textArea.setText(app.getFileText("srt"));
						textArea.setCaretPosition(0);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmNewMenuItem);

		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(frmSubtitleshifter);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						app.writeFile(file.getAbsolutePath(), "cp1250", "srt");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmSave);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSubtitleshifter.dispose();
			}
		});
		mnFile.add(mntmExit);
	}

}
