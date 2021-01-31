import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public
class Istogramma extends Canvas {
	
	private int	grays[] = new int[ 256 ];
	private int	max_value = 0;
	private int	max_frequent = 0;

	private final static int	x_offset = 10;
	private final static int	y_offset = 10;

	
	private final static int	WIDTH = 280;
	private final static int	HEIGHT = 256;

	private	Dimension 	size = null;


	public 
	Istogramma()
	{
		super();
		
		setBackground(Color.BLACK);
		setForeground(Color.LIGHT_GRAY);

		this.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent e ) {
				int	x = (int)((e.getX()-x_offset) * 256.0 / size.width);
				int	y = e.getY();
				
				if ( x < 0 || x > grays.length ) return;
				
				int freq = grays[x];
				System.out.println( "Istogramma: Frequency of gray " + x + " :" + freq );
				
			}
		} );

	}
	
	public
	Istogramma( int[] pixels )
	{
		this();
		compute( pixels );
	}
	
	public
	void compute( int[] pixels )
	{
//		System.out.println( "Istogramma: pixels " + pixels.length );
		for( int i=0; i < pixels.length; i++ ) {
			int	gray = pixels[i] & 0xff;
			if ( gray < 0 || gray > 255 ) {
				System.out.println( "Istogramma: pixels colour out of range " + gray );
				continue;
			}
			
			grays[ gray ]++;
			
			if ( grays[gray] > max_value ) {
				max_value = grays[gray];
				max_frequent = gray;
			}
		}
		
		System.out.println( "Istogramma: most frequent gray "+ max_frequent + " freq: " + max_value);

	}
	
	public
	int getValueAt( int index )
	{
		return grays[index];
	}
	
	public
	Dimension getMinimumSize()
	{
		return new Dimension( WIDTH, HEIGHT );
	}
	
	public
	Dimension getPreferredSize() {
		return new Dimension( WIDTH, HEIGHT );
	}
	
	public
	void paint( Graphics g )
	{
		size = getSize();
		int		height = size.height - y_offset;

		double yScale = (double)(height) / max_value;
		double xScale = (double)(size.width) / 256.0;

		
//		g.drawRect( 0 + x_offset, 0, 256 + x_offset, HEIGHT );
		g.drawRect( 0 + x_offset, 0, size.width, height );

		for( int i = 0; i < grays.length; i += 25 )
		{
			int	x = (int)(i * xScale) + x_offset;
			int	y = height;
			g.drawLine( x, y - 5 , x, y + 5 );
		}
		
		g.setColor( Color.RED );
		
		int	last_x = 0 + x_offset;
		int	last_y = height;
		
//		System.out.println( "Istogramma: max_value " + max_value + " yScale " + yScale );
		
		for(int i=0; i < grays.length; i++ ) {
			int	y = height - (int)(grays[i] * yScale);
			int x = (int) (x_offset + i * xScale);
			g.drawLine( last_x, last_y, x,  y );
			
			last_x = x; last_y = y;
			
		}
	}

}