package application;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.ImageView;

public class ImageTh implements Runnable {
	BufferedInputStream br;
	BufferedInputStream bw;
	BufferedReader br1;
	ImageView imge;
	int port;
	Socket sock;
	private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
	private static final ColorModel RGB_OPAQUE =
	    new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
	public ImageTh(String adr,int port,ImageView img,BufferedReader br1){
		try {
			this.sock=new Socket(adr,port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.imge=img;
		this.br1=br1;
	}
	@Override
	public void run() {
		File tmp=new File("./image.jpg");
		File tmp2=new File("./image2.jpg");
		tmp.canRead();
		tmp.canWrite();

		try {
		/*	BufferedInputStream br= new BufferedInputStream(sock.getInputStream());
			BufferedOutputStream bw= new BufferedOutputStream(new FileOutputStream(tmp));
			BufferedOutputStream bw2= new BufferedOutputStream(new FileOutputStream(tmp2));
			int y=0;
			int x=0;
			int taille;
		
			byte[] buf=new byte[256];
				taille=Integer.parseInt(br1.readLine());
				System.out.println(taille);
				while(taille>((x=x+br.read(buf)))) {	//System.out.println(x); 
					bw.write(buf);	bw.flush();}
				
				bw.flush();
				System.out.println(x+"lol");
				*/
			InputStream inputStream = sock.getInputStream();
			 byte[] sizeAr = new byte[4];
		        inputStream.read(sizeAr);
		        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
		        System.out.println(size);


	   //     BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

	     //   System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
	      //  ImageIO.write(image, "jpg", new File("C:\\Users\\Jakub\\Pictures\\test2.jpg"));

				/*try {
				java.awt.Image img = Toolkit.getDefaultToolkit().getImage("image.jpeg");
				MediaTracker tracker = new MediaTracker(new Component(){});
				tracker.addImage(img, 0);
				try   {tracker.waitForID(0);}
				catch (InterruptedException e){}
				PixelGrabber pg = new PixelGrabber(img, 0, 0, -1, -1, true);
				System.out.println(pg.grabPixels());
					
					 
					int width = pg.getWidth(), height = pg.getHeight();
					System.out.println(width+""+height);
					DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
			//		WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
				//	BufferedImage bi = new BufferedImage(RGB_OPAQUE, raster, false, null);
				
				//	imge.setImage(SwingFXUtils.toFXImage(bi, null));
			
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
								*/
				
				//imge.setImage(new javafx.scene.image.Image(new FileInputStream(new File("./image.jpg"))));
				
			
				
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		
		
		}catch(IOException e) {e.printStackTrace();}

	}

}
