

public class Probleme {
	
	
	private Donnees donnee;
	private Affectation affect;
	private Production prod;
	private livraison liv;


	public Probleme (String nomfichier){
		this.donnee = new Donnees(nomfichier);	
		this.affect = new Affectation(this.donnee.getUsines(), this.donnee.getClients(), this.donnee.getCamions());
		affect.plClient();
		affect.associeCamions();
		this.prod = new Production(this.affect,this.donnee.getDuree());
		this.prod.plProd();
		this.prod.plStock();
		this.prod.setVentes();
		this.liv =new livraison(this.prod.getUsines(),this.donnee.getPrestataire());
		liv.resolutionLivraison();
	}
	

	public double getCout(){
		double coutProd= this.prod.getCoutProdStock();
		double coutLog = this.liv.getCoutLogistique();
		return coutProd+coutLog;
	}
	
	
	
	public livraison getLiv(){
		return this.liv;
	}
	
	public Affectation getAffect(){
		return this.affect;
	}
	
	public Production getProd(){
		return this.prod;
	}
	
	

	
	                                

}
