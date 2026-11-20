import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/** A small graphical editor for applying image effects and inserting overlays. */
public class ImageAppUI extends JFrame {
  private Picture current = new Picture("lib/beach.jpg");
  private final JPanel preview = new JPanel() {
    @Override protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      int width = current.getWidth();
      int height = current.getHeight();
      double scale = Math.min((double) getWidth() / width, (double) getHeight() / height);
      int w = Math.max(1, (int) (width * scale));
      int h = Math.max(1, (int) (height * scale));
      g.drawImage(current.getBufferedImage(), (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
    }
  };

  /** Builds the editor window and its transformation buttons. */
  public ImageAppUI() {
    super("Image App");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    preview.setPreferredSize(new Dimension(720, 480));
    add(preview, BorderLayout.CENTER);
    JPanel controls = new JPanel(new FlowLayout());
    button(controls, "Recolor", () -> current = ImageApp.recolor(current));
    button(controls, "Negative", () -> current = ImageApp.negative(current));
    button(controls, "Grayscale", () -> current = ImageApp.grayscale(current));
    button(controls, "Rotate", this::rotate);
    button(controls, "Insert", () -> insert(false));
    button(controls, "Insert part", () -> insert(true));
    button(controls, "Save", this::save);
    button(controls, "Reset", () -> current = new Picture("lib/beach.jpg"));
    add(controls, BorderLayout.NORTH);
    add(new JLabel("Choose effects in order. Insert can be used more than once."), BorderLayout.SOUTH);
    pack();
    setLocationRelativeTo(null);
  }

  /** Adds a button and reports invalid input without closing the editor. */
  private void button(JPanel panel, String label, Runnable action) {
    JButton button = new JButton(label);
    button.addActionListener(event -> {
      try { action.run(); preview.repaint(); }
      catch (IllegalArgumentException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE); }
    });
    panel.add(button);
  }

  /** Lets the user choose a quarter-turn rotation. */
  private void rotate() {
    String[] options = {"90° clockwise", "180°", "90° counterclockwise"};
    int selected = JOptionPane.showOptionDialog(this, "Choose a rotation", "Rotate", JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    if (selected >= 0) current = ImageApp.rotatePicture(current, selected == 0 ? 90 : selected == 1 ? 180 : -90);
  }

  /** Inserts an entire image or a user-selected rectangular portion. */
  private void insert(boolean crop) {
    JFileChooser chooser = new JFileChooser(new File("lib2"));
    if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
    Picture overlay = new Picture(chooser.getSelectedFile().getPath());
    if (crop) {
      String answer = JOptionPane.showInputDialog(this, "Crop: top row, left column, height, width", "0,0,50,50");
      if (answer == null) return;
      int[] cropValues = numbers(answer, 4);
      overlay = ImageApp.crop(overlay, cropValues[0], cropValues[1], cropValues[2], cropValues[3]);
    }
    String answer = JOptionPane.showInputDialog(this, "Insert position: top row, left column", "30,400");
    if (answer == null) return;
    int[] position = numbers(answer, 2);
    int blend = JOptionPane.showConfirmDialog(this, "Blend the overlay with the background?", "Insert", JOptionPane.YES_NO_CANCEL_OPTION);
    if (blend == JOptionPane.CANCEL_OPTION || blend == JOptionPane.CLOSED_OPTION) return;
    current = ImageApp.combine(current, overlay, position[0], position[1], blend == JOptionPane.YES_OPTION);
  }

  /** Parses comma-separated integers from an input dialog. */
  private static int[] numbers(String answer, int count) {
    String[] parts = answer.split(",");
    if (parts.length != count) throw new IllegalArgumentException("Enter " + count + " numbers separated by commas.");
    int[] values = new int[count];
    for (int i = 0; i < count; i++) values[i] = Integer.parseInt(parts[i].trim());
    return values;
  }

  /** Saves the edited image to a file chosen by the user. */
  private void save() {
    JFileChooser chooser = new JFileChooser();
    chooser.setSelectedFile(new File("image-result.png"));
    if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
    if (!current.write(chooser.getSelectedFile().getPath())) throw new IllegalArgumentException("Could not save the image.");
  }
}
