import java.util.Iterator;
import java.util.LinkedList;

public class livraison {
	private Usine[] usines;
	private Prestataire p;
	private double cout;
	
	

public livraison(Usine[] usines, Prestataire p){
	this.usines = usines;
	this.p=p;
	this.cout=0;
	
}

public void resolutionLivraison(){
	int nbUsines=this.usines.length;
	int nbJour=this.usines[0].getJour();
	for (int j=0; j<nbJour;j++){
		for (int i=0;i<nbUsines;i++ ){
			Usine u = this.usines[i];
			livraisonUsine lu = new livraisonUsine(u,p,j);
			lu.livraisonprodUsine();
			cout+=lu.getCout();
			Iterator<Camion> iter1= u.getCamions().iterator();
			while(iter1.hasNext()){
				Camion c = iter1.next();
				c.jourSuivant();
			}
			
			
		}
		cout+=p.getCoutFinal(j);
		p.jourSuivant();
	}
	cout +=this.coutPenalite();

	}

public double getCoutLogistique(){
	return this.cout;
}

public double coutPenalite() {
	int nbUsines=this.usines.length;
	double coutPenalite=0;
	for (int i=0;i<nbUsines;i++){
		Iterator<Client> iter1= this.usines[i].getClients().iterator();
		Client client = iter1.next();
		coutPenalite+=client.coutPenalite();

		}
	
	return coutPenalite;
	
}


public Usine[] getUsine(){
	return this.usines;
}

public Prestataire getPrestataire(){
	return this.p;
	
}

}
