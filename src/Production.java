import iofiles.InputFile;

import iofiles.OutputFile;
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
	
	
	public double[][] getDemande(){
		double[][] demande= new double[this.affect.getUsines().length][this.nbJours];
		for( int i=0; i<this.affect.getUsines().length; i++){
			for( int j=0; j< this.nbJours; j++ ){
				demande[i][j]= this.affect.getUsines()[i].demandeJour(j);
			}
		}
		return demande;
		
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
                OplSolver solver1 = new OplSolver("PL.mod");
                
                // traduction des correspondances entre les noms dans le modele et les donnees
                OplData data1 = new OplData(solver1.getOplEnv());
                data1.add("nbJours",this.nbJours);
                data1.add("coutP", getCoutProd()[i]);
                data1.add("coutS", getCoutStock()[i]);
                data1.add("capaP", getCapa()[i]); 
                data1.add("capaS", getCapaStock()[i]);
                data1.add("demandeSemaine", getDemande()[i]);
                

                
                // resolution du probleme
                
                int status1 = solver1.solve(data1);
                
                // recuperation de la solution optimale
                
                if (status1 == 0) {
                        double coutUsine = solver1.getOptValue();
                        this.cout= this.cout+ coutUsine;
                        double [] solProd = solver1.getDoubleArray("p","Jour");
                        double [] solStock = solver1.getDoubleArray("s","Jour");
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
                OplSolver solver1 = new OplSolver("PL.mod");
                
                // traduction des correspondances entre les noms dans le modele et les donnees
                OplData data1 = new OplData(solver1.getOplEnv());
                data1.add("nbJours",this.nbJours);
                data1.add("coutP", getCoutProd()[i]);
                data1.add("coutS", getCoutStock()[i]);
                data1.add("capaP", getCapa()[i]); 
                data1.add("capaS", getCapaStock()[i]);
                data1.add("demandeSemaine", getDemande()[i]);
                

                
                // resolution du probleme
                
                int status1 = solver1.solve(data1);
                
                // recuperation de la solution optimale
                
                if (status1 == 0) {
                        double coutUsine = solver1.getOptValue();
                        double [] solProd = solver1.getDoubleArray("p","Jour");
                        double [] solStock = solver1.getDoubleArray("s","Jour");
                        for (int j=0; j<this.nbJours; j++){
//                        
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
	
	public double getCoutProdStock(){
		return this.cout;
	}
    //public Usine[] getUsines(){
    	//return this.usines;
    //}
    
    //public Client[] getClients(){
    	//return this.clients;
    //}

}
        


     	

            
