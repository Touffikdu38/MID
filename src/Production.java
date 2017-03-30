import java.util.Iterator;



import oplinterface.*;

public class Production {
	
	private Affectation affect;
	private int nbJours;
	private double cout;
	
	
	public Production(Affectation affect, int nbJours) {
		this.affect= affect;
		this.nbJours=nbJours;
		this.cout=0;
	}

	public double[][] getCapa(){
		double[][] capa= new double[this.affect.getUsines().length][this.nbJours];
		for( int i=0; i<this.affect.getUsines().length; i++){
			for( int j=0; j<this.nbJours; j++){
				capa[i][j]= this.affect.getUsines()[i].getCapaciteProd(j);
			}
		}
		return capa;
	}
	
	
	public double[][] getCapaStock(){
		double[][] capa= new double[this.affect.getUsines().length][this.nbJours];
		for( int i=0; i<this.affect.getUsines().length; i++){
			for( int j=0; j<this.nbJours; j++){
				capa[i][j]= this.affect.getUsines()[i].getCapaciteStock(j);
			}
		}
		return capa;
	}
	
	public double[][] getCoutProd(){
		double[][] prod= new double[this.affect.getUsines().length][this.nbJours];
		for( int i=0; i<this.affect.getUsines().length; i++){
			for( int j=0; j<this.nbJours; j++){
				prod[i][j]= this.affect.getUsines()[i].getcoutProd(j);
			}
		}
		return prod;
	}
	
