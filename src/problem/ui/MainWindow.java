package problem.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import problem.detector.AbstractDetector;
import problem.detector.IPatternDetector;
import problem.model.data.IClassData;
import problem.model.data.IPackageModel;
import problem.util.DesignParser;

public class MainWindow implements Observer{
	
	private JFrame frame;
	private JPanel landingPanel;
	private JPanel resultPanel;
	private JButton loadButton;
	private JButton analyzeButton;
	private String imagePath;
	private JScrollPane picPane;
	private JScrollPane treePane;
	IPackageModel model; 
	DesignParser designParser;
	
	public MainWindow(){
		
		this.frame = new JFrame("Design Parser");
		this.frame.setSize(500, 500);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.buildLandingPanel();
		this.assignActions();
		this.frame.add(this.landingPanel);
		
		this.frame.pack();
	}
	public void analyze(){
		this.frame.remove(this.landingPanel);
		this.buildResultPanel();
		this.buildCheckboxPanel();
		
		this.resultPanel.add(this.treePane, BorderLayout.WEST);
		this.resultPanel.add(this.picPane,  BorderLayout.EAST);
		this.frame.add(this.resultPanel, BorderLayout.WEST);
		
//		this.frame.add(this.treePane, BorderLayout.EAST);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
		this.frame.revalidate();
		this.frame.repaint();
		
		this.frame.pack();
	}
	private void buildLandingPanel(){
		this.landingPanel = new JPanel();
		this.loadButton = new JButton("Load Config");
		this.analyzeButton = new JButton("Analyze");
		this.landingPanel.add(this.loadButton);
		this.landingPanel.add(this.analyzeButton);
	}
	void buildResultPanel(){
		this.resultPanel = new JPanel();
		Icon image = new ImageProxy(this.imagePath);
		this.picPane = new JScrollPane(new JLabel(image));
//		this.resultPanel.add(this.picPane, BorderLayout.CENTER);
		
//		this.resultPanel.revalidate();
//		this.resultPanel.repaint();
	}
	
	void buildCheckboxPanel() {
		ArrayList<String> made = new ArrayList<>();
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Model");
		for (String phase: this.designParser.getReader().getPhases()) {
			final DefaultMutableTreeNode node =
					add(root, phase, true, this.designParser);
			for (IPatternDetector det : this.designParser.getModel().getDetectors()) {
				if (det.getPatternName().equalsIgnoreCase(phase)) {
					for (IClassData dat : det.getClasses()) {
						add(node, dat.getName(), true, this.designParser);
						made.add(dat.getName());
					}
				}
			}
			root.add(node);
		}
		for (IClassData dat : this.designParser.getModel().getClasses()) {
			if (!made.contains(dat.getName()))
				add(root, dat.getName(), true, this.designParser);
		}
		//root.add(node);
		
		final DefaultTreeModel treeModel = new DefaultTreeModel(root);
		final JTree tree = new JTree(treeModel);
		
		final ClassCheckboxRenderer renderer = new ClassCheckboxRenderer();
		tree.setCellRenderer(renderer);
		
		final ClassCheckboxManager manager = new ClassCheckboxManager(tree, this.designParser);
		tree.setCellEditor(manager);
		tree.setEditable(true);		

		this.treePane = new JScrollPane(tree);
	}
	
	private void assignActions() {
		this.loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(System.getProperty("user.dir")+"\\input_output");
				int returnVal = fc.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					MainWindow.this.designParser= new DesignParser(fc.getSelectedFile());
				}
			}
			
		});
		this.analyzeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				MainWindow.this.designParser.createThread();
				while(MainWindow.this.designParser.isThreadIsRunning()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				MainWindow.this.setImage(MainWindow.this.designParser.getImagePath());
				MainWindow.this.analyze();
				MainWindow.this.designParser.addObserver(MainWindow.this);
				
			}
			
		});
	}
	
	public void show() {
		this.frame.setVisible(true);
	}

	public String getImage() {
		return this.imagePath;
	}
	public void setImage(String image) {
		this.imagePath = image;
	}

	private static DefaultMutableTreeNode add(
		final DefaultMutableTreeNode parent, final String text,
		final boolean checked, final DesignParser parser)
	{
		final ClassCheckboxData data = new ClassCheckboxData(text, checked, parser);
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
		parent.add(node);
		return node;
	}
	@Override
	public void update(Observable o, Object arg) {
		DesignParser dp = (DesignParser) o;
		
		this.imagePath = dp.getImagePath();
		if(imagePath != null) {
			Icon image = new ImageProxy(this.imagePath);
			this.picPane = new JScrollPane(new JLabel(image));
		}
		this.model = dp.getModel();
		
		this.frame.revalidate();
		this.frame.repaint();
		
	}
}
