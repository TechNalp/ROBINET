package version_5.copy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThEcouteStop implements Runnable {
	Socket  so;
	 
	Thread thread;
	public ThEcouteStop(Socket sock,Thread th) {
		this.so=sock;
		this.thread=th;

	}
	@Override
	public void run() {
		BufferedReader br;
		
		
		
		try {
		
			br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		
			String s=br.readLine();
			
			if(s.equals("stop")) {
		//		ps.println("interrompue");
				thread.interrupt();
			}
			Thread.sleep(0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