	public double[][] getCoutStock(){
		double[][] stock= new double[this.affect.getUsines().length][this.nbJours];
		for( int i=0; i<this.affect.getUsines().length; i++){
			for( int j=0; j<this.nbJours; j++){
				stock[i][j]= this.affect.getUsines()[i].getcoutStock(j);
			}
		}
		return stock;
	}
	
	
	public double[][][] getDemandeClientJour(){
		double[][][] demande= new double[this.affect.getUsines().length][this.affect.getClients().length][this.nbJours];
		for ( int k=0; k<this.affect.getUsines().length; k++){

			Iterator<Client>iter= this.affect.getUsines()[k].getClients().iterator();
			int j=0;
			while( iter.hasNext()){
				
				Client c= iter.next();
				
				for( int i=0; i<this.nbJours; i++){
	

						double tailleFen=1;
						if( c.getD2() != c.getD1()){
							tailleFen= c.getD2()-c.getD1();
						}	
						double dem= c.getDemande()/tailleFen;	
						if( j>= c.getD1()-1 && j<= c.getD2()-1){
							demande[k][j][i]= dem;
						} else{
							demande[k][j][i]= 0;
						}
							
			}
				j++;
			
			
			}
		}
	}
				
				

				 
				
				
				
					
				
			
			double demandeJour= this.affect.getClients()[i].getDemande()/tailleFen;
			for( int j=0; j< this.nbJours; j++ ){
				if( j<= this.affect.getClients()[i].getD2()-1 && this.affect.getClients()[i].getD1()-1 <= j){
					demande[i][j]=demandeJour;
				}
				else{
					demande[i][j]=0;
				}
				
			}
		}
		}
		return demande;
		
	}
	
	
	public double[][] getFenetre(){
		double[][] fenetre= new double[this.affect.getClients().length][2];
		for( int i=0; i<this.affect.getClients().length; i++){
				fenetre[i][0]= this.affect.getClients()[i].getD1();
				fenetre[i][1]=this.affect.getClients()[i].getD2();
				
			}
		return fenetre;
	}
	
	public double[][] getPenalite(){
		double[][] penalite= new double[this.affect.getClients().length][this.nbJours];
		for( int i=0; i<this.affect.getClients().length; i++){
			for( int j=0; j< this.nbJours; j++ ){
					penalite[i][j]=this.affect.getClients()[i].getPenalites(j);
				
			}
			}
		
		return penalite;
		
	}
	
		

   
	/*
	 * Pl 2 Recherche du coût minimum de production et stockage
	 */
		
	public double[][] plProduction(){
		double [][] tab=new double[this.affect.getUsines().length][nbJours];
		for (int i=0;i<this.affect.getUsines().length;i++){
			for(int j=0;j<nbJours;j++){
				tab[i][j]=0;
			}
		}
		
               
		for (int i=0; i< this.affect.getUsines().length; i++){
			
            	// lecture du modele prod
                OplSolver solver1 = new OplSolver("Production2.mod");
                
                // traduction des correspondances entre les noms dans le modele et les donnees
                OplData data1 = new OplData(solver1.getOplEnv());
                data1.add("nbJours",this.nbJours);
                data1.add("nbClients",  this.affect.getClients().length);
                data1.add("coutP", getCoutProd()[i]);
                data1.add("coutS", getCoutStock()[i]);
                data1.add("capaP", getCapa()[i]); 
                data1.add("capaS", getCapaStock()[i]);
                data1.add("demande", getDemandeClientJour());
                data1.add("fenetre", getFenetre());
                data1.add("coutPenalite", getPenalite());
                
                
               

                
                // resolution du probleme
                
                int status1 = solver1.solve(data1);
                
                solver1.printData();
                
                // recuperation de la solution optimale
                
                if (status1 == 0) {
                        double coutUsine = solver1.getOptValue();
                        this.cout= this.cout+ coutUsine;
                        double [] solProd = solver1.getDoubleArray("p","Jour");
                        for (int j=0; j<this.nbJours; j++){
//                        	this.usines[i].setProd(solProd[j], j);         	
//                        	this.usines[i].setStock(solStock[j],j);
                        	tab[i][j]=solProd[j];
                        	
                        }
                        
                 }
                
                data1.end();
                solver1.end();  
		}
		return tab;
	
}
	public void plProd(){
		for (int i=0;i<this.affect.getUsines().length;i++){
			for(int j=0;j<nbJours;j++){
				this.affect.getUsines()[i].setProd(plProduction()[i][j],j);
			}
		}
	}
	
	
	
	public double[][] plStockage(){
		double [][] tab=new double[this.affect.getUsines().length][nbJours];
		for (int i=0;i<this.affect.getUsines().length;i++){
			for(int j=0;j<nbJours;j++){
				tab[i][j]=0;
			}
		}
		
               
		for (int i=0; i< this.affect.getUsines().length; i++){
			
            	// lecture du modele prod
                OplSolver solver1 = new OplSolver("Production2.mod");
                
                // traduction des correspondances entre les noms dans le modele et les donnees
                OplData data1 = new OplData(solver1.getOplEnv());
                data1.add("nbJours",this.nbJours);
                data1.add("nbClients",  this.affect.getClients().length);
                data1.add("coutP", getCoutProd()[i]);
                data1.add("coutS", getCoutStock()[i]);
                data1.add("capaP", getCapa()[i]); 
                data1.add("capaS", getCapaStock()[i]);
                data1.add("demande", getDemandeClientJour());
                data1.add("fenetre", getFenetre());
                data1.add("coutPenalite", getPenalite());
                

                
                // resolution du probleme
                
                int status1 = solver1.solve(data1);
                
                // recuperation de la solution optimale
                
                if (status1 == 0) {
                        double coutUsine = solver1.getOptValue();
                        double [] solStock = solver1.getDoubleArray("s","Jour");
                        for (int j=0; j<this.nbJours; j++){
//                        	this.usines[i].setProd(solProd[j], j);         	
//                        	this.usines[i].setStock(solStock[j],j);
                        	tab[i][j]=solStock[j];
                        	
                        }
                        
                 }
                
                data1.end();
                solver1.end();  
		}
		return tab;
	
}
	public void plStock(){
		for (int i=0;i<this.affect.getUsines().length;i++){
			for(int j=0;j<nbJours;j++){
				this.affect.getUsines()[i].setStock(plStockage()[i][j],j);
			}
		}
	}
	
	
	
	
	
	public double[][][] plVente(){
		double [][][] tab=new double[this.affect.getUsines().length][this.affect.getClients().length][nbJours];
       
		for (int i=0; i< this.affect.getUsines().length; i++){
			
            	// lecture du modele prod
                OplSolver solver1 = new OplSolver("Production2.mod");
                
                // traduction des correspondances entre les noms dans le modele et les donnees
                OplData data1 = new OplData(solver1.getOplEnv());
                data1.add("nbJours",this.nbJours);
                data1.add("nbClients",  this.affect.getClients().length);
                data1.add("coutP", getCoutProd()[i]);
                data1.add("coutS", getCoutStock()[i]);
                data1.add("capaP", getCapa()[i]); 
                data1.add("capaS", getCapaStock()[i]);
                data1.add("demande", getDemandeClientJour());
                data1.add("fenetre", getFenetre());
                data1.add("coutPenalite", getPenalite());
                

                
                // resolution du probleme
                
                int status1 = solver1.solve(data1);
                
                // recuperation de la solution optimale
                
                if (status1 == 0) {
                        double coutUsine = solver1.getOptValue();
                        double [][] solVente = solver1.getDoubleArray("Quantite","Client" ,"Jour");
                      
                        	
//                        	this.usines[i].setProd(solProd[j], j);         	
//                        	this.usines[i].setStock(solStock[j],j);
                        	tab[i]=solVente;
                        	
                        }
                        
                 
                
                data1.end();
                solver1.end();  
		}
	

		return tab;
	
}
	
	
	public void setVentes(){
		for(int i=0; i<this.affect.getUsines().length; i++){
			 Iterator<Client> iter= this.affect.getUsines()[i].getClients().iterator();
			 int j=0;
			 while( iter.hasNext()){
				 Client c= iter.next();
				 c.setMarchandiseTot(plVente()[i][j]);
				 j++;
				 }
			 }
		}
	
	
	
	
	
	
	
	
	public double getCoutProdStock(){
		return this.cout;
	}
	
	
    public Usine[] getUsines(){
    	return this.affect.getUsines();
    }
    
    //public Client[] getClients(){
    	//return this.clients;
    //}

}
        


    
