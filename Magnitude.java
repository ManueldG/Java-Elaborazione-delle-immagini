import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public
class Magnitude extends Canvas {
	
	private double[]		magnitude;
	private double[]		phase;
	
	private	double			max_magnitude;
	private	double			min_magnitude;
	
	private final static int	WIDTH = 280;
	private final static int	HEIGHT = 100;

	public 
	Magnitude()
	{
		super();
		setBackground(Color.BLACK);
		setForeground(Color.LIGHT_GRAY);
		
		this.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent e ) {
				int	x = e.getX();
				int	y = e.getY();
				double mag = magnitude[x];
				
				System.out.println( "Magnitude: Magnitude of gray " + x + " :" + mag );
			}
		} );

		setSize( WIDTH, HEIGHT );
	}
	
	public
	Magnitude( double[][] FFT )
	{
		this();
		compute( FFT );
	}
	
	public
	void compute( double[][] FFT )
	{
		magnitude = new double[ FFT.length ];
		phase = new double[ FFT.length ];
		
		max_magnitude = min_magnitude = 0;
		
		for( int i=1; i < magnitude.length; i++ ) {
			double	mag = FFTUtil.magnitude( FFT[i][0], FFT[i][1] ); 
			magnitude[i] = mag;
			
			if ( mag > max_magnitude ) {
				max_magnitude = mag;
			}
			else
			if ( mag < min_magnitude ) {
				min_magnitude = mag;
			}


		}


		System.out.println( "Magnitude: highest magnitude  "+ max_magnitude + " lowest magnitude: " + min_magnitude );

	}
	
	public
	double  getValueAt( int index )
	{
		return magnitude[index];
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
		Dimension	size = getSize();
		
		int	x_offset = 10;
		int	y_top_value = 50;
		int	x_step = 2;
		int	MAX_ELEM = (size.width - x_offset) / x_step;
		int	yCenter = HEIGHT / 2;
		
		double scale = ( (double)y_top_value / max_magnitude);
		
//		g.setColor( Color.BLACK );
//		g.drawRect( 0 + x_offset, 0, size.width - x_offset, HEIGHT );
		g.drawLine( 0, yCenter, size.width, yCenter);

		for( int i = 0; i < size.width; i += 256 )
		{
			int	x = i + x_offset;
			g.drawLine( x, 5 , x, yCenter + 5 );
		}

		
		g.setColor( Color.YELLOW );
		

		int	max_elems = magnitude.length;
		if ( max_elems > MAX_ELEM ) max_elems = MAX_ELEM;

//		System.out.println( "Magnitude: yScale " + scale );

		for(int i=0; i < max_elems; i++ ) {
			int	y = (int) (magnitude[i*x_step] * scale);
			int x = x_offset + x_step * i;
			g.drawLine( x, yCenter, x,  yCenter - y);

//			System.out.println( "Magnitude: x: " + x + " y: " +y );
			
		}

//		System.out.println( "Magnitude: max_elems " + max_elems  );
	}
}