import java.util.LinkedList;
import java.util.List;

public class Client {

	private String id;
	private int dureeExp;
	private Point coord;
	private double demande;
	private double[] fenetreLivraison; 
	private double penalite;
	private double[]marchandiseJourLivree;
	private double[]marchandiseJourCamionLivree;
	private double[]marchandiseJourPrestLivree;
	private boolean etat;
	private Usine usine;
	
	
	/**
	 * Constructeur d'un Client
	 * @param id chaîne de caractère représentant l'identifiant du client
	 * @param coord objet de type Point représentant la position (coordonnées) du Client
	 * @param demande double représentant la demande du client
	 * @param d1 double représentant la valeur inférieur de la fenêtre de livraison
	 * @param d2 double représentant la valeur supérieur de la fenêtre de livraison
	 * @param penalite double représentant la valeur de la pénalité par unité de quantité
	 * @param duree entier représentant le nombre de jour total d'activité
	 */
	
	
	public Client( String id, Point coord, double demande, double d1, double d2,double penalite, int duree){
		this.id=id;
		this.dureeExp=duree;
		this.coord=coord;
		this.demande=demande;
		this.fenetreLivraison=new double[2];
		this.fenetreLivraison[0]= d1;
		this.fenetreLivraison[1]= d2;
		this.marchandiseJourLivree= new double[dureeExp];
		this.marchandiseJourCamionLivree= new double[dureeExp];
		this.marchandiseJourPrestLivree= new double[dureeExp];
		this.etat=false;
		this.penalite=penalite;
	}
	
	/**
	 * Accesseur
	 * @return le jour auquel il faut livrer le client ie à la valeur inférieur de la fenêtre 
	 */
	
	public double jourALivrer(){
		return this.fenetreLivraison[0];
	}
	
	/**
	 * Accesseur
	 * @param jour 
	 * @return la quantité de marchandise livrée au client le jour j
	 */
	
	public double getMarchandise( int jour){
		return this.marchandiseJourLivree[jour];
	}
	
	/**
	 * Méthode modifiant l'attribution du client à l'usine
	 * @param u Usine
	 */
	
	public void setUsine(Usine u){
		this.usine=u;
		
	}
	
	/**
	 * Accesseur
	 * @return L'usine affectée au client
	 */
	
	public Usine getUsine(){
		return this.usine;
	}
	
	/**
	 * Accesseur
	 * @param j jour
	 * @return la penalite du client si le jour n'est pas compris dans sa fenêtre de livraison
	 */
	
	
	public double getPenalites(int j){
		if( j>= getD1()-1 && j<= getD2()){
			return 0;
		}
		 else {
			return this.penalite;
		}
	}
	
	
	/**
	 * @return le cout de la penalite qui est proportionnel à la quantité livrée par jour de retard
	 */
	
	public double coutPenalite(){
		double somme=0;
		for( int i=0; i<this.dureeExp; i++){
			somme= somme + getPenalites(i)*this.marchandiseJourLivree[i];
			
		}
		return somme;
	}
	
	/**
	 * Méthode affectant un tableau de quantité de marchandise livrée 
	 * @param tab
	 */
	
	public void setMarchandiseTot( double[] tab){
		this.marchandiseJourLivree=tab;
	}

	/**
	 * @param j jour
	 * @return la demande du client le jour j  
	 */
	
	public double getDemandeRestante( int j){
		double demandeRest= this.demande;
		for(int i=0; i<j; i++){
			demandeRest= demandeRest-this.marchandiseJourLivree[j];
		}
		return demandeRest;
	}	
		
	/**
	 * Accesseur
	 * @return la valeur inférieur de la fenêtre de livraison
	 */
	
	public double getD1(){
		return this.fenetreLivraison[0];
	}
	
	/**
	 * Accesseur
	 * @return la valeur supérieur de la fenêtre de livraison
	 */
	
	public double getD2(){
		return this.fenetreLivraison[1];
	}
	
	/**
	 * Méthode modifiant la quantité q le jour j
	 * @param j jour
	 * @param q quantité
	 */
	
	public void setMarchandise(int j, double q){
		this.marchandiseJourLivree[j]=q;
	}
	
	/**
	 * Accesseur
	 * @return la demande du client
	 */
	
	public double getDemande() {
		return this.demande;
	}
	
	/**
	 * Accesseur
	 * @return l'état du client ie True s'il a été livré, 1 sinon
	 */

	public boolean getEtat() {
		return this.etat;
	}
	
	/**
	 * Méthode modifiant l'état du client
	 * Si le client a été livré son état devient False (réinitialisation)
	 */
	
	public void changeEtat() {
		if (getEtat()) {
			this.etat=false;
		}
	}
	
	/**
	 * @param jourlivrer
	 * @return True s'il y a une pénaltité (ie que le client a été livré en dehors de sa fenêtre de livraison), False sinon 
	 */
	
	public boolean estPenalite(int jourlivrer) {
		if((jourlivrer < this.fenetreLivraison[0]) || (jourlivrer > this.fenetreLivraison[1])){
			return true;
		} else {
		return false;	
		}
	}
	
	/**
	 * Accesseur
	 * @return l'identifiant du client
	 */
	
	public String getId() {
		return this.id;
	}

	/**
	 * Accesseur
	 * @return la position du client
	 */

	public Point getCoord(){
	return this.coord;
	}
	
	/**
	 * Accesseur
	 * @param j jour
	 * @return la quantité de marchandise livrée par un camion le jour j
	 */
	
	public double getMarchandiseJourCamionLivree(int j){
		return this.marchandiseJourCamionLivree[j];
	}
	
	/**
	 * Accesseur
	 * @param j jour
	 * @return la quantité de marchandise livrée par un prestataire le jour j
	 */
	
	public double getMarchandiseJourPrestLivree(int j){
		return this.marchandiseJourPrestLivree[j];
	}
	
	/**
	 * Méthode affectant une quantité de marchandise livrée par un camion le jour j
	 * @param quantite
	 * @param j jour
	 */
	public void setMarchandiseJourCamionLivree(double quantite,int j){
		this.marchandiseJourCamionLivree[j]=quantite;
	}
	
	/**
	 * Méthode affectant une quantité de marchandise livrée par un prestataire le jour j
	 * @param quantite
	 * @param j jour
	 */
	public void setMarchandiseJourPrestLivree(double quantite, int j){
		this.marchandiseJourPrestLivree[j]=quantite;
	}

}

