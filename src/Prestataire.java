public class Prestataire {

	private double[] coutFixe;
	private double[] coutTransport;
	private double distanceUtilise;
	private double distance;
	private double couts;
	private int dureeExp;
	private String id;

	
	/**
	 * Constructeur d'un prestataire
	 * @param coutFixe tableau de coûts fixe
	 * @param coutTransport tableau de coûts de transport
	 * @param duree entier représentant la durée totale de l'activité
	 */
	
	public Prestataire(double[] coutFixe,double[] coutTransport, int duree){
		this.coutFixe=coutFixe;
		this.coutTransport=coutTransport;
		this.distanceUtilise=0;
		this.couts=0;
		this.dureeExp=duree;
		this.id=new String("CExt");
	}
	
	/**
	 * @param j entier jour
	 * @return le coût fixe du prestataire le jour j
	 */
	
	public double getCoutFixe(int j){
		return this.coutFixe[j];
	}
	
	/**
	 * @param j entier jour
	 * @return le coût de transport au kilomètre du prestataire le jour j
	 */
	
	public double getCoutTransport(int j){
		return this.coutTransport[j];
	}
	
	/**
	 * @return le coût cumulé du prestataire 
	 */
	
	public double getCout(){
		return this.couts;
	}
	
	/**
	 * @param cout
	 * Ajoute le coût actuel au coût précédent
	 */
	
	public void addCout(double cout){
		this.couts=this.couts + cout;
	}
	
	
	/**
	 * @param j entier jour
	 * @return le cout total du jour j du prestataire
	 */
	
	public double getCoutFinal(int j){
		if (this.couts!=0){
			this.couts=this.couts+this.coutFixe[j];
		}
		return this.couts;
	}
	
	/**
	 * Méthode permettant d'ajouter une distance à la distance déjà utilisée
	 * @param dist distance
	 */
	
	public void addDistance(double dist ){
		distanceUtilise= distanceUtilise + dist;
	}
	
	/**
	 * Accesseur
	 * @return la distance utilisée par le prestataire
	 */
	
	public double getDistanceUtilise(){
		return this.distanceUtilise;
	}
	
	
	/**
	 * Méthode affectant un client à livrer pour un prestataire
	 * @param client objet de type Client
	 * @param u objet de type Usine
	 * @param quantite double représentant la quantité livrée par le prestataire
	 * @param j entier représentant les jours
	 */
	

	public void prestDelivre(Client client, Usine u,double quantite,int j){
		double distance =u.getCoord().distance(client.getCoord());
		this.addDistance(distance);
		client.setMarchandise(j, quantite);
		client.setMarchandiseJourPrestLivree(quantite, j);
	}
	
	
	/**
	 * Accesseur
	 * @return l'identifiant du prestataire
	 */
	
	public String getId(){
		return this.id;
	}
	
	/**
	 * Setter
	 * Réinitialise les coûts et les distances parcourues par le prestataire chaque début de journée
	 */
	
	public void jourSuivant(){
		this.couts=0;
		this.distanceUtilise=0;
	}
	
}
