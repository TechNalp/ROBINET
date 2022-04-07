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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;



import javafx.scene.image.ImageView;

public class ImageTh implements Runnable {
	BufferedInputStream br;
	BufferedInputStream bw;
	BufferedReader br1;
	ImageView imge;
	int port;
	Socket sock;

	public ImageTh(String adr,int port,ImageView img){
		try {
			this.sock=new Socket(adr,port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.imge=img;
	
	}
	@Override
	public void run() {

		try {

			InputStream inputStream = sock.getInputStream();
			byte[] sizeAr = new byte[4];
			while(true) {
				//lecture taille image
				this.sock.setSoTimeout(1000);
				try {
					inputStream.read(sizeAr);
				}catch (java.net.SocketTimeoutException e){
					if(Thread.currentThread().isInterrupted()){
						this.sock.close();
						return;
					}
					continue;
				}
				//conversion
				int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

				byte[] imageAr = new byte[size];
				//lecture image
				while(true) {
					try {
						inputStream.read(imageAr);
					} catch (java.net.SocketTimeoutException e) {
						if(Thread.currentThread().isInterrupted()){
							this.sock.close();
							return;
						}
						continue;
					}
					break;
				}

				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
				ImageIO.write(image, "png", new File("./tmp.png"));
				imge.setImage(new javafx.scene.image.Image(new FileInputStream(new File("./tmp.png"))));

			}




		}catch (SocketException e){
			return;
		}catch(IOException e) {e.printStackTrace();}

	}

}
