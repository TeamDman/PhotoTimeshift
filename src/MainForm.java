import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;

public class MainForm {
	private JButton              btnBrowseOutput;
	private JButton              btnOpenOutputDir;
	private JPanel               panelMain;
	private JPanel               panelOutput;
	private JTextField           txtOutputPath;
	private JTable               tblFiles;
	private JPanel               panelInput;
	private JPanel               panelAdjust;
	private JSpinner             spinOffset;

	private ArrayList<InputItem> inputItems = new ArrayList<>();

	public MainForm() {
		$$$setupUI$$$();
		panelInput.setBorder(BorderFactory.createTitledBorder(panelInput.getName()));
		panelOutput.setBorder(BorderFactory.createTitledBorder(panelOutput.getName()));
		panelAdjust.setBorder(BorderFactory.createTitledBorder(panelAdjust.getName()));
		btnBrowseOutput.addMouseListener(new BrowseFilesAdapter());
		btnOpenOutputDir.addMouseListener(new OpenOutputAdapter());
		txtOutputPath.setText(System.getProperty("java.io.tmpdir"));
		new FileDrop(panelMain, files -> {
			for (File f : files)
				addFile(f);
			draw();
		});
	}

	public void addFile(File path) {
		try {
			inputItems.add(new InputItem(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw() {
		DefaultTableModel model = (DefaultTableModel) tblFiles.getModel();
		for (InputItem x : inputItems) {
			model.addRow(new Object[]{x.toString(), x.getDateOriginal(), "lmao"});
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame instance = new JFrame("PhotoTimeshift");
		instance.setTitle("PhotoTimeshift");
		instance.setContentPane(new MainForm().panelMain);
		instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instance.pack();
		instance.setVisible(true);
	}

	private void createUIComponents() {
		tblFiles = new JTable(new DefaultTableModel(new Object[]{"Input File", "Input Time", "Output Time"}, 0));
		spinOffset = new JSpinner(new SpinnerNumberModel());
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		panelMain = new JPanel();
		panelMain.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panelMain.setName("");
		panelOutput = new JPanel();
		panelOutput.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		panelOutput.setName("Output");
		panelMain.add(panelOutput, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("Directory");
		panelOutput.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		txtOutputPath = new JTextField();
		txtOutputPath.setText("");
		panelOutput.add(txtOutputPath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		btnBrowseOutput = new JButton();
		btnBrowseOutput.setText("...");
		panelOutput.add(btnBrowseOutput, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		btnOpenOutputDir = new JButton();
		btnOpenOutputDir.setText("Open");
		panelOutput.add(btnOpenOutputDir, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		panelInput = new JPanel();
		panelInput.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		panelInput.setName("Input");
		panelMain.add(panelInput, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JScrollPane scrollPane1 = new JScrollPane();
		panelInput.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane1.setViewportView(tblFiles);
		panelAdjust = new JPanel();
		panelAdjust.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		panelAdjust.setName("Time Adjustment");
		panelInput.add(panelAdjust, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panelAdjust.add(spinOffset, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Time offset");
		panelAdjust.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panelMain;
	}


	private class BrowseFilesAdapter extends MouseAdapter {
		private File path = new File("./");

		@Override
		public void mouseClicked(MouseEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			chooser.setCurrentDirectory(path);
			if (chooser.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
				path = chooser.getSelectedFile();
				txtOutputPath.setText(path.getAbsolutePath());
			}
		}
	}

	private class OpenOutputAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				Desktop.getDesktop().open(new File(txtOutputPath.getText()));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
