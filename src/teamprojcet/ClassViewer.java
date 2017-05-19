package teamprojcet;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

public class ClassViewer extends JFrame {
	private static int WIDTH_SIZE = 700;
	private static int HEIGHT_SIZE = 400;
	
	private MenuBar menuBar = new MenuBar();
	
	public ClassViewer() {
		setTitle("C++ Class Viewer");
		setBounds(300, 200, WIDTH_SIZE, HEIGHT_SIZE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(menuBar, "North");
		
		setVisible(true);
	}
	
	// �޴��� ���� Ŭ���� ����
	private class MenuBar extends JMenuBar implements ActionListener {
		private JMenu menu = new JMenu("File");
		private JMenuItem openItem = new JMenuItem("Open");
		private JMenuItem saveItem = new JMenuItem("Save");
		private JMenuItem exitItem = new JMenuItem("Exit");
		private final JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
		
		public MenuBar() {
			openItem.addActionListener(this);
			menu.add(openItem);
			
			saveItem.addActionListener(this);
			menu.add(saveItem);
			
			menu.addSeparator();
			
			exitItem.addActionListener(this);
			menu.add(exitItem);
			
			add(menu);
		}

		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			
			if (o == openItem) {
				int returnVal = fileChooser.showOpenDialog(this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// ���� ���� ó�� �ڵ� �߰�
				}
				
			} else if (o == saveItem) {
				// ���� ���� �ڵ� �߰�		
				
			} else if (o == exitItem) {
				System.exit(0);
			}
		}
	}
}