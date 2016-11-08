package slideshow;

/*
 * Java II Project 3 Description:
 * Write an application that displays a slideshow of images, one after the other, with a time delay between each image.
 * The user should be able to select up to 10 images for the slide show and specify the time delay in seconds.
 * 
 * 
 * Written by Billy Matthews, November 2016.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;


@SuppressWarnings("serial")
public class Slideshow extends JFrame {
	private final int height = 400;				// Window height
	private final int width = 800;				// Window width

	private JPanel moveImagesPanel;				// Panel that holds two buttons, used for moving images

	private JPanel slideshowSelection;			// Panel that holds slide show and buttons 
	private JList<String> photosList;			// JList that stores image paths
	private DefaultListModel<String> listModel; // List model for photosList JList

	private JPanel selectedImagePreview;		// Panel that displays the currently selected image
	private JLabel selectedImage;				// Used to display the image

	private JPanel bottomPanel;					// Holds the play slideshow, help, and exit buttons


	public Slideshow() {
		setTitle("Slideshow application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		buildMovePicsPanel();			// Creates the panel for buttons that are responsible for moving images
		buildSlideshowPanel();			// Creates the panel for the slideshow images list and buttons
		buildSelectedImagePreview();	// Creates the panel for the selected image preview
		buildBottomButtonPanel();		// Creates the bottom panel which holds a few buttons

		add(moveImagesPanel, BorderLayout.WEST);
		add(slideshowSelection, BorderLayout.CENTER);
		add(selectedImagePreview, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(width, height));
		setResizable(false);
		setVisible(true);

		pack();
	}

	//-----------------------------------------------------------------------------------------------------------

	// This method builds the two buttons on the left of the slide show JList
	private void buildMovePicsPanel() {
		moveImagesPanel = new JPanel();
		moveImagesPanel.setLayout(new GridLayout(2,1));
		moveImagesPanel.setPreferredSize(new Dimension(22, height));  // Second parameter doesn't matter too much

		JButton moveUp = new JButton("<html>^<br>^</html>");
		moveUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(listModel.isEmpty())
					return;

				int indexSelected = photosList.getSelectedIndex();
				if(indexSelected > 0) {
					File selectedImg = new File(listModel.get(indexSelected));
					File fileAboveSelected = new File(listModel.get(indexSelected - 1));

					listModel.setElementAt(selectedImg.getPath(), indexSelected - 1);
					listModel.setElementAt(fileAboveSelected.getPath(), indexSelected);

				}
			}
		});


		JButton moveDown = new JButton("<html>v<br>v</html>");
		moveDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listModel.isEmpty())
					return;

				int indexSelected = photosList.getSelectedIndex();
				if(indexSelected < listModel.size() - 1) {
					File selectedImg =  new File(listModel.get(indexSelected));
					File fileBelowSelected =  new File(listModel.get(indexSelected + 1));

					listModel.setElementAt(selectedImg.getPath(), indexSelected + 1);
					listModel.setElementAt(fileBelowSelected.getPath(), indexSelected);

				}
			}

		});

		moveImagesPanel.add(moveUp);
		moveImagesPanel.add(moveDown);
	}

	//-----------------------------------------------------------------------------------------------------------

	private void buildSlideshowPanel() {
		slideshowSelection = new JPanel();
		slideshowSelection.setPreferredSize(new Dimension(width/2, height - 20));
		slideshowSelection.setLayout(new BorderLayout());
		slideshowSelection.setBorder(BorderFactory.createTitledBorder("Images"));

		// listModel will store the file paths
		listModel = new DefaultListModel<String>();
		photosList = new JList<String>(listModel);
		photosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		photosList.setVisibleRowCount(10);
		photosList.setFixedCellWidth(width/2);


		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				// If listModel isn't empty, we display the selected image in the image preview panel
				if(listModel.size() != 0) {
					try {
						File file = new File(listModel.get(photosList.getSelectedIndex()));
						ImageIcon iconPath = new ImageIcon(file.getPath());
						ImageIcon imageIcon = new ImageIcon(iconPath.getImage().getScaledInstance(width/2, height-20, Image.SCALE_DEFAULT));
						selectedImage.setIcon(imageIcon);
						selectedImage.setVisible(true);
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			}
		};

		// Add the mouse-click listener to the photosList JList
		photosList.addMouseListener(mouseListener);

		// addPhotos button allows the user to add multiple photos to the photosList JList
		JButton addPhotos = new JButton("Add Photos");
		addPhotos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = fileChooser.getSelectedFiles();

					// The slideshow has a maximum size of 10, and if the user selected more than 10 we exit
					// and not add any elements to the list model
					if(selectedFiles.length > 10) {
						JOptionPane.showMessageDialog(null, "Error! More than 10 images selected.");
						return;
					}
					// Now we check if the amount added and the amount in the slideshow list model are greater than 10
					else if(selectedFiles.length + listModel.size() > 10) {
						JOptionPane.showMessageDialog(null, "Error! Slidesow cannot exceed 10 images.");
						return;
					}

					// Steps through the selectedFiles array and adds elements to the listModel if the item is in an image format
					for(File file : selectedFiles) {
						String[] extension = file.getPath().split("[.]");
						if(extension[1].equalsIgnoreCase("gif") || extension[1].equalsIgnoreCase("png") || 
								extension[1].equalsIgnoreCase("jpg") || extension[1].equalsIgnoreCase("jpeg")) {
							listModel.addElement(file.getPath());
						}
					}
				}
			}
		});

		// The removePhoto buttons removes the selected image from the JList, and also resets the image preview
		JButton removePhoto = new JButton("Remove");
		removePhoto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(photosList.getSelectedIndex() != -1) {
					listModel.removeElementAt(photosList.getSelectedIndex());
					selectedImage.setVisible(false);
				}
			}
		});

		// The clearList button clears the entire list of images and resets the image preview
		JButton clearList = new JButton("Clear List");
		clearList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!listModel.isEmpty()) {
					int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the entire list?", 
							"Clear list", JOptionPane.YES_NO_OPTION);
					if(reply == JOptionPane.YES_OPTION) {
						selectedImage.setVisible(false);
						listModel.clear();

					}
				}
			}
		});

		JPanel bottomButtons = new JPanel();
		bottomButtons.setLayout(new GridLayout(1,2));
		bottomButtons.add(removePhoto);
		bottomButtons.add(clearList);

		slideshowSelection.add(addPhotos, BorderLayout.NORTH);
		slideshowSelection.add(photosList, BorderLayout.CENTER);
		slideshowSelection.add(bottomButtons, BorderLayout.SOUTH);
	}

	//-----------------------------------------------------------------------------------------------------------	

	// This method builds the selected image preview, which will display the highlighted JList item
	private void buildSelectedImagePreview() {
		selectedImagePreview = new JPanel();
		selectedImagePreview.setBorder(BorderFactory.createTitledBorder("Image Preview"));
		selectedImagePreview.setLayout(new BorderLayout());
		selectedImagePreview.setPreferredSize(new Dimension(width/2, height - 20));
		selectedImage = new JLabel();
		selectedImagePreview.add(selectedImage, BorderLayout.CENTER);
	}

	//-----------------------------------------------------------------------------------------------------------	

	// Lastly, we create the bottom panel for the JFrame, which will store two buttons, playSlideshow and exit
	private void buildBottomButtonPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,2));

		// playSlideshow will play a slideshow of the images populated in the photosList JList
		JButton playSlideshow = new JButton("Play Slideshow");
		playSlideshow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// playSlides will create a temporary instance of the DisplaySlideShow class
					// which will create a new JFrame and create a slideshow of images
					playSlides();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		// The help button displays some helpful information to the user
		JButton help = new JButton("Help");
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "You may use the following image formats: "
						+ "JPG, JPEG, PNG, and GIF.\n"
						+ "You can click the image during a slideshow "
						+ "to continue to the next slide.\n"
						+ "You may use up to 10 images in your slideshow.", "Help Menu", JOptionPane.QUESTION_MESSAGE);
			}
		});

		// The exit button allows the user to exit the program
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
				if(reply == JOptionPane.YES_OPTION) {
					System.exit(0);
					dispose();
					setVisible(false);
				}
			}
		});

		// helpAndExitButtonPanel holds two buttons and stores them in a GridLayout panel
		JPanel helpAndExitButtonPanel = new JPanel();
		helpAndExitButtonPanel.setLayout(new GridLayout(1,2));
		helpAndExitButtonPanel.add(help);
		helpAndExitButtonPanel.add(exit);

		// Adds the playSlideshow button to the left side of the bottom panel
		bottomPanel.add(playSlideshow);
		// Lastly we add the help and exit button panel to the right side
		bottomPanel.add(helpAndExitButtonPanel);
	}

	//-----------------------------------------------------------------------------------------------------------

	// This method creates an instance of the DisplaySlideShow class, and sends an array of ImageIcons to it
	private void playSlides() throws InterruptedException {
		ImageIcon[] img = new ImageIcon[listModel.size()];
		// If the array empty, we don't bother playing the slideshow
		if(img.length == 0) {
			JOptionPane.showMessageDialog(null, "No images have been selected for the slideshow.");
			return;
		}

		// We populate the ImageIcon array with values the found in listModel
		for(int i = 0; i < img.length; ++i) {
			ImageIcon iconPath = new ImageIcon(new File(listModel.getElementAt(i)).getPath());
			img[i] = new ImageIcon(iconPath.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT));
		}

		while(true) {
			// Get the slide delay
			String input = JOptionPane.showInputDialog(null, "Enter the delay, in seconds, between each slide.");
			try {
				Double delay = Double.parseDouble(input);
				// If the parsing worked, we send the array of ImageIcons and slide delay to the object
				new DisplaySlideShow(img, delay);
				break;
			} catch(NumberFormatException format) {
				JOptionPane.showMessageDialog(null, "Invalid value entered, please try again.");
			} catch(NullPointerException emptyStr) {
				JOptionPane.showMessageDialog(null, "Please enter a value for the time.");
			}
		}
	}	
}
