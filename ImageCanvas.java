import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public
class ImageCanvas extends Canvas {
	
	private static final int	MIN_WIDTH = 64;
	private static final int	MIN_HEIGHT = 64;

	private Image		image;
	private Dimension 	size;

	public ImageCanvas( Dimension d ) {
		this( null, d );
	}

	public ImageCanvas( Image img, Dimension d )
	{
		super();
		image = img;
		size = d;
		
		
		this.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent e ) {
				int	x = e.getX();
				int	y = e.getY();
				int color = x*y;
				
/*				
				
				if ( color > 128 ) color -= 128;
				else
				if ( color < 128 ) color += 128;
*/				 
				System.out.println( "Color at (" + x + "," + y +") " + color );
			}
		} );


		
	}
	
	public
	void changeImage( Image img ) {
		image = img;
	}
	
	public
	Dimension getMinimumSize()
	{
		return new Dimension( MIN_WIDTH, MIN_HEIGHT );
	}
	
	public
	Dimension getPreferredSize() {
		return new Dimension(size);
	}
	
	public
	void paint( Graphics g )
	{
		g.drawImage(image, 0, 0, getBackground(), this );
	}


	
	public static
	int[] grabImage( Image image, Dimension size )
	{
		int[] data = new int[ size.width * size.height ];
		PixelGrabber pg = new PixelGrabber( image, 0, 0, size.width, size.height, data, 0, size.width );
		
		try {
			pg.grabPixels();
		}
		catch( InterruptedException e ) {
			System.err.println( "ImageCanvas: Interrupted whlie grabbing pixels ");
			return null;
		}
		
		if ((pg.status() & ImageObserver.ABORT) != 0 ){
			System.err.println( "ImageCanvas: pixel grab anprted or errored" );
			return null;
		}
		
		return data;
	}
}