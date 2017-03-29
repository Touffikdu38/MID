import java.util.Iterator;
import java.util.LinkedList;

public class Usine {

	private String id;
	private int dureeExp;
	private double[] capaciteProd;
	private double[] capaciteStock;
	private double[] coutProd;
	private double[] coutStock;
	private double[] stock;
	private double[] production;
	private Point coord;
	private LinkedList<Client> clients;
	private LinkedList<Camion> flotteCamion;
	private double[] quantiteALivrer;
	
	
	
	public Usine(String id,double[] capaProd, double[] capaStock, double[] coutStock, double[] coutProd, Point coord, int duree){
		this.id=id;
		this.dureeExp=duree;
		this.coord=coord;
		this.capaciteProd=new double[dureeExp];
		this.capaciteStock=new double[dureeExp];
		this.coutStock=new double[dureeExp];
		this.coutProd=new double[dureeExp];
		this.stock= new double[dureeExp];
		this.production=new double[dureeExp];
		for (int i=0; i<7; i++){
			this.capaciteProd[i]=capaProd[i];
			this.capaciteStock[i]=capaStock[i];
			this.coutStock[i]= coutStock[i];
			this.coutProd[i]= coutProd[i];
			this.production[i]=0;
			this.stock[i]=0;
		}
		
		this.flotteCamion=null ;
		this.clients=null;
		this.quantiteALivrer= new double[dureeExp];
		for( int i=0; i<dureeExp-1; i++){
			this.quantiteALivrer[i]= this.production[i]+this.stock[i]-this.stock[i+1];
		}
		this.quantiteALivrer[dureeExp-1]= this.production[dureeExp-1]+this.stock[dureeExp-1];
	}
		

	public LinkedList<Client> getClients(){
		return this.clients;
	}
	
	public LinkedList<Camion> getCamions(){
		return this.flotteCamion;
	}
	
	public double getQuantiteALivrer( int j){
		return this.quantiteALivrer[j];
	}

	
		
	public Point getCoord(){
		return this.coord;
	}
	
	public void setProd(int prod, int jour){
		if( this.capaciteProd[jour]>= prod)
		this.production[jour]= prod;
		
	}
	
	public void setStock(int stock, int jour){
		if( this.capaciteStock[jour]>= stock)
		this.production[jour]= stock;
		
	}
	
	public double getCapaciteProd(int j){
		return this.capaciteProd[j];
	}
	
	 public double getCapaciteProdTotale(){
		 double capa=0;
		 for(int i=0; i<7; i++){
			 capa=capa+ getCapaciteProd(i);
		 }
		 return capa;
	 }
	
	public double getCapaciteStock(int j){
		return this.capaciteStock[j];
	}
		
	public double getCapaciteStockTotale(){
		double capa=0;
		for( int i =0; i<7; i++){
			capa= capa+ getCapaciteStock(i);
		}
		return capa;
	}
		
	
	public void addClient( Client c){
		this.clients.add(c);
	}
	
	public void addCamion( Camion c){
		this.flotteCamion.add(c);
	}
	
	public double getStock (int j){
		return this.stock[j];
	}
	
	public double getProd(int j){
		return this.production[j];
	
}
	
	public double getcoutStock(int j){
		return this.coutStock[j];
		
	}
	
	
	public double getcoutProd(int j){
		return this.coutProd[j];
		
	}
	
	
	public double demandeJour( int j){
		double qte=0;
		LinkedList<Client> cList= this.clients;
		Iterator<Client> iter= cList.iterator();
		while(iter.hasNext()){
			Client c = iter.next();
			if( c.jourALivrer()==j){
				qte= qte + c.getDemande();
			}
		}
		return qte;
	}
	
	
}