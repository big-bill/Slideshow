package slideshow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class DisplaySlideShow {

	private int currentSlide;
	private ImageIcon[] images;
	private JFrame slideShowFrame;
	private JLabel icon;

	
	public DisplaySlideShow(ImageIcon[] images, double delay) {
		currentSlide = 1;
		this.images = images.clone();
		slideShowFrame = new JFrame();
		slideShowFrame.setTitle("Slideshow application");
		slideShowFrame.setLayout(new BorderLayout());
		slideShowFrame.setPreferredSize(new Dimension(800, 600));
		slideShowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		icon = new JLabel();
		icon.setPreferredSize(new Dimension(800, 600));
		icon.setVerticalAlignment(JLabel.CENTER);
		icon.setHorizontalAlignment(JLabel.CENTER);
		icon.setIcon(this.images[0]);
		slideShowFrame.add(icon, BorderLayout.CENTER);
		
		slideShowFrame.pack();
		slideShowFrame.setVisible(true);
		
		
		Timer timer = new Timer((int)(delay*1000), new TimerListener());
		timer.start();
		
	}
		
	//-----------------------------------------------------------------------------------------------------------

	private class TimerListener implements ActionListener {

	    @Override
	    public void actionPerformed(ActionEvent ae) {
	    	if(currentSlide == images.length) {
	    		((Timer)ae.getSource()).stop();
	    		JOptionPane.showMessageDialog(null, "The slideshow has concluded.");
	    		slideShowFrame.dispatchEvent(new WindowEvent(slideShowFrame, JFrame.DISPOSE_ON_CLOSE));
	    		slideShowFrame.dispose();
	    	}
	    	else {
		    	icon.setIcon(images[currentSlide]);
		    	currentSlide++;
	    	}
	    }
	
	}
	
	
}
