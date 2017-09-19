package cz.kamma.subtitle.shifter.ui;

import javax.swing.JButton;
import javax.swing.JProgressBar;

public class ProgressBarUpdator implements java.lang.Runnable {
  private JProgressBar jpb = null;
  private JButton translationModeBtn = null;
  public int value = 0;

  public ProgressBarUpdator(javax.swing.JProgressBar jpb, JButton translationModeBtn, int maximum) {
    this.jpb = jpb;
    this.translationModeBtn = translationModeBtn;
    jpb.setMinimum(0);
    jpb.setMaximum(maximum);
    jpb.setValue(0);
  }

  public void run() {
    do {
      jpb.setValue(value);
      try {
        Thread.sleep(10);
      } catch (java.lang.InterruptedException ex) {
        ex.printStackTrace();
      }
    } while (value < jpb.getMaximum());
    jpb.setValue(0);
    translationModeBtn.setEnabled(true);
  }

  public synchronized void addValue() {
    value += 1;
  }
}