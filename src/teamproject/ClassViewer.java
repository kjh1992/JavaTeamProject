package teamproject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class ClassViewer extends JFrame {
	private static final int WIDTH_SIZE = 700;
	private static final int HEIGHT_SIZE = 400; // ������ �⺻ ������ ���
	private static final Dimension MONITOR_SIZE = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������
	
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
		subSplitPane.setDividerLocation(225);
		subSplitPane.setTopComponent(treePanel); // ���� ��ܿ� Ʈ��
		subSplitPane.setBottomComponent(subPanel); // ���� �ϴܿ� ���� ��
		
		mainSplitPane.setDividerSize(5);
		mainSplitPane.setDividerLocation(200);
		mainSplitPane.setLeftComponent(subSplitPane); // ������ Ʈ�� + ���� ��
		mainSplitPane.setRightComponent(new JPanel()); // ������ �� �г�
		
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
					Parser.openFile(file);
					Parser.parse();
					treePanel.update(Parser.theClass);
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
			
			tree.addTreeSelectionListener(this);
			tree.setRootVisible(false);
			
			add(new JScrollPane(tree));
		}

		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode d = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			Object o = d.getUserObject();
			
			if (o instanceof ClassInfor) { // Ŭ���� ��带 Ŭ��
				tablePanel.update((ClassInfor)o);
				
			} else if (o instanceof MethodInfor) { // �޼ҵ� ��带 Ŭ��
				textPanel.update((MethodInfor)o);
				
			} else if (o instanceof MemberData) { // ��� ��带 Ŭ��
				tablePanel.update((MemberData)o);
			}
		}
		
		public void update(ClassInfor c) {
			// Ʈ�� ������ ����
			tree.removeAll();
			
			// ���ڷ� ���޵� Ŭ������ ��Ʈ ���� ����
			rootNode.setUserObject(c);
			
			// �ش� Ŭ������ ���� �޼ҵ���� �ڽ� ���� �߰�
			for (MethodInfor m : c.methodList) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m);
				rootNode.add(childNode);
			}
			
			// �ش� Ŭ������ ���� ������� �ڽ� ���� �߰�
			for (MemberData m : c.memberList) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m);
				rootNode.add(childNode);
			}
			
			// ��Ʈ ��� ���̰� ����
			tree.setRootVisible(true);
		}
	}
	
	// �ؽ�Ʈ ���� �г� ���� Ŭ���� ����
	private class TextPanel extends JPanel {
		JTextArea textArea = new JTextArea();
		
		public TextPanel() {
			setLayout(new BorderLayout());	
			
			textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
			textArea.setTabSize(4);
			
			add(new JScrollPane(textArea));
		}
		
		public void update(MethodInfor m) {
			// �ؽ�Ʈ ������ ������ �ش� �޼ҵ��� �ٵ�� ä��
			textArea.setText(m.getBody());
			
			// ����� ������ ä ���� �г� ����
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
	}
	
	// ���̺� �г� ���� Ŭ���� ����
	private class TablePanel extends JPanel {
		
		String[] classAttributes = { "Name", "Type", "Access" };
		String[] memberAttributes = { "Name", "Methods" };
		DefaultTableModel classTableModel = new DefaultTableModel(classAttributes, 0);
		DefaultTableModel memberTableModel = new DefaultTableModel(memberAttributes, 0);
		JTable table = new JTable(classTableModel);
		
		public TablePanel() {
			setLayout(new BorderLayout());	
			
			table.setFont(new Font("Monospaced", Font.PLAIN, 12));
			
			add(new JScrollPane(table));
		}
		
		public void update(ClassInfor c) {
			// Ŭ���� ���̺� �� ���� ����
			for (int i = classTableModel.getRowCount() - 1; i >= 0 ; i--)
				classTableModel.removeRow(i);
			
			// Ŭ������ ���� �޼ҵ���� ���̺� �߰�
			for (MethodInfor m : c.methodList) {
				classTableModel.addRow(new String[] { m.toString(), m.getType(), m.getAccess()});
			}
			
			// Ŭ������ ���� ������� ���̺� �߰�
			for (MemberData m : c.memberList) {
				classTableModel.addRow(new String[] { m.toString(), m.getType(), m.getAccess()});
			}
			
			// ���̺� �� ����
			table.setModel(classTableModel);
			
			// ����� ������ ä ���� �г� ����
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
		
		public void update(MemberData m) {
			// ��� ���̺� �� ���� ����
			for (int i = memberTableModel.getRowCount() - 1; i >= 0 ; i--)
				memberTableModel.removeRow(i);
			
			// Ŭ������ ��� �޼ҵ带 ��ȸ�Ͽ� �ش� ����� ����Ͽ����� ���ڿ��� �߰�
			String methodUsing = "";
			
			for (MethodInfor method : m.getParentClass().methodList) {
				if (method.memberList.contains(m)) {
					if (methodUsing.equals(""))
						methodUsing += method.toString();
					else
						methodUsing += ", " + method.toString();
				}
			}
			
			// ���̺� �߰�
			memberTableModel.addRow(new String[] { m.getName(), methodUsing });
			
			// ���̺� �� ���� �� ���� ����
			table.setModel(memberTableModel);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(400);
			
			// ����� ������ ä ���� �г� ����
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
		
	}
	
	// ���� �� �г� ���� Ŭ���� ���� (���� �ϴ� ����)
	private class SubPanel extends JPanel {
		public SubPanel() {
			add(new JLabel("test"));
		}
	}
}