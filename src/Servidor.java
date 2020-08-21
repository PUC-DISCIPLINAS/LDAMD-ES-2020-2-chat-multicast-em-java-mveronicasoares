import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
	public static void main(String args[]) {

		ServerSocket listenSocket = null;

		try {
			// Porta do servidor
			int serverPort = 7896;
			
			// Fica ouvindo a porta do servidor esperando uma conexao.
			listenSocket = new ServerSocket(serverPort);
			System.out.println("Servidor: ouvindo porta TCP/7896.");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				new Connection(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Listen socket:" + e.getMessage());
		} finally {
			if (listenSocket != null)
				try {
					listenSocket.close();
					System.out.println("Servidor: liberando porta TCP/7896.");
				} catch (IOException e) {
					/* close falhou */
				}
		}
	}

}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	static ArrayList <String> participantes = new ArrayList<String>();

	public Connection(Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Conecao:" + e.getMessage());
		}
	}

	public void run() {
		try {
			while (true){
				String data = in.readUTF();
				System.out.println("Recebido: " + data);
				String comando = data.split("-")[0];
				String nome = data.split("-")[1];
				switch (comando) {
					case "ingressar":
						participantes.add(nome);
						out.writeUTF("228.5.6.7");
						break;
					case "sair":
						participantes.remove(nome);
						break;
					default:
						System.out.println("ERRO: Nenhum comando valido.");
				}
				System.out.println("*** Participantes ***\n");
				for (String participante : participantes)
					System.out.println("<" + participante + ">");
			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			try {
				clientSocket.close();
				System.out.println("Servidor: fechando conex�o com cliente.");
			} catch (IOException e) {
				/* close falhou */
			}
		}
	}
}