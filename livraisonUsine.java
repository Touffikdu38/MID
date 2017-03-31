import java.util.Iterator;
import java.util.LinkedList;

public class livraisonUsine {
	private Usine usine;
	private Prestataire prestExt;
	private int j;
	private double cout;

	/**
	 * Constructeur d'une livraison usine
	 * @param u objet de type Usine représentant une usine
	 * @param prest objet de type Prestataire représentant un prestataire
	 * @param j entier représentant le jour
	 */
	
	public livraisonUsine(Usine u, Prestataire prest ,int j){
		this.usine=u;
		this.prestExt=prest;
		this.j=j;
		this.cout=0;
	}

	/**
	 * Méthode qui livre la quantité restante à tout les clients d'une usine grâce au prestataire
	 * @param p
	 * @param quantite
	 */

	public void livraisonPrest(Prestataire p, double quantite){
		LinkedList<Client> clients = this.usine.getClients();
		Iterator<Client> iter = clients.iterator();
		while(iter.hasNext() && quantite>=0) {
			Client c = iter.next();
			if (c.getMarchandise(this.j)>0) {
				p.prestDelivre(c,usine, min(quantite,c.getDemandeRestante(j)),j);
				quantite=quantite-min(quantite,c.getDemandeRestante(j));
			}
		}
	}

	/**
	 * @param q1 quantité 1
	 * @param q2 quantité 2
	 * @return le minimum des deux quantités
	 */

	public double min(double q1, double q2){
		if (q1<= q2){
			return q1;
		} else{
			return q2;
		}
	}
	
	/**
	 * Accesseur
	 * @return le coût du prestataire
	 */
	
	public double getCout(){
		return  this.cout;
	}
	
	/**
	 * Méthode qui livre tout les clients d'une usine grâce au prestataire
	 */
	
	public void livraisonToutPrest(){
		Iterator<Client> iter=this.usine.getClients().iterator();
		while(iter.hasNext()){
			Client client=iter.next();
			if (client.getMarchandise(j)!=0){
				this.prestExt.prestDelivre(client, this.usine, client.getMarchandise(j), j);
				this.cout+= this.prestExt.getCoutTransport(j)*this.usine.getCoord().distance(client.getCoord()) + this.prestExt.getCoutFixe(j);
			}
		}
		
	}

}
