package problem.ui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageProxy implements Icon{
	volatile ImageIcon imageIcon;
	String pathToImage= null;
	boolean retrieving;
	private Thread retrievalThread;
	
	public ImageProxy(String fileName){
		this.pathToImage = fileName;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (this.imageIcon != null) {
			this.imageIcon.paintIcon(c, g, x, y);
		} else {
			g.drawString("Loading image, please wait...", x+300, y+190);
			if (!this.retrieving) {
				this.retrieving = true;
				final Component com = c;
				this.retrievalThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							
							ImageProxy.this.setImageIcon(new ImageIcon(ImageProxy.this.pathToImage, "UML Diagram"));

							//NOTE: Do both revalidate() and repaint() on the parent component
							com.revalidate();
							com.repaint();
						} catch (Exception e) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException exception) {
								exception.printStackTrace();
							}
						}
					}
				});
				this.retrievalThread.start();
			}
		}
	}

	@Override
	public int getIconWidth() {
		if (this.imageIcon != null) {
            return this.imageIcon.getIconWidth();
        }
		return 800;
	}

	@Override
	public int getIconHeight() {
		if (this.imageIcon != null) {
            return this.imageIcon.getIconHeight();
        }
		return 800;
	}
	
	synchronized void setImageIcon(ImageIcon imageIcon){
		this.imageIcon = imageIcon;
	}
	
	public void flush(){
		if(this.imageIcon!=null){
			this.imageIcon.getImage().flush();
		}
		this.retrieving = false;
	}

}
