package teamproject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

public class ClassViewer extends JFrame {
	private static final int WIDTH_SIZE = 700;
	private static final int HEIGHT_SIZE = 400; // 프레임 기본 사이즈 상수
	private static final Dimension MONITOR_SIZE = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
	
	private static ArrayList<ClassInfor> classList = new ArrayList<ClassInfor>();
	
	private JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	
	private MenuBar menuBar = new MenuBar();
	private TreePanel treePanel = new TreePanel();
	private TextPanel textPanel = new TextPanel();
	private TablePanel tablePanel = new TablePanel();
	private SubPanel subPanel = new SubPanel();
	
	private JPanel titlePanel = new JPanel(new BorderLayout());
	private JLabel label1 = new JLabel("C++ Class Viewer  Ver.1.0", JLabel.CENTER);
	private JLabel label2 = new JLabel("by Joohong Kim, Hyoenjin Kim", JLabel.CENTER);

	
	public ClassViewer() {
		setTitle("C++ Class Viewer");
		setBounds((int)MONITOR_SIZE.getWidth() / 2 - WIDTH_SIZE / 2, (int)MONITOR_SIZE.getHeight() / 2 - HEIGHT_SIZE / 2,
				WIDTH_SIZE, HEIGHT_SIZE); // 화면 정중앙에 위치
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(menuBar, "North");
		
		subSplitPane.setDividerSize(5);
		subSplitPane.setDividerLocation(225);
		subSplitPane.setTopComponent(treePanel); // 좌측 상단에 트리
		subSplitPane.setBottomComponent(subPanel); // 좌측 하단에 서브 뷰
		subSplitPane.setResizeWeight(1);
		
		mainSplitPane.setDividerSize(5);
		mainSplitPane.setDividerLocation(200);
		mainSplitPane.setLeftComponent(subSplitPane); // 좌측에 트리 + 서브 뷰
		label1.setFont(new Font("Serif", Font.BOLD, 36));
		label2.setFont(new Font("Monospaced", Font.PLAIN, 20));
		titlePanel.add(label1, "Center");
		titlePanel.add(label2, "South");
		mainSplitPane.setRightComponent(titlePanel); // 우측에 빈 패널
		
		add(mainSplitPane, "Center");
		
		setVisible(true);
	}
	
	// 메뉴바 내부 클래스 정의
	private class MenuBar extends JMenuBar implements ActionListener {
		private JMenu menu = new JMenu("File");
		private JMenuItem openItem = new JMenuItem("Open");
		private JMenuItem saveItem = new JMenuItem("Save");
		private JMenuItem exitItem = new JMenuItem("Exit");
		private final JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir"))); // 기본 경로를 프로젝트 폴더로 설정
		
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
			
