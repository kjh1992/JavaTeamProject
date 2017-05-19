package teamproject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ClassViewer extends JFrame {
	private static int WIDTH_SIZE = 700;
	private static int HEIGHT_SIZE = 400; // 프레임 기본 사이즈 상수
	private static Dimension MONITOR_SIZE = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
	
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
		subSplitPane.setDividerLocation(200);
		subSplitPane.setTopComponent(new JScrollPane(treePanel)); // 좌측 상단에 트리
		subSplitPane.setBottomComponent(subPanel); // 좌측 하단에 서브 뷰
		
		mainSplitPane.setDividerSize(5);
		mainSplitPane.setDividerLocation(200);
		mainSplitPane.setLeftComponent(subSplitPane); // 좌측에 트리 + 서브 뷰
		mainSplitPane.setRightComponent(new JScrollPane(textPanel)); // 우측에 텍스트 영역
		
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
					// 파일 오픈 처리 코드 필요
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
			
			// 파싱을 통해 생성된 클래스 객체를 읽어서 노드를 생성해 rootNode에 추가하는 코드 필요
			
			tree.addTreeSelectionListener(this);
			
			add(tree);
		}

		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode d = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			Object o = d.getUserObject();
			
			if (o instanceof ClassInfor) {
				// 트리에서 클래스를 눌렀을 경우
				// 해당 클래스 객체에서 데이터를 읽어와 테이블을 생성하여 오른쪽 화면에 띄움
			} else if (o instanceof MethodInfor) {
				// 트리에서 메소드를 눌렀을 경우
				// 해당 메소드 객체에서 바디를 읽어와 TextArea에 setText
				// 좌측 하단 서브 뷰에 사용한 멤버 목록 띄움
			} else if (o instanceof MemberData) {
				// 트리에서 멤버를 눌렀을 경우
				// 해당 멤버 객체에서 데이터를 읽어와 테이블을 생성하여 오른쪽 화면에 띄움
			}
		}
	}
	
	// 텍스트 영역 패널 내부 클래스 정의
	private class TextPanel extends JPanel {
		JTextArea textArea = new JTextArea("test");
		
		public TextPanel() {
			setLayout(new BorderLayout());			
			add(textArea);
		}
	}
	
	// 테이블 패널 내부 클래스 정의
	private class TablePanel extends JPanel {
		JTable table = new JTable();
		
		public TablePanel() {
			add(table);
		}
	}
	
	// 서브 뷰 패널 내부 클래스 정의 (좌측 하단 영역)
	private class SubPanel extends JPanel {
		public SubPanel() {
			add(new JLabel("test"));
		}
	}
}