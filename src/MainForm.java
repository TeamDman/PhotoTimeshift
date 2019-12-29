import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainForm {
	private JButton    btnBrowseOutput;
	private JButton    btnOpenOutputDir;
	private JPanel     panelMain;
	private JPanel     panelOutput;
	private JTextField txtOutputPath;

	public MainForm() {
		panelOutput.setBorder(BorderFactory.createTitledBorder(panelOutput.getName()));
		btnBrowseOutput.addMouseListener(new BrowseFilesAdapter());
		btnOpenOutputDir.addMouseListener(new OpenOutputAdapter());
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
