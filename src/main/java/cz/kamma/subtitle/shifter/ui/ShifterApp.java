package cz.kamma.subtitle.shifter.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import cz.kamma.subtitle.shifter.Constants;
import cz.kamma.subtitle.shifter.ShifterEngine;
import cz.kamma.subtitle.shifter.SubtitleLine;

public class ShifterApp {

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
  private JPanel panel_9;

  private File openedFile;
  private JCheckBox applyShiftInSelectionCB;

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

  public void check(MouseEvent e) {
    if (e.isPopupTrigger()) { // if the event shows the menu
      subList.setSelectedIndex(subList.locationToIndex(e.getPoint())); // select the item
      if (subList.getSelectedIndex() >= 0)
        jPopupMenu.show(subList, e.getX(), e.getY()); // and show the menu
    }
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    app = new ShifterEngine();
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
    }
    jPopupMenu = new JPopupMenu("Action");
    JMenuItem afterMenuItem = new JMenuItem("Apply Shift After");
    JMenuItem beforeMenuItem = new JMenuItem("Apply Shift Before");
    JMenuItem textMenuItem = new JMenuItem("Edit Subtitle Text");
    JMenuItem timeMenuItem = new JMenuItem("Edit Subtitle Time");
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
    textMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        editSubtitleText();
      }
    });
    timeMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        editSubtitleTime();
      }
    });
    jPopupMenu.add(afterMenuItem);
    jPopupMenu.add(beforeMenuItem);
    jPopupMenu.add(textMenuItem);
    jPopupMenu.add(timeMenuItem);

    frmSubtitleshifter = new JFrame();
    frmSubtitleshifter.setTitle("SubtitleShifter");
    frmSubtitleshifter.setBounds(100, 100, 850, 512);
    frmSubtitleshifter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frmSubtitleshifter.getContentPane().setLayout(new BoxLayout(frmSubtitleshifter.getContentPane(), BoxLayout.X_AXIS));
    frmSubtitleshifter.setMinimumSize(new Dimension(850, 512));

    panel = new JPanel();
    frmSubtitleshifter.getContentPane().add(panel);
    panel.setLayout(new BorderLayout(0, 0));

    panel_1 = new JPanel();
    panel.add(panel_1, BorderLayout.SOUTH);
    panel_1.setLayout(new BorderLayout(0, 0));

    panel_7 = new JPanel();
    panel_1.add(panel_7, BorderLayout.WEST);

    lblNewLabel_2 = new JLabel("Shift (ms)");
    panel_7.add(lblNewLabel_2);

    shiftTF = new JTextField();

    panel_7.add(shiftTF);
    shiftTF.setColumns(10);

    applyShiftBtn = new JButton("Apply Shift");
    panel_7.add(applyShiftBtn);

    applyShiftInSelectionCB = new JCheckBox("Only For Selected Lines");
    panel_7.add(applyShiftInSelectionCB);

    panel_8 = new JPanel();
    panel_1.add(panel_8, BorderLayout.EAST);

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

    panel_2 = new JPanel();
    panel.add(panel_2, BorderLayout.NORTH);
    FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
    flowLayout_1.setAlignment(FlowLayout.LEFT);

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
        reloadFile(openedFile.getAbsolutePath());
      }
    });

    panel_4 = new JPanel();
    panel_2.add(panel_4);

    lblNewLabel = new JLabel("Target Encoding");
    panel_4.add(lblNewLabel);

    targetEncCB = new JComboBox<String>();
    targetEncCB.setModel(new DefaultComboBoxModel<String>(Constants.ENCODING_TYPES));
    targetEncCB.setSelectedItem(Constants.DEFAULT_CHARSET);
    targetEncCB.setEnabled(false);
    targetEncCB.addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        saveFileBtn.setEnabled(true);
      }
    });
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

    panel_6 = new JPanel();
    panel_2.add(panel_6);

    saveFileBtn = new JButton("Save Subtitles");
    saveFileBtn.setEnabled(false);
    saveFileBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        saveFileAction();
      }
    });
    panel_6.add(saveFileBtn);

    panel_9 = new JPanel();
    panel.add(panel_9, BorderLayout.CENTER);
    panel_9.setLayout(new BorderLayout(0, 0));

    subList = new JList<>();
    subList.setCellRenderer(new MyListCellRenderer());
    subList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    subList.setFont(new Font("Courier New", Font.BOLD, 12));
    subList.setDropTarget(new DropTarget() {
      private static final long serialVersionUID = 1L;

      public synchronized void drop(DropTargetDropEvent evt) {
        try {
          evt.acceptDrop(DnDConstants.ACTION_COPY);
          List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
          for (File file : droppedFiles) {
            reloadFile(file.getAbsolutePath());
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while Drag'n'Drop file.\nError: " + ex.getMessage(), "Cannot open file", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    subList.addMouseListener(new MouseListener() {

      @Override
      public void mouseReleased(MouseEvent event) {
        check(event);

      }

      @Override
      public void mousePressed(MouseEvent event) {
        check(event);

      }

      @Override
      public void mouseExited(MouseEvent event) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseEntered(MouseEvent event) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
          editSubtitleText();
        }
      }
    });
    scrollPane = new JScrollPane(subList);
    panel_9.add(scrollPane);

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

  protected void editSubtitleTime() {
    int index = subList.getSelectedIndex();
    if (index < 0)
      return;
    SubtitleLine sl = app.getLines().get(index);
    createTimeDialog("Edit Subtitle Time", sl);
    subList.repaint();
  }

  protected void editSubtitleText() {
    int index = subList.getSelectedIndex();
    if (index < 0)
      return;
    SubtitleLine sl = app.getLines().get(index);
    String s = createTextAreaDialog("Edit Subtitle Text", sl.getText());
    if (s != null) {
      sl.setText(s);
    }
    subList.repaint();
  }

  private String createTextAreaDialog(String title, String initialString) {
    final BorderLayout layout = new BorderLayout();
    final JPanel panel = new JPanel(layout);
    panel.setPreferredSize(new Dimension(380, 160));
    JTextArea subText = new JTextArea();
    subText.setLineWrap(true);
    subText.setWrapStyleWord(true);
    subText.setFont(new Font("Courier New", Font.BOLD, 12));
    if (initialString != null)
      subText.setText(initialString);
    JScrollPane scrollPane = new JScrollPane(subText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(380, 100));
    panel.add(scrollPane);
    int result = JOptionPane.showConfirmDialog(frmSubtitleshifter, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.YES_OPTION) {
      return subText.getText().trim().concat("\n");
    }
    return null;
  }

  private void createTimeDialog(String title, SubtitleLine line) {
    final FlowLayout layout = new FlowLayout();
    final JPanel panel = new JPanel(layout);
    panel.setPreferredSize(new Dimension(200, 35));
    JTextField startTime = new JTextField();
    startTime.setText(line.getTimeFromFormatted());
    JTextField endTime = new JTextField();
    endTime.setText(line.getTimeToFormatted());
    JLabel label = new JLabel(Constants.TIME_DELIMITTER_SRT);
    panel.add(startTime);
    panel.add(label);
    panel.add(endTime);
    int result = JOptionPane.showConfirmDialog(frmSubtitleshifter, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.YES_OPTION) {
      try {
        line.setTimeFrom(startTime.getText());
        line.setTimeTo(endTime.getText());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(frmSubtitleshifter, "Cannot parse time.\nError: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
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
    if (searchStr == null || searchStr.trim().length() == 0)
      return;
    int index = app.search(searchStr);
    if (index < 0) {
      subList.clearSelection();
      JOptionPane.showMessageDialog(frmSubtitleshifter, "String not found", "Search result", JOptionPane.INFORMATION_MESSAGE);
    } else
      subList.setSelectedIndex(index);
  }

  protected void reloadFile(String filename) {
    try {
      if (filename != null && !"".equals(filename)) {
        app.readFile(filename, (String) encodingCB.getSelectedItem(), Constants.FORMAT_TYPE_SRT);
        subList.setListData(app.getLinesAsArray());
        openedFile = new File(filename);
        targetEncCB.setEnabled(true);
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while reading file.\nError: " + ex.getMessage(), "Cannot read file", JOptionPane.ERROR_MESSAGE);
    }
  }

  protected void shiftApplyAction(int index, boolean after) {
    String shiftStr = shiftTF.getText();
    if (shiftStr == null || shiftStr.trim().length() == 0)
      return;
    int shift = 0;
    try {
      shift = Integer.parseInt(shiftStr);
      if (applyShiftInSelectionCB.isSelected())
        app.applyShiftMillis(shift, subList.getSelectedIndices());
      else
        app.applyShiftMillis(shift, index, after);
      subList.repaint();
      saveFileBtn.setEnabled(true);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frmSubtitleshifter, "Error occured while applying shift.\nError: " + ex.getMessage(), "Cannot shift subtitles", JOptionPane.ERROR_MESSAGE);
    }
  }

  protected void saveFileAction() {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(openedFile.getParentFile());
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
    JFileChooser fc = new JFileChooser();
    int returnVal = fc.showOpenDialog(frmSubtitleshifter);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      reloadFile(file.getAbsolutePath());
    }
  }

  private class MyListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      SubtitleLine sl = (SubtitleLine) value;
      String labelText = "<html><body><font color='blue'>" + sl.getLineNum() + "</font><br/><font color='red'>" + sl.getTimeFromTo() + "</font><br/><font color='green'>" + sl.getTextAsHTML() + "</font><br/></body></html>";
      setText(labelText);
      if (isSelected)
        setBackground(Color.YELLOW);
      return this;
    }
  }

}
