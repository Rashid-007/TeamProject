package problem.ui;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClassSelectorPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public final JLabel label = new JLabel();
	public final JCheckBox check = new JCheckBox();

	public ClassSelectorPanel() {
		this.check.setMargin(new Insets(0, 0, 0, 0));
		setLayout(new BorderLayout());
		add(this.check, BorderLayout.WEST);
		add(this.label, BorderLayout.CENTER);
	}
}
