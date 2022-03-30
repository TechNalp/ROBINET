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
	boolean lo=false;
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
	public void log_recup() {
		if(lo==true) {
			lo=false;
			ps.println("oui");
			try {
				log.appendText(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@FXML
	public void connection() {
		String adr=adresse.getText();
		int por=Integer.parseInt(port.getText());
		try {
			this.sock=new Socket(adr,por);
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		//	BufferedWriter be =new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

			 ps = new PrintStream(sock.getOutputStream());
			 
		}catch(java.net.ConnectException e) {			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("**le serveur n'est pas allumé**");
			alert.showAndWait();

		}catch( java.net.SocketException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("** le serveur est déconnecté");
			alert.showAndWait();
		}catch(Exception e) {
			System.out.print(e);
		}
	}
	public void envoie_script() {
		Runnable task=()->{
		String scrip=script.getText();
			ps.println(scrip);
	
			try {
				System.out.print(br.readLine());
				lo=true;
	
			}catch(java.net.SocketException e) {
				System.out.print("tache interrompue");
			}catch(IOException e) {	e.printStackTrace();}
		};
		new Thread(task).start();
		
	}
	public void stop_script() {
	
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
