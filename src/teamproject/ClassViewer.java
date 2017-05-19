package teamproject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ClassViewer extends JFrame {
	private static int WIDTH_SIZE = 700;
	private static int HEIGHT_SIZE = 400; // ������ �⺻ ������ ���
	private static Dimension MONITOR_SIZE = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������
	
	private JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	private MenuBar menuBar = new MenuBar();
	private TreePanel treePanel = new TreePanel();
	private TextPanel textPanel = new TextPanel();
	private TablePanel tablePanel = new TablePanel();
	private SubPanel subPanel = new SubPanel();
	
	public ClassViewer() {
		setTitle("C++ Class Viewer");
		setBounds((int)MONITOR_SIZE.getWidth() / 2 - WIDTH_SIZE / 2,
				  (int)MONITOR_SIZE.getHeight() / 2 - HEIGHT_SIZE / 2,
				  WIDTH_SIZE, HEIGHT_SIZE); // ȭ�� ���߾ӿ� ��ġ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(menuBar, "North");
		
		subSplitPane.setDividerSize(5);
		subSplitPane.setDividerLocation(200);
		subSplitPane.setTopComponent(new JScrollPane(treePanel)); // ���� ��ܿ� Ʈ��
		subSplitPane.setBottomComponent(subPanel); // ���� �ϴܿ� ���� ��
		
		mainSplitPane.setDividerSize(5);
		mainSplitPane.setDividerLocation(200);
		mainSplitPane.setLeftComponent(subSplitPane); // ������ Ʈ�� + ���� ��
		mainSplitPane.setRightComponent(new JScrollPane(textPanel)); // ������ �ؽ�Ʈ ����
		
		add(mainSplitPane, "Center");
		
		setVisible(true);
	}
	
	// �޴��� ���� Ŭ���� ����
	private class MenuBar extends JMenuBar implements ActionListener {
		private JMenu menu = new JMenu("File");
		private JMenuItem openItem = new JMenuItem("Open");
		private JMenuItem saveItem = new JMenuItem("Save");
		private JMenuItem exitItem = new JMenuItem("Exit");
		private final JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir"))); // �⺻ ��θ� ������Ʈ ������ ����
		
		public MenuBar() {
			openItem.addActionListener(this);
			saveItem.addActionListener(this);
			exitItem.addActionListener(this);
			
			menu.add(openItem);
			menu.add(saveItem);
			menu.addSeparator();
			menu.add(exitItem);
			
			add(menu);
		}

		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			
			if (o == openItem) { // �޴� �̺�Ʈ : File - Open
				int returnVal = fileChooser.showOpenDialog(this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// ���� ���� ó�� �ڵ� �ʿ�
				}
				
			} else if (o == saveItem) { // �޴� �̺�Ʈ : File - Save
				// ���� ���� �ڵ� �ʿ�
				
			} else if (o == exitItem) { // �޴� �̺�Ʈ : File - Exit
				System.exit(0);
			}
		}
	}
	
	// Ʈ�� �г� ���� Ŭ���� ����
	private class TreePanel extends JPanel implements TreeSelectionListener  {
		private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("none");
		private JTree tree = new JTree(rootNode);
		
		public TreePanel() {
			setLayout(new BorderLayout());
			
			// �Ľ��� ���� ������ Ŭ���� ��ü�� �о ��带 ������ rootNode�� �߰��ϴ� �ڵ� �ʿ�
			
			tree.addTreeSelectionListener(this);
			
			add(tree);
		}

		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode d = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			Object o = d.getUserObject();
			
			if (o instanceof ClassInfor) {
				// Ʈ������ Ŭ������ ������ ���
				// �ش� Ŭ���� ��ü���� �����͸� �о�� ���̺��� �����Ͽ� ������ ȭ�鿡 ���
			} else if (o instanceof MethodInfor) {
				// Ʈ������ �޼ҵ带 ������ ���
				// �ش� �޼ҵ� ��ü���� �ٵ� �о�� TextArea�� setText
				// ���� �ϴ� ���� �信 ����� ��� ��� ���
			} else if (o instanceof MemberData) {
				// Ʈ������ ����� ������ ���
				// �ش� ��� ��ü���� �����͸� �о�� ���̺��� �����Ͽ� ������ ȭ�鿡 ���
			}
		}
	}
	
	// �ؽ�Ʈ ���� �г� ���� Ŭ���� ����
	private class TextPanel extends JPanel {
		JTextArea textArea = new JTextArea("test");
		
		public TextPanel() {
			setLayout(new BorderLayout());			
			add(textArea);
		}
	}
	
	// ���̺� �г� ���� Ŭ���� ����
	private class TablePanel extends JPanel {
		JTable table = new JTable();
		
		public TablePanel() {
			add(table);
		}
	}
	
	// ���� �� �г� ���� Ŭ���� ���� (���� �ϴ� ����)
	private class SubPanel extends JPanel {
		public SubPanel() {
			add(new JLabel("test"));
		}
	}
}