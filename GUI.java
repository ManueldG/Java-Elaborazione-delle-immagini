import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;

public class GUI extends Frame  {
	private Image 					lennaImage;
	private Insets 					insets;
	public	String					fileName;

//	public final static int			IMAGE_WIDTH = 256;
//	public final static int			IMAGE_HEIGHT = 256;
	
	public  static int			IMAGE_WIDTH;
	public  static int			IMAGE_HEIGHT;
	
	private final static int[]		factors = {100, 99, 90, 80, 70, 60, 50, 40, 30, 20, 10, 5, 1};
	
	private ImageUtil				imgUtil;
	private int[]					img;

	public GUI( String fileName, int xSize, int ySize )
	{
		super( "Original Image" );
		
		IMAGE_WIDTH = xSize;
		IMAGE_HEIGHT = ySize;

		this.fileName = fileName;
		imgUtil = new ImageUtil( getImageName(), IMAGE_WIDTH, IMAGE_HEIGHT );

		initMenu();
		initImage();

		setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
		this.pack();
		this.show();

		insets = getInsets();
		setSize(insets.left + GUI.IMAGE_WIDTH, insets.top + GUI.IMAGE_HEIGHT);
	}

	public
	String	getImageName()
	{
		return fileName;
	}

	public
	ImageUtil getImageUtil()
	{
		return imgUtil;
	}

	private
	void initImage()
	{
		img = imgUtil.loadImage();
		lennaImage = createImage( imgUtil.getImageObject(img) );
	}


	private
	MenuItem	createMenuItem( String label, char shortcut, final int factor, final boolean standard, final int blockSize )
	{
//		MenuItem	item = new MenuItem( label, new MenuShortcut(shortcut) );
		MenuItem	item = new MenuItem( label );
		
		class MenuItemListener implements ActionListener {
			
			public
			void actionPerformed( ActionEvent e )
			{
				if (standard ) 
					new FFTFrame( GUI.this, img, factor, blockSize );
				else
					new ExtendedFFTFrame( GUI.this, img, factor );
			}
		}
		
		ActionListener listener = new MenuItemListener();
		item.addActionListener(listener);
		
		return item;
	}

	private
	void initMenu()
	{
		MenuBar  menubar = new MenuBar();
		this.setMenuBar( menubar );
		
		Menu standard = new Menu( "Compress" );
		Menu standard16x16 = new Menu( "Compress 16x16" );
		Menu extended = new Menu( "Extended" );
		menubar.add( standard );
		menubar.add( standard16x16 );
		menubar.add( extended );
		
		MenuItem	c1, q;
		

		MenuItem	[]itm = new MenuItem[ factors.length ];
		for( int i=0; i <  itm.length; i++) {
			itm[i] = createMenuItem( String.valueOf(factors[i]) +"%", (char)(KeyEvent.VK_A + i), factors[i], true, IMAGE_WIDTH );
			standard.add( itm[i] );
		}

		standard.add( q = new MenuItem( "Quit", new MenuShortcut(KeyEvent.VK_Q) ));


		MenuItem	[]itm2 = new MenuItem[ factors.length ];
		for( int i=0; i <  itm2.length; i++) {
			itm2[i] = createMenuItem( String.valueOf(factors[i]) +"%", (char)(KeyEvent.VK_A + i), factors[i], true, 16 );
			standard16x16.add( itm2[i] );
		}


		MenuItem	[]extItm = new MenuItem[ factors.length ];
		for( int i=0; i <  itm.length; i++) {
			extItm[i] = createMenuItem( String.valueOf(factors[i]) +"%", (char)(KeyEvent.VK_A + i), factors[i], false, IMAGE_WIDTH );
			extended.add( extItm[i] );
		}



/*
		c1.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				// code here
				new FFTFrame( GUI.this, img, 1 );
			}
		} );
*/

		q.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				// code here
				dispose();
				System.exit(0);
			}
		});
		
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				dispose();
				System.exit(0);
			}
		} );
	}

	public void paint(Graphics g) {
	    // Draw the animated image
	    g.drawImage(lennaImage, insets.left, insets.top, this);
	}
}