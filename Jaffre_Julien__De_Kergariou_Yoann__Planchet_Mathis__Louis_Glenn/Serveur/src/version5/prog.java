package version5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class prog {
	
	public static void main(String[] args){
		System.out.println("Le Serveur tout court");
		boolean r=true;
	
			ServerSocket serveur;
			try {
				serveur = new ServerSocket(8000);
				while(r) {
					Socket socket = serveur.accept();
					Serveur sct = new Serveur(socket);
					Thread th =new Thread(sct);
					th.start();
					
				}
				serveur.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
			
			
	}

}