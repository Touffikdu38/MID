import iofiles.InputFile;
import iofiles.OutputFile;


public class Donnees{ 
	
	private String nomfichier;
	private int nbjours;
	private int nbusines;
	private int nbclients;
	private int nbcamions;
	private double[][][] usines;
	private double[][] coordUsines;
	private double[][] clients;
	private double[][] camions;
	private double[][] prestataire; 
	private String[] usinesCamions;
	private String[] idusines;
	private String[] idClients;
	private String[] idcamions;
	
	
	/**
	 * Construction de Donnees
	 * @param name nom du fichier d'instances
	 */
	
	
	public Donnees (String name){
       	this.nomfichier = name + ".txt";
    	InputFile f = new InputFile();
		
    	f.open(nomfichier);	
	
    	/**
    	 * Lecture de la première ligne
    	 */
    	
		int nbjours= f.readInt();
    	int nbusines=f.readInt();
    	int nbclients= f.readInt();
    	int nbcamions= f.readInt();
    	
    	/**
    	 * Affectation de la première ligne
    	 */
    	
    	this.nbjours=nbjours;
    	this.nbusines=nbusines;
    	this.nbclients=nbclients;
    	this.nbcamions=nbcamions;
    	
    	/**
    	 * Création de tableau d'usines, de leur coordonnées, de clients etc
    	 */
    	
    	this.usines= new double [nbusines][4][nbjours];
    	this.coordUsines= new double [nbusines][2];
    	this.clients= new double[nbclients][6];
    	this.camions= new double [nbcamions][10];
    	this.prestataire= new double[2][nbjours];  		
    	this.usinesCamions= new String[nbcamions];
    	this.idusines= new String[nbusines];
    	this.idClients= new String[nbclients];
    	this.idcamions= new String[nbcamions];
    	
    	/**
    	 * Remplissage des tableaux d'usines etc avec les données lues
    	 */

    	
    	for(int i=0; i<nbusines; i++ ){
	    	String idUsine= f.readString();
	    	this.idusines[i]=idUsine;
	    	this.coordUsines[i][0]= f.readDouble();
	    	this.coordUsines[i][1]= f.readDouble();
	    	for( int j =0; j<4;j++){
	    		for(int k=0; k<nbjours; k++){
	    			this.usines[i][j][k]= f.readDouble();	
	    		}
	    	}
    	}
    
    	
    	for(int i=0; i<nbclients; i++){
    		String idClient= f.readString();
    		this.idClients[i]=idClient;
    		for (int j=0; j<6;j++){
    			this.clients[i][j]=f.readDouble();
    		}
    	}
    	
    	
    	for( int i=0; i<nbcamions; i++){
    		String idCamion=f.readString();
    		this.idcamions[i]=idCamion;
    		String idCamionUsine=f.readString();
    		this.usinesCamions[i]=idCamionUsine;
    		for(int j=0; j<10; j++){
    			this.camions[i][j]= f.readDouble();
    			}
    		}
    	
    	
    	for (int i=0; i<2;i++){
    		for(int j=0; j<nbjours;j++){
    			this.prestataire[i][j]=f.readDouble();
    		}
    	}
    	f.close();
	}
	
	/**
	 * @return un tableau d'usines dont les attributs sont ceux des données lues
	 */
	
	public Usine[] getUsines(){
		Usine[ ] tabUsines= new Usine[nbusines];	
		for( int i=0; i<nbusines; i++){
			double[] capaciteProdU= new double[nbjours];
			double[] capaciteStockU= new double[nbjours];
			double[] coutProdU= new double[nbjours];
			double[] coutStockU= new double[nbjours];
			for ( int j=0; j<nbjours; j++){
				capaciteProdU[j]= this.usines[i][0][j];
				capaciteStockU[j]= this.usines[i][1][j];
				coutProdU[j]= this.usines[i][2][j];
    			coutStockU[j]= this.usines[i][3][j];
			}
		String id = this.idusines[i];
		Point coordU= new Point( coordUsines[i][0], coordUsines[i][1]);
		Usine u= new Usine(id, capaciteProdU, capaciteStockU,coutStockU, coutProdU, coordU, this.nbjours);
		tabUsines[i]=u;
		}
		return tabUsines;
	}
	
	
	/**
	 * @return un tableau de clients dont les attributs sont ceux des données lues
	 */
	
	public Client[] getClients(){
		Client[] tabClients= new Client[nbclients];
		for( int i=0; i<nbclients; i++){
			Point coord= new Point(clients[i][0], clients[i][1]);
			Client c = new Client( idClients[i], coord, clients[i][2],clients[i][3], this.clients[i][4], this.clients[i][5], this.nbjours );
			tabClients[i]= c;
		}
		return tabClients;
	}
	
	
	/**
	 * @return un tableau de camions dont les attributs sont ceux des données lues
	 */
	
	public Camion[] getCamions(){
		Camion[] tabCamions= new Camion[this.nbcamions];
		for( int i=0; i<this.nbcamions; i++){
			String idUsine= this.usinesCamions[i];
			double[] heuresJour= new double[this.nbjours];
			for( int j=0; j<this.nbjours; j++){
				heuresJour[j]= this.camions[i][3+j];
			}
			String nbUsine= Character.toString(idUsine.charAt(1));
			int nb = Integer.parseInt(nbUsine);
			Camion c= new Camion(this.idcamions[i], idUsine,  this.camions[i][0], this.camions[i][1], this.camions[i][2], heuresJour, getUsines()[nb-1], this.nbjours );
			tabCamions[i]=c;
		}
		return tabCamions;
	}
	
	
	/**
	 * @return un prestataire dont les attributs sont ceux des données lues
	 */
	
	public Prestataire getPrestataire(){
		double[] coutFixe = new double[this.nbjours];
		double[] coutVar = new double[this.nbjours];
		for( int i=0; i< this.nbjours; i++){
			coutFixe[i]=prestataire[0][i];
			coutVar[i]= prestataire[1][i];
		}
		Prestataire p = new Prestataire(coutFixe, coutVar, this.nbjours);
		return p;
	}
	
	
	/** 
	 * @return la durée total de l'activité en jour
	 */
	
	public int getDuree(){
		return this.nbjours;
	}
	
}
