import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;

public class FFTFrame extends Frame  {
	
	protected int[]			img;
	protected int			factor;
	protected int			blockSize;
	
	protected GUI			parent;
	
	private Insets 			insets;
	private ImageCanvas		imageCanvas;
	


	public FFTFrame( GUI parent, int[] mat, int factor, int blockSize )
	{
		super( "Image at " + factor + "%" );		
		
		this.parent = parent;
		this.factor = factor;
		this.blockSize = blockSize;
		this.img  = new int[mat.length];

		for( int i=0; i < mat.length; i++ )
				img[i] = mat[i];

		
		initListeners();	
		initFrame();
		
		this.pack();
		this.show();

		insets = getInsets();
		setSize(insets.left + GUI.IMAGE_WIDTH, insets.top + GUI.IMAGE_HEIGHT);
		
		doFFT();
	}

	protected 
	void initListeners() 
	{
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				dispose();
			}
		} );
/*		
		this.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent e ) {
				int	x = e.getX() - insets.left;
				int	y = e.getY() - insets.top;
				int color = img[x * GUI.IMAGE_WIDTH + y];
				
				System.out.println( "Color at (" + x + "," + y +") " + color );
			}
		} );
*/		
	}
	
	protected	
	void initFrame()
	{
		Dimension imageSize = new Dimension( GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT );
		imageCanvas = new ImageCanvas( imageSize );
		add( imageCanvas );
	}

	protected 
	void	doFFT()
	{
		double[][] FFT = FFTUtil.doFFT2( img, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT, blockSize, blockSize );

/*
		for( int i=0; i < FFT.length; i++ )
			System.out.println("FFT Re:"+FFT[i][0] + " Im:"+FFT[i][1] );
*/

		doCompress( FFT );

//		double[][] iFFT = FFTUtil.doIFFT2( FFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT );
		FFTUtil.doFFT2( FFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT, blockSize, blockSize, 1 );

/*
		for( int i=0; i < FFT.length; i++ )
			System.out.println("iFFT Re:"+iFFT[i][0] + " Im:"+iFFT[i][1] );
*/

		img = FFTUtil.normalizeValues(FFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT);	
		Image image = createImage( parent.getImageUtil().getImageObject( img ) );
		
		imageCanvas.changeImage( image );

/*
		for( int i=0; i < GUI.IMAGE_WIDTH; i++ )
			for( int j=0; j < GUI.IMAGE_HEIGHT; j++ )
				System.out.println("Img :"+img[i][j] );
*/
		
	}


	protected
	void doCompress( double[][] FFT)
	{
		int length = FFT.length;
		
		double[]	mag = new double[ length ];
		double[]	mag2 = new double[ length ];
		int			pos = 0;
		double		value = 0;
		double 		value2 = 0;

		int	scarti = 0;
		
		for( int i=0; i < length; i++ ) {
			mag[i] = mag2[i]= FFTUtil.magnitude( FFT[i][0], FFT[i][1] );
//				System.out.print(" mag:" + mag[j] );
		}

//		System.out.println("select: length: " + (length-1) + " pos: " + length * factor / 100 );
		value = value2 = Util.select( mag, 0, length-1, (length-1) * factor / 100 );
		

		for( int i=0; i < length; i++ )
		{
			if ( mag2[i] < value2 ) {
				FFT[i][0] = FFT[i][1] = 0;
				scarti++;
			}
		}
		System.out.println("Punti scartati:" + scarti );

		setTitle( factor + "% - " +scarti +" Points less" );	

	}


	void doBlockCompress( double[][] FFT)
	{
		int length = FFT.length;
		
		double[]	mag = new double[ length ];
		double[]	mag2 = new double[ length ];
		int			pos = 0;
		double		value = 0;
		double 		value2 = 0;

		int	scarti = 0, idx = 0;
		int	BLOCK_SIZE	= 8;
		
		for( int i=0; i < length; i+= BLOCK_SIZE ) {
			mag[idx] = mag2[idx] = FFTUtil.magnitude( FFT[i][0], FFT[i][1] );
			idx++;
		}

//		System.out.println("select: length: " + (length-1) + " pos: " + length * factor / 100 );
		value = value2 = Util.select( mag, 0, length-1, (length-1) * factor / 100 );
		

		for( int i=idx=0; i < length; i+= BLOCK_SIZE )
		{
			if ( mag2[idx++] < value2 ) {
				FFT[i][0] = FFT[i][1] = 0;
				scarti++;
			}
		}
		System.out.println("Punti scartati:" + scarti );

		setTitle( factor + "% - " +scarti +" Points less" );	

	}



}