import java.util.List;

public class Client {

	private String id;
	private int dureeExp;
	private Point coord;
	private double demande;
	private double[] fenetreLivraison; 
	private double penalite;
	private double[]marchandiseJourLivree; 
	private boolean etat;
	private Usine usine;
	
	public Client( String id, Point coord, double demande, double d1, double d2,double penalite, int duree){
		this.id=id;
		this.dureeExp=duree;
		this.coord=coord;
		this.demande=demande;
		this.fenetreLivraison=new double[2];
		this.fenetreLivraison[0]= d1;
		this.fenetreLivraison[1]= d2;
		this.marchandiseJourLivree= new double[dureeExp];
		this.etat=false;
	}
	
	public double jourALivrer(){
		return this.fenetreLivraison[0];
	}
	
	
	public double getMarchandise( int jour){
		return this.marchandiseJourLivree[jour];
	}
	
	public void setUsine(Usine u){
		this.usine=u;
		
	}
	
	
	public Usine getUsine(){
		return this.usine;
	}
	
	
	
	
	public double getPenalites(int j){
		if( j>= getD1()-1 && j<= getD2()){
			return 0;
		}
		 else {
			return this.penalite;
		}
	}
	
	
	public double coutPenalite(){
		double somme=0;
		for( int i=0; i<this.dureeExp; i++){
			somme= somme + getPenalites(i)*this.marchandiseJourLivree[i];
			
		}
		return somme;
	}
	
	
	
	public void setMarchandiseTot( double[] tab){
		this.marchandiseJourLivree=tab;
	}

	
	
	
	
	public double getDemandeRestante( int j){
		double demandeRest= this.demande;
		for(int i=0; i<j; i++){
			demandeRest= demandeRest-this.marchandiseJourLivree[j];
		}
		return demandeRest;
	}	
		
	public double getD1(){
		return this.fenetreLivraison[0];
	}
	
	public double getD2(){
		return this.fenetreLivraison[1];
	}
		
	
	public void setMarchandise(int j, double q){
		this.marchandiseJourLivree[j]-=q;
	}
	
	

	
	
	public double getDemande() {
		return this.demande;
	}
	
	

	
	

	public boolean getEtat() {
		return this.etat;
	}
	
	public void changeEtat() {
		if (getEtat()) {
			this.etat=false;
		}
	}
	
	public boolean estPenalite(int jourlivrer) {
		if((jourlivrer < this.fenetreLivraison[0]) || (jourlivrer > this.fenetreLivraison[1])){
			return true;
		} else {
		return false;	
		}
	}
	
	public String getIdclient() {
		return this.id;
	}


	

public Point getCoord(){
	return this.coord;
}

}
