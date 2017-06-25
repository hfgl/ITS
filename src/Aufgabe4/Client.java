package Aufgabe4;

/* Simulation einer Kerberos-Session mit Zugriff auf einen Fileserver
 /* Client-Klasse
 */

import java.util.*;

public class Client extends Object {

	private KDC myKDC; // Konstruktor-Parameter

	private String currentUser; // Speicherung bei Login nötig
	private Ticket tgsTicket = null; // Speicherung bei Login nötig
	private long tgsSessionKey; // K(C,TGS) // Speicherung bei Login nötig

	// Konstruktor
	public Client(KDC kdc) {
		myKDC = kdc;
	}

	public boolean login(String userName, char[] password) {
		/*
		Aufgabe: TGS‐Ticket für den übergebenen Benutzer vom KDC (AS) holen (TGS‐Servername:
		myTGS) und zusammen mit dem TGS‐Sessionkey und dem UserNamen speichern.
		Rückgabe: Status (Login ok / fehlgeschlagen)
		*/

		long nonce = generateNonce();
		TicketResponse ticketResponse = myKDC.requestTGSTicket(userName,"myTGS",nonce);
		if(ticketResponse != null && ticketResponse.isEncrypted()) {
			//Nonce check?
			if(ticketResponse.getNonce() == nonce){
				System.out.println("Nonce is ok");
			}
			//ticketResponse should be encrypted with key of client
			if(ticketResponse.decrypt(generateSimpleKeyFromPassword(password))) {
				//Verschlüsselt mit K(S)
				tgsTicket = ticketResponse.getResponseTicket();
				//K(C,TGS)
				tgsSessionKey = tgsTicket.getSessionKey();
				currentUser = tgsTicket.getClientName();
				return true;
			}
		}
		return false;
	}

	public boolean showFile(Server fileServer, String filePath) {
		/*
		Aufgabe: Serverticket vom KDC (TGS) holen und „showFile“‐Service beim übergebenen Fileserver
		anfordern.
		*/

		///Anforderung des Servertickets mit TGS-Ticket + K(Client,K(C,TGS))
		long currentTime = (new Date()).getTime();
		Auth auth = new Auth(currentUser, currentTime);
		//Übergabe von K(C,TGS) via auth
		auth.encrypt(tgsSessionKey);
		TicketResponse response = myKDC.requestServerTicket(tgsTicket, auth, fileServer.getName(), generateNonce());

		if(response.getResponseTicket().decrypt(tgsSessionKey)){
			//Anforderung eines Dienstes von Server S mit Serverticket + K(C,S)
			auth = new Auth(currentUser, response.getResponseTicket().getStartTime());
			auth.encrypt(response.getSessionKey());
			fileServer.requestService(response.getResponseTicket(), auth, "showFile", filePath);
			return true;
		}
		return false;
	}

	/* *********** Hilfsmethoden **************************** */

	private long generateSimpleKeyFromPassword(char[] passwd) {
		// Liefert einen eindeutig aus dem Passwort abgeleiteten Schlüssel
		// zurück, hier simuliert als long-Wert
		long pwKey = 0;
		if (passwd != null) {
			for (int i = 0; i < passwd.length; i++) {
				pwKey = pwKey + passwd[i];
			}
		}
		return pwKey;
	}

	private long generateNonce() {
		// Liefert einen neuen Zufallswert
		long rand = (long) (100000000 * Math.random());
		return rand;
	}
}
