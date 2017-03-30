import java.util.Iterator;
import java.util.LinkedList;

public class livraisonUsine {
	private Usine usine;
	private Prestataire prestExt;
	private int j;
	private double cout;

	
public livraisonUsine(Usine u, Prestataire prest ,int j){
	this.usine=u;
	this.prestExt=prest;
	this.j=j;
	this.cout=0;
}







public Client lePlusProche(Camion camion, LinkedList<Client> clients){
	int i=0;
	int j=0;
	Iterator<Client> iter = clients.iterator() ;
	double distance= camion.getCoord().distance(clients.getFirst().getCoord());
	
	while (iter.hasNext()) {
		Client c = iter.next() ;
	
		if (distance < camion.getCoord().distance(c.getCoord())){
			distance=camion.getCoord().distance(c.getCoord());
			j=i;
		}
		i++;
	}
	return clients.get(j);
}



public void livraisonPrest(Prestataire p, double quantite){
	LinkedList<Client> clients = this.usine.getClients();
	Iterator<Client> iter = clients.iterator();
	while(iter.hasNext() && quantite>=0) {
		Client c = iter.next();
		if (c.getMarchandise(this.j)>0) {
			p.prestDelivre(c,usine, getMarchandises2(quantite,c.getDemandeRestante(j)),j);
			quantite=quantite-getMarchandises2(quantite,c.getDemandeRestante(j));
		}
	}
	}
	



public double getMarchandises2(double q1, double q2){
	if (q1<= q2){
		return q1;
	} else{
		return q2;
	}
}







public void livraisonCam(Camion c){
	LinkedList<Client> clients= this.usine.getClients();
	Client clientProche= lePlusProche(c, clients);
	while(c.peutRetourner(clientProche, this.j) && c.getCapaUtilisee()>0 && clientProche.getMarchandise(this.j)>0 ){
		c.delivre(clientProche,getMarchandises2(c.getCapaUtilisee(), clientProche.getMarchandise(this.j)),this.j);
		clientProche=lePlusProche(c, clients);
	}
	c.retourUsine();
}






public void livraisonprodUsine(){
	
	Iterator<Camion> iter= this.usine.getCamions().iterator();
	double quantitePrestataire=this.usine.getQuantiteALivrer(this.j);
	
	while(iter.hasNext() && quantitePrestataire>=0){
		Camion c= iter.next();
		c.addCapacite(quantitePrestataire);
		quantitePrestataire= quantitePrestataire - c.getCapaUtilisee();
	}

	Iterator<Camion> iter2= this.usine.getCamions().iterator(); 
	while (iter2.hasNext()){
		Camion c = iter2.next();
		if( c.getCapaUtilisee()>0){
			livraisonCam (c);
			quantitePrestataire=quantitePrestataire+c.getCapaUtilisee();
		}
	}
	
	if (quantitePrestataire!=0){// && quantiteLivree>=getMarchandiseALivrer(j) (condition de rÃƒÂ©alisabilitÃƒÂ©)
			livraisonPrest(prestExt, quantitePrestataire);
		}
			
	this.cout= this.cout +  this.prestExt.getCout();
	
			
	}

public double getCout(){
	return  this.cout;
}


}
