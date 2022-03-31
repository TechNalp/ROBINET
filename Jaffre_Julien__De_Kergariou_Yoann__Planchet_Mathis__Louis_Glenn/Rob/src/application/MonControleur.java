package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class MonControleur implements Initializable {
	protected Socket sock;
	protected PrintStream ps ;
	protected BufferedReader  br ;
	boolean lancer_script=false;
	@FXML
	private TextField adresse ;
	@FXML
	private TextField port ;
	@FXML
	private Button send ;
	@FXML
	private Button connec ;
	@FXML
	private TextField script;
	@FXML
	private Button stop;
	@FXML
	private Button log_rec;
	@FXML
	private TextArea log;
	@Override
	public void initialize(URL location, ResourceBundle resources) {}


	@FXML
	public void connection() {
		String adr=adresse.getText();
		int por=Integer.parseInt(port.getText());
		try {
			this.sock=new Socket(adr,por);
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			//	BufferedWriter be =new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			ps = new PrintStream(sock.getOutputStream());
			log.appendText("connexion réussite\n");
		}catch(java.net.ConnectException e) {			
			log.appendText("**le serveur n'est pas allumé**\n");

		}catch( java.net.SocketException e) {
			log.appendText("** le serveur est déconnecté\n");
		}catch(Exception e) {
			System.out.print(e);
		}
	}
	public void envoie_script() {

		Runnable task=()->{
			String scrip=script.getText();
			ps.println(scrip);
			String logmes="";
			try {
				lancer_script=true;
				do {
					logmes=br.readLine();
					log.appendText(logmes+"\n");
				}
				while(logmes.compareTo("fin")!=0);
				lancer_script=false;
				

			}catch(java.net.SocketException e) {
				lancer_script=false;
				System.out.print("tache interrompue");
			}catch(IOException e) {	e.printStackTrace();}
		};
			if(lancer_script==false)
				new Thread(task).start();

	}
	public void stop_script() {
		if(lancer_script=true)ps.println("stop");

	}

}
