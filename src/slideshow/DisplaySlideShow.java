package slideshow;

/*
 * This class creates a JFrame and steps through an array of ImageIcons, which will be display at intervals specified by the user.
 * 
 * Written by Billy Matthews, November 2016.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class DisplaySlideShow {
	private JFrame slideShowFrame;
	private JLabel icon;
	private ImageIcon[] images;
	private int currentSlide;


	//-----------------------------------------------------------------------------------------------------------

	public DisplaySlideShow(ImageIcon[] images, double delay) {
		currentSlide = 0;
		this.images = images.clone();
		
		Timer timer = new Timer((int)(delay*1000), new TimerListener());
		
		slideShowFrame = new JFrame();
		slideShowFrame.setTitle("Slideshow");
		slideShowFrame.setLayout(new BorderLayout());
		slideShowFrame.setPreferredSize(new Dimension(800, 600));
		slideShowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// The icon JLabel is the only thing that will be displayed in the frame
		// It stores the icon in the center of the JFrame
		icon = new JLabel();
		icon.setPreferredSize(new Dimension(800, 600));
		icon.setVerticalAlignment(JLabel.CENTER);
		icon.setHorizontalAlignment(JLabel.CENTER);
		icon.setIcon(this.images[currentSlide]);
		
		// This MouseListener class will advance the slide if the user clicks the Label
		MouseListener mouseListener = new MouseAdapter() {
	    	public void mouseClicked(MouseEvent mouseEvent) {
	    		timer.restart();
	    		currentSlide++;
	    		if(currentSlide >= images.length) {
	    			timer.stop();
	    			JOptionPane.showMessageDialog(null, "The slideshow has concluded.");
		    		slideShowFrame.dispatchEvent(new WindowEvent(slideShowFrame, JFrame.DISPOSE_ON_CLOSE));
		    		slideShowFrame.dispose();
	    		}	
	    		else
	    			icon.setIcon(images[currentSlide]);
	    	}
		};

		icon.addMouseListener(mouseListener);
		
		slideShowFrame.add(icon, BorderLayout.CENTER);
		slideShowFrame.pack();
		slideShowFrame.setVisible(true);
		timer.start();
	}
		
	//-----------------------------------------------------------------------------------------------------------

	private class TimerListener implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
	    	currentSlide++;
	   
	    	// If the currentSlide value is greater than or equal to the length of the images array,
	    	// that means all the images have been display and we are ready to close the slideshow
	    	if(currentSlide >= images.length) {
	    		((Timer)ae.getSource()).stop();
	    		JOptionPane.showMessageDialog(null, "The slideshow has concluded.");
	    		slideShowFrame.dispatchEvent(new WindowEvent(slideShowFrame, JFrame.DISPOSE_ON_CLOSE));
	    		slideShowFrame.dispose();
	    	}
	    	// Otherwise, we increment the currentSlide value and set the icon for the JLabel
	    	else icon.setIcon(images[currentSlide]);
	    }
	}	
}
