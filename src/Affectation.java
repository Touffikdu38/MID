import oplinterface.OplData;
import oplinterface.OplSolver;

public class Affectation {
	
	private Usine[] usines;
	private Client[] clients;
	private Camion[] camions;

	/**
	 * Constructeur d'une affectation de clients et de camions à une usine
	 * @param u tableau d'Usine
	 * @param c tableau de Client
	 * @param ca tableau de Camion
	 */
	
	public Affectation( Usine[] u,Client[] c , Camion[]ca){
		this.usines=u;
		this.clients=c;
		this.camions=ca; 
	}
	
	/**
	 * @return un tableau correspondant aux demandes de chaque client
	 */
	
	public double[] getDemande(){
		double[] demande= new double[this.clients.length];
		for( int i =0; i<this.clients.length; i++){
			demande[i]=this.clients[i].getDemande();
		}
		return demande;
	}
	
	/**
	 * @return une matrice affichant la distance de chaque client à chaque usine
	 */
	
	public double[][] getDistance(){
		double[][] distance= new double[this.usines.length][this.clients.length];
		for( int i=0; i<this.usines.length; i++){
			for( int j=0; j<this.clients.length; j++){
				distance[i][j]= this.usines[i].getCoord().distance(this.clients[j].getCoord());
				
			}
		}
		return distance;
	}
	
	/**
	 * @return la capacité de production totale de chaque usine
	 */
	
	public double[] getCapacite(){
		double[] capacite= new double[this.usines.length];
		for( int i=0; i<this.usines.length; i++){
			capacite[i]= this.usines[i].getCapaciteProdTotale();
		}
		return capacite;
	}
	
	/**
	 * Méthode associant les camions à leurs usines de base
	 */
	
	public void associeCamions(){
		int nbCamions=this.camions.length;
		int nbUsines = this.usines.length;		
		for (int i=0;i<nbUsines;i++){
			for(int j=0;j<nbCamions;j++){
				if (camions[j].usineBase()==usines[i]){
					this.usines[i].addCamion(camions[j]);
				}
			}
		}
	}
	
	
	
		public void plClient(){
	
                // lecture du modele client
			
                OplSolver solver2 = new OplSolver ("PLclient.mod");
                
                // traduction des correspondances entre les noms dans le modele et les donnees
                
                OplData data2 = new OplData(solver2.getOplEnv());
               
                data2.add("nbClients", this.clients.length);
                data2.add("nbUsines", this.usines.length);
                data2.add("demande",getDemande());
                data2.add("capaP", getCapacite());
                data2.add("distance", getDistance());
              
                // resolution du probleme
                int status = solver2.solve(data2);
        
                // recuperation de la solution optimale
                if (status == 0) {
                	int [][] solAffectation = solver2.getIntArray("y", "clients", "usines");
                	for( int j=0; j<this.clients.length; j++){
                        for (int i=0; i< this.usines.length; i++){
                        	if (solAffectation[j][i]==1){
                            	this.usines[i].addClient(this.clients[j]);
                            	this.clients[j].setUsine(this.usines[i]);
                        	}	
                        }
                	}	
                }
                data2.end();
                solver2.end();                
		}
              
		/**
		 * Accesseur
		 * @return une usine du tableau d'Usine
		 */
                
        public Usine[] getUsines(){
        	return this.usines;
        }
        
        /**
         * Accesseur
         * @return un client du tableau de Client
         */
        
        public Client[] getClients(){
        	return this.clients;
        }
        
        /**
         * Accesseur 
         * @return un camion du tableau de Camion
         */
        
        public Camion[] getCamions(){
        	return this.camions;
        }
			
}
