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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;

public class ShifterApp {

	private JFrame frmSubtitleshifter;
	private JPanel panel;
	private JTextArea textArea;
	private JPanel panel_1;
	private JTextField shiftTF;
	private JButton applyShiftBtn;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmSave;
	private JMenuItem mntmExit;
	private ShifterEngine app;
	private JScrollPane scrollPane;

	final JFileChooser fc = new JFileChooser();

	ArrayList<SubtitleLine> lines;
	private JPanel panel_2;
	private JLabel lblNewLabel;
	private JComboBox formatCB;
	private JLabel lblNewLabel_1;
	private JComboBox encodingCB;
	private JPanel panel_3;
	private JPanel panel_4;

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
		
		panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		scrollPane.setColumnHeaderView(panel_2);
		
		panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		panel_2.add(panel_3);
		
		lblNewLabel = new JLabel("Subtitle format");
		panel_3.add(lblNewLabel);
		
		formatCB = new JComboBox();
		panel_3.add(formatCB);
		formatCB.setModel(new DefaultComboBoxModel(new String[] {"Srt"}));
		
		panel_4 = new JPanel();
		panel_2.add(panel_4);
		
		lblNewLabel_1 = new JLabel("Encoding");
		panel_4.add(lblNewLabel_1);
		
		encodingCB = new JComboBox();
		panel_4.add(encodingCB);
		encodingCB.setModel(new DefaultComboBoxModel(new String[] {"CP1250", "CP1252", "UTF-8"}));

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		shiftTF = new JTextField();
		panel_1.add(shiftTF);
		shiftTF.setColumns(10);

		applyShiftBtn = new JButton("Apply Shift");
		applyShiftBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String shiftStr = shiftTF.getText();
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
		panel_1.add(applyShiftBtn);

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
						app.readFile(file.getAbsolutePath(), encodingCB.getSelectedItem().toString(), formatCB.getSelectedItem().toString());
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
						app.writeFile(file.getAbsolutePath(), encodingCB.getSelectedItem().toString(), formatCB.getSelectedItem().toString());
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
