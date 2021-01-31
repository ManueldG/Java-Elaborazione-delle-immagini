import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;

public class ExtendedFFTFrame extends FFTFrame   {
	
	private	ImageCanvas	imageCanvas;
	private Istogramma	istogramma;
	private	Magnitude	magnitudes;
	private TextArea	text;


	public ExtendedFFTFrame( GUI parent, int[] mat, int factor )
	{
		super( parent, mat, factor, GUI.IMAGE_WIDTH );
//		super( "Extended image viewer at " + factor + "%" );		
		
		this.pack();
		this.show();
		
		doFFT();
	}

	protected
	void initFrame()
	{
		Dimension imageSize = new Dimension( GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT );

		imageCanvas = new ImageCanvas( imageSize );
		istogramma = new Istogramma();
		magnitudes = new Magnitude();
		
//		Panel p = new Panel( new GridLayout(2, 1, 10, 10) );
		Panel p = new Panel( new BorderLayout(10, 10) );
//		Panel top = new Panel( new FlowLayout() );
//		Panel top = new Panel( new GridLayout(1, 2) );
		Panel top = new Panel( new BorderLayout(10, 10) );
		p.add( top, BorderLayout.CENTER );
		
		top.add( imageCanvas, BorderLayout.WEST );
//		top.add( new Panel( new FlowLayout()).add(istogramma), BorderLayout.CENTER );
		top.add( istogramma, BorderLayout.CENTER );
		p.add( magnitudes, BorderLayout.SOUTH );
		
//		text = new TextArea(10, 5 );
//		p.add( text );
		
		add(p);

	}


	protected
	void	doFFT()
	{
		double[][] FFT = FFTUtil.doFFT2( img, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT );

		doCompress( FFT );

//		double[][] iFFT = FFTUtil.doFFT2( FFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT, 1 );
//		img = FFTUtil.normalizeValues(iFFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT);	

		magnitudes.compute( FFT );

		FFTUtil.doFFT2( FFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT, 1 );
		img = FFTUtil.normalizeValues(FFT, GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT);	
		
		Image image = createImage( parent.getImageUtil().getImageObject( img ) );
		Dimension imageSize = new Dimension( GUI.IMAGE_WIDTH, GUI.IMAGE_HEIGHT );
//		int []pixels = imageCanvas.grabImage( image, imageSize );
		
		imageCanvas.changeImage( image );
//		istogramma.compute( pixels );
		istogramma.compute( img );
	}

}