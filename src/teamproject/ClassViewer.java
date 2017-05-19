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
	private static final int HEIGHT_SIZE = 400; // 프레임 기본 사이즈 상수
	private static final Dimension MONITOR_SIZE = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
	
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
				  WIDTH_SIZE, HEIGHT_SIZE); // 화면 정중앙에 위치
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(menuBar, "North");
		
		subSplitPane.setDividerSize(5);
		subSplitPane.setDividerLocation(225);
		subSplitPane.setTopComponent(treePanel); // 좌측 상단에 트리
		subSplitPane.setBottomComponent(subPanel); // 좌측 하단에 서브 뷰
		
		mainSplitPane.setDividerSize(5);
		mainSplitPane.setDividerLocation(200);
		mainSplitPane.setLeftComponent(subSplitPane); // 좌측에 트리 + 서브 뷰
		mainSplitPane.setRightComponent(new JPanel()); // 우측에 빈 패널
		
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
					Parser.parse();
					treePanel.update(Parser.theClass);
				}
				
			} else if (o == saveItem) { // 메뉴 이벤트 : File - Save
				// 파일 저장 코드 필요
				
			} else if (o == exitItem) { // 메뉴 이벤트 : File - Exit
				System.exit(0);
			}
		}
	}
	
	// 트리 패널 내부 클래스 정의
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
			
			if (o instanceof ClassInfor) { // 클래스 노드를 클릭
				tablePanel.update((ClassInfor)o);
				
			} else if (o instanceof MethodInfor) { // 메소드 노드를 클릭
				textPanel.update((MethodInfor)o);
				
			} else if (o instanceof MemberData) { // 멤버 노드를 클릭
				tablePanel.update((MemberData)o);
			}
		}
		
		public void update(ClassInfor c) {
			// 트리 내용을 제거
			tree.removeAll();
			
			// 인자로 전달된 클래스를 루트 노드로 설정
			rootNode.setUserObject(c);
			
			// 해당 클래스에 속한 메소드들을 자식 노드로 추가
			for (MethodInfor m : c.methodList) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m);
				rootNode.add(childNode);
			}
			
			// 해당 클래스에 속한 멤버들을 자식 노드로 추가
			for (MemberData m : c.memberList) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(m);
				rootNode.add(childNode);
			}
			
			// 루트 노드 보이게 설정
			tree.setRootVisible(true);
		}
	}
	
	// 텍스트 영역 패널 내부 클래스 정의
	private class TextPanel extends JPanel {
		JTextArea textArea = new JTextArea();
		
		public TextPanel() {
			setLayout(new BorderLayout());	
			
			textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
			textArea.setTabSize(4);
			
			add(new JScrollPane(textArea));
		}
		
		public void update(MethodInfor m) {
			// 텍스트 영역의 내용을 해당 메소드의 바디로 채움
			textArea.setText(m.getBody());
			
			// 사이즈를 유지한 채 우측 패널 변경
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
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
			
			table.setFont(new Font("Monospaced", Font.PLAIN, 12));
			
			add(new JScrollPane(table));
		}
		
		public void update(ClassInfor c) {
			// 클래스 테이블 모델 내용 제거
			for (int i = classTableModel.getRowCount() - 1; i >= 0 ; i--)
				classTableModel.removeRow(i);
			
			// 클래스에 속한 메소드들을 테이블에 추가
			for (MethodInfor m : c.methodList) {
				classTableModel.addRow(new String[] { m.toString(), m.getType(), m.getAccess()});
			}
			
			// 클래스에 속한 멤버들을 테이블에 추가
			for (MemberData m : c.memberList) {
				classTableModel.addRow(new String[] { m.toString(), m.getType(), m.getAccess()});
			}
			
			// 테이블 모델 변경
			table.setModel(classTableModel);
			
			// 사이즈를 유지한 채 우측 패널 변경
			int tempDividerLocation = mainSplitPane.getDividerLocation();
			mainSplitPane.setRightComponent(this);
			mainSplitPane.setDividerLocation(tempDividerLocation);
		}
		
		public void update(MemberData m) {
			// 멤버 테이블 모델 내용 제거
			for (int i = memberTableModel.getRowCount() - 1; i >= 0 ; i--)
				memberTableModel.removeRow(i);
			
			// 클래스의 모든 메소드를 순회하여 해당 멤버를 사용하였으면 문자열에 추가
			String methodUsing = "";
			
			for (MethodInfor method : m.getParentClass().methodList) {
				if (method.memberList.contains(m)) {
					if (methodUsing.equals(""))
						methodUsing += method.toString();
					else
						methodUsing += ", " + method.toString();
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
		public SubPanel() {
			add(new JLabel("test"));
		}
	}
}