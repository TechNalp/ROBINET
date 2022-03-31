package version_ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ThEcouteStop implements Runnable {
	BufferedReader br;
	PrintStream ps;
	Thread thread;
	public ThEcouteStop(BufferedReader sock,PrintStream ps) {
		this.br=sock;
		this.ps=ps;

	}
	@Override
	public void run() {

		try {
		
			String s=br.readLine();
			
			if(s.equals("stop")) {
				ps.println("interrompue");
				Thread.currentThread().interrupt();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
