package redes;

import java.net.*;
import java.io.*;

public class MulticastPeer {
	public static void main(String args[]) {
		
		// args  prov� o conte�do da mensagem e o endere�o  do grupo multicast (p. ex. "228.5.6.7")
		
		MulticastSocket mSocket = null;
		
		try {
			InetAddress groupIp = InetAddress.getByName(args[1]);
			
			mSocket = new MulticastSocket(6789);
			mSocket.joinGroup(groupIp);
			
			byte[] message = args[0].getBytes();
			DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, 6789);
			mSocket.send(messageOut);
			byte[] buffer = new byte[1000];
			for (int i = 0; i < 3; i++) { // get messages from others in group
				DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
				mSocket.receive(messageIn);
				System.out.println("Recebido:" + new String(messageIn.getData()).trim());
			}
			mSocket.leaveGroup(groupIp);
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (mSocket != null)
				mSocket.close();
		}
	}

}
