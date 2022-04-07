package version_5.copy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ImageTh implements Runnable{
	ByteArrayOutputStream img;
	byte[] size;
	OutputStream nos;
	public ImageTh(ByteArrayOutputStream br,byte[] b,OutputStream os) {
		this.img=br;
		this.size=b;
		this.nos=os;
	}
		@Override
		public void run() {
			
			try {
				nos.write(size);
				nos.write(img.toByteArray());
				nos.flush();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			

		}

	}
