package cz.kamma.subtitle.shifter.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import cz.kamma.subtitle.shifter.Constants;
import cz.kamma.subtitle.shifter.ShifterEngine;
import cz.kamma.subtitle.shifter.SubtitleLine;

public class ShifterApp implements MouseListener {

  private JFrame frmSubtitleshifter;
  private JPanel panel;
  private JList<SubtitleLine> subList;
  private JPanel panel_1;
  private JTextField shiftTF;
  private JButton applyShiftBtn;
  private JMenuBar menuBar;
  private JMenu mnFile;
  private JMenuItem openMenuItem;
  private JMenuItem saveMenuItem;
  private JMenuItem exitMenuItem;
  private ShifterEngine app;
  private JScrollPane scrollPane;

  final JFileChooser fc = new JFileChooser();

  ArrayList<SubtitleLine> lines;
  private JPanel panel_2;
  private JLabel lblNewLabel_1;
  private JComboBox<String> encodingCB;
  private JPanel panel_3;
  private JPanel panel_4;
  private JPanel panel_5;
  private JButton openFileBtn;
  private JButton saveFileBtn;
  private JPanel panel_6;
  private JLabel lblNewLabel;
  private JComboBox<String> targetEncCB;
  private JPanel panel_7;
  private JPanel panel_8;
  private JTextField searchTF;
  private JButton searchBtn;
  private JLabel lblNewLabel_2;
  private JPopupMenu jPopupMenu;

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

  public void mousePressed(MouseEvent e) {
    check(e);
  }

  public void mouseEntered(MouseEvent e) {
    //check(e);
  }
  
  public void mouseClicked(MouseEvent e) {
    //check(e);
  }

  public void mouseExited(MouseEvent e) {
    //check(e);
  }
  
  public void mouseReleased(MouseEvent e) {
    check(e);
  }