			if (o == openItem) { // 메뉴 이벤트 : File - Open
				int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Parser.openFile(file);
					classList.clear();
					Parser.parse();
					resetFrame();
					treePanel.update();
				}
			} else if (o == saveItem) { // 메뉴 이벤트 : File - Save
				int returnVal = fileChooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					Parser.saveFile(file);
				}
			} else if (o == exitItem) { // 메뉴 이벤트 : File - Exit
				System.exit(0);
			}
		}
	}
	
	// 트리 패널 내부 클래스 정의
	private class TreePanel extends JPanel implements TreeSelectionListener  {
		private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
		private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		private JTree tree = new JTree(treeModel);
		
		public TreePanel() {
			setLayout(new BorderLayout());
			
			tree.addTreeSelectionListener(this);
			tree.setRootVisible(false);
			
			add(new JScrollPane(tree));
		}

		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode d = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			Object o = d.getUserObject();
			
			if (o instanceof ClassInfor) { // 클래스 노드를 클릭
				subPanel.removeAll();
				tablePanel.update((ClassInfor)o);
				
			} else if (o instanceof MethodInfor) { // 메소드 노드를 클릭
				textPanel.update((MethodInfor)o);
				subPanel.update((MethodInfor)o);
				
			} else if (o instanceof MemberInfor) { // 멤버 노드를 클릭
				subPanel.removeAll();
				tablePanel.update((MemberInfor)o);
			}
		}
		
		public void update() {
			// 트리 내용을 제거
			tree.setRootVisible(true);
			rootNode.removeAllChildren();
			tree.removeTreeSelectionListener(this);
			
			for (ClassInfor c : classList) {
				DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(c);
				// 해당 클래스에 속한 메소드들을 자식 노드로 추가
				for (MethodInfor m : c.methodList) {
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m);
					classNode.add(childNode);
				}
				
				// 해당 클래스에 속한 멤버들을 자식 노드로 추가
				for (MemberInfor m : c.memberList) {
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m);
					classNode.add(childNode);
				}
				rootNode.add(classNode);	
			}
			treeModel.reload();
			tree.addTreeSelectionListener(this);
			tree.expandRow(0);
			tree.setRootVisible(false);
		}
	}
	
	// 텍스트 영역 패널 내부 클래스 정의
	private class TextPanel extends JPanel implements DocumentListener {
		Hashtable<MethodInfor, JScrollPane> hashtable = new Hashtable<MethodInfor, JScrollPane>();
		MethodInfor currentMethod;
		
		public TextPanel() {
			setLayout(new BorderLayout());	
		}
		
		public void update(MethodInfor m) {
			if (!hashtable.containsKey(m)) {
				JTextArea textArea = new JTextArea();
				textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
				textArea.setTabSize(4);
				textArea.setText(m.getBody());
				textArea.getDocument().addDocumentListener(this);
				hashtable.put(m, new JScrollPane(textArea));
			}
			
			// 텍스트 영역의 내용을 해당 메소드의 바디로 채움
			removeAll();
			currentMethod = m;
			add(hashtable.get(currentMethod));
			
			// 사이즈를 유지한 채 우측 패널 변경
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
		
		public void insertUpdate(DocumentEvent e) {
			currentMethod.setBody(((JTextArea)hashtable.get(currentMethod).getViewport().getView()).getText());
			subPanel.update(currentMethod);
		}
		
		public void removeUpdate(DocumentEvent e) {
			currentMethod.setBody(((JTextArea)hashtable.get(currentMethod).getViewport().getView()).getText());
			subPanel.update(currentMethod);
		}
		
		public void changedUpdate(DocumentEvent e) { }
	}
	
	// 테이블 패널 내부 클래스 정의
	private class TablePanel extends JPanel {
		String[] classAttributes = { "Name", "Type", "Access" };
		String[] memberAttributes = { "Name", "Methods" };
		DefaultTableModel classTableModel = new DefaultTableModel(classAttributes, 0);
		DefaultTableModel memberTableModel = new DefaultTableModel(memberAttributes, 0);
		JTable table = new JTable(classTableModel);
		
		public TablePanel() {
			setLayout(new BorderLayout());
			table.setFillsViewportHeight(true);
			
			table.setEnabled(false);
			table.setFont(new Font(table.getFont().getFontName(), Font.PLAIN, 14));
			
			add(new JScrollPane(table));
		}
		
		public void update(ClassInfor c) {
			// 클래스 테이블 모델 내용 제거
			for (int i = classTableModel.getRowCount() - 1; i >= 0 ; i--)
				classTableModel.removeRow(i);
			
			// 클래스에 속한 메소드들을 테이블에 추가
			for (MethodInfor m : c.methodList)
				classTableModel.addRow(new String[] { m.toString(), m.getType(), m.getAccess()});
			
			// 클래스에 속한 멤버들을 테이블에 추가
			for (MemberInfor m : c.memberList)
				classTableModel.addRow(new String[] { m.getName(), m.getType(), m.getAccess()});
			
			// 테이블 모델 변경
			table.setModel(classTableModel);
			
			// 사이즈를 유지한 채 우측 패널 변경
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
		
		public void update(MemberInfor m) {
			// 멤버 테이블 모델 내용 제거
			for (int i = memberTableModel.getRowCount() - 1; i >= 0 ; i--)
				memberTableModel.removeRow(i);
			
			// 클래스의 모든 메소드를 순회하여 해당 멤버를 사용하였으면 문자열에 추가
			String methodUsing = "";
			
			for (MethodInfor method : m.getParentClass().methodList) {
				if (method.getMemberList().contains(m)) {
					if (methodUsing.equals("")) methodUsing += method.toString();
					else methodUsing += ", " + method.toString();
				}
			}
			
			// 테이블에 추가
			memberTableModel.addRow(new String[] { m.getName(), methodUsing });
			
			// 테이블 모델 변경 및 비율 조정
			table.setModel(memberTableModel);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(400);
			
			// 사이즈를 유지한 채 우측 패널 변경
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}	
	}
	
	// 서브 뷰 패널 내부 클래스 정의 (좌측 하단 영역)
	private class SubPanel extends JPanel {
		JPanel usePanel = new JPanel();
		JLabel label = new JLabel("Use", JLabel.CENTER);
		JPanel memberPanel = new JPanel();
		
		public SubPanel() {
			setLayout(new FlowLayout(FlowLayout.LEADING));
			setBackground(Color.WHITE);
			
			memberPanel.setLayout(new GridLayout(0, 1));
			memberPanel.setBackground(Color.WHITE);
			memberPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.BLACK, 1, true),
					BorderFactory.createEmptyBorder(3, 5, 3, 5))
					);
			
			usePanel.setLayout(new BoxLayout(usePanel, BoxLayout.Y_AXIS));
			usePanel.setBackground(Color.WHITE);
			label.setAlignmentX(CENTER_ALIGNMENT);
			usePanel.add(label);
			usePanel.add(memberPanel);
			
		}
		
		public void update(MethodInfor method) {
			memberPanel.removeAll();
			ArrayList<MemberInfor> list = method.getMemberList();
			if (list.isEmpty()) memberPanel.add(new JLabel("(none)", JLabel.CENTER));
			else for (MemberInfor m : method.getMemberList()) memberPanel.add(new JLabel(m.getName(), JLabel.CENTER));
			add(usePanel);
			revalidate();
		}		
	}
	
	
	// 클래스 관련 메소드들
	public static void addClass(ClassInfor c) {
		classList.add(c);
	}
	
	public static ClassInfor getTheClass(String name) {
		for (ClassInfor c : classList)
			if (c.getName().equals(name)) return c;
		return null;
	}
	
	public void resetFrame() {
		int tempDividerLocation = mainSplitPane.getDividerLocation();
		subPanel.removeAll();
		mainSplitPane.setRightComponent(titlePanel);
		mainSplitPane.setDividerLocation(tempDividerLocation);
	}
}