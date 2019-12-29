import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainForm {
	private JButton    btnBrowseOutput;
	private JButton    btnOpenOutputDir;
	private JPanel     panelMain;
	private JPanel     panelOutput;
	private JTextField txtOutputPath;
	private JTable     tblFiles;
	private JPanel panelInput;
	private ArrayList<File> inputFiles = new ArrayList<>();

	public MainForm() {
		panelInput.setBorder(BorderFactory.createTitledBorder(panelInput.getName()));
		panelOutput.setBorder(BorderFactory.createTitledBorder(panelOutput.getName()));
		btnBrowseOutput.addMouseListener(new BrowseFilesAdapter());
		btnOpenOutputDir.addMouseListener(new OpenOutputAdapter());
		new FileDrop(panelMain, files -> {
			for (File f : files)
				addFile(f);
			draw();
		});
	}

	public void addFile(File file) {
		inputFiles.add(file);
	}

	public void draw() {
		DefaultTableModel model = (DefaultTableModel) tblFiles.getModel();
		for (File f : inputFiles) {
			model.addRow(new Object[]{f.getAbsolutePath(),"idk","lmao"});
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
		tblFiles = new JTable(new DefaultTableModel(new Object[]{"Input File", "Input Time", "Output Time"},0));
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
				java.awt.Desktop.getDesktop().open(new File(txtOutputPath.getText()));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