  public void check(MouseEvent e) {
    if (e.isPopupTrigger()) { // if the event shows the menu
      subList.setSelectedIndex(subList.locationToIndex(e.getPoint())); // select the item
      if (subList.getSelectedIndex()>=0)
        jPopupMenu.show(subList, e.getX(), e.getY()); // and show the menu
    }
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    app = new ShifterEngine();
    jPopupMenu = new JPopupMenu("Action");
    JMenuItem afterMenuItem = new JMenuItem("Apply shift after");
    JMenuItem beforeMenuItem = new JMenuItem("Apply shift before");
    afterMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        applyShifAfter();        
      }
    });
    beforeMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        applyShifBefore();        
      }
    });
    jPopupMenu.add(afterMenuItem);
    jPopupMenu.add(beforeMenuItem);
    
    frmSubtitleshifter = new JFrame();
    frmSubtitleshifter.setTitle("SubtitleShifter");
    frmSubtitleshifter.setBounds(100, 100, 850, 512);
    frmSubtitleshifter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmSubtitleshifter.getContentPane().setLayout(new BoxLayout(frmSubtitleshifter.getContentPane(), BoxLayout.X_AXIS));
    frmSubtitleshifter.setMinimumSize(new Dimension(850, 512));
    
    panel = new JPanel();
    frmSubtitleshifter.getContentPane().add(panel);
    panel.setLayout(new BorderLayout(0, 0));

    subList = new JList<>();
    subList.setCellRenderer(new MyListCellRenderer());
    subList.addMouseListener(this);
    scrollPane = new JScrollPane(subList);
    panel.add(scrollPane, BorderLayout.CENTER);

    panel_2 = new JPanel();
    FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
    flowLayout_1.setAlignment(FlowLayout.LEFT);
    scrollPane.setColumnHeaderView(panel_2);

    panel_3 = new JPanel();
    panel_2.add(panel_3);

    lblNewLabel_1 = new JLabel("Source Encoding");
    panel_3.add(lblNewLabel_1);

    encodingCB = new JComboBox<String>();
    panel_3.add(encodingCB);
    encodingCB.setModel(new DefaultComboBoxModel<String>(Constants.ENCODING_TYPES));
    encodingCB.setSelectedItem(Constants.DEFAULT_CHARSET);
    encodingCB.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        if (app.isFileOpen()) {
          reloadFile();
        }
      }
    });

    panel_4 = new JPanel();
    panel_2.add(panel_4);

    lblNewLabel = new JLabel("Target Encoding");
    panel_4.add(lblNewLabel);

    targetEncCB = new JComboBox<String>();
    targetEncCB.setModel(new DefaultComboBoxModel<String>(Constants.ENCODING_TYPES));
    targetEncCB.setSelectedItem(Constants.DEFAULT_CHARSET);
    panel_4.add(targetEncCB);

    panel_5 = new JPanel();
    panel_2.add(panel_5);

    openFileBtn = new JButton("Open Subtitles");
    openFileBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        openFileAction();
      }
    });
    panel_5.add(openFileBtn);

    panel_1 = new JPanel();
    panel.add(panel_1, BorderLayout.SOUTH);
    panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

    panel_7 = new JPanel();
    panel_1.add(panel_7);

    lblNewLabel_2 = new JLabel("Shift (ms)");
    panel_7.add(lblNewLabel_2);

    shiftTF = new JTextField();

    panel_7.add(shiftTF);
    shiftTF.setColumns(10);

    applyShiftBtn = new JButton("Apply Shift");
    panel_7.add(applyShiftBtn);

    panel_8 = new JPanel();
    panel_1.add(panel_8);

    searchTF = new JTextField();
    panel_8.add(searchTF);
    searchTF.setColumns(10);

    searchBtn = new JButton("Search / Next");
    searchBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        search();
      }
    });
    panel_8.add(searchBtn);

    panel_6 = new JPanel();
    panel_1.add(panel_6);

    saveFileBtn = new JButton("Save Subtitles");
    saveFileBtn.setEnabled(false);
    saveFileBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        saveFileAction();
      }
    });
    panel_6.add(saveFileBtn);
    applyShiftBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        shiftApplyAction(0, true);
      }
    });

    menuBar = new JMenuBar();
    frmSubtitleshifter.setJMenuBar(menuBar);

    mnFile = new JMenu("File");
    menuBar.add(mnFile);

    openMenuItem = new JMenuItem("Open");
    openMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openFileAction();
      }
    });
    mnFile.add(openMenuItem);

    saveMenuItem = new JMenuItem("Save");
    saveMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveFileAction();
      }
    });
    mnFile.add(saveMenuItem);

    exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frmSubtitleshifter.dispose();
      }
    });
    mnFile.add(exitMenuItem);
  }

  protected void applyShifBefore() {
    int index = subList.getSelectedIndex();
    shiftApplyAction(index, false);
  }

  protected void applyShifAfter() {
    int index = subList.getSelectedIndex();
    shiftApplyAction(index, true);
  }

  protected void search() {
    String searchStr = searchTF.getText();
    int index = app.search(searchStr);
    if (index < 0)
      JOptionPane.showMessageDialog(frmSubtitleshifter, "String not found", "Search result", JOptionPane.INFORMATION_MESSAGE);
    else
      subList.setSelectedIndex(index);

  }

  protected void reloadFile() {
    reloadFile(null);
  }

  protected void reloadFile(String filename) {
    try {
      if (filename != null && !"".equals(filename))
        app.openFile(filename);
      app.readFile((String) encodingCB.getSelectedItem(), Constants.FORMAT_TYPE_SRT);
      subList.setListData(app.getLinesAsArray());
      saveFileBtn.setEnabled(true);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while reading file.\nError: " + ex.getMessage(), "Cannot read file", JOptionPane.ERROR_MESSAGE);
    }
  }

  protected void shiftApplyAction(int index, boolean after) {
    String shiftStr = shiftTF.getText();
    int shift = 0;
    try {
      shift = Integer.parseInt(shiftStr);
      app.applyShiftMillis(shift, index, after);
      // textArea.setListData(app.getLinesAsArray());
      subList.repaint();
      saveFileBtn.setEnabled(true);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while applying shift.\nError: " + ex.getMessage(), "Cannot shift subtitles", JOptionPane.ERROR_MESSAGE);
    }
  }

  protected void saveFileAction() {
    int returnVal = fc.showSaveDialog(frmSubtitleshifter);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      try {
        app.writeFile(file.getAbsolutePath(), (String) targetEncCB.getSelectedItem(), Constants.FORMAT_TYPE_SRT);
        saveFileBtn.setEnabled(false);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while saving the file.\nError: " + ex.getMessage(), "Cannot save file", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  protected void openFileAction() {
    int returnVal = fc.showOpenDialog(frmSubtitleshifter);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      try {
        app.openFile(file.getAbsolutePath());
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while opening the file.\nError: " + ex.getMessage(), "Cannot open file", JOptionPane.ERROR_MESSAGE);
      }
      reloadFile();
    }
  }

  private class MyListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      SubtitleLine sl = (SubtitleLine) value;
      String labelText = "<html><body>" + sl.getLineNum() + "<br/>" + sl.getTimeFromTo() + "<br/>" + sl.getTextAsHTML() + "<br/></body></html>";
      setText(labelText);

      return this;
    }
  }

}
