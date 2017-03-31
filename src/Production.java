import java.util.Iterator;

import oplinterface.*;

public class Production {

	private Usine[] affect;
	private int nbJours;
	private double cout;
	private double[] test;

	/**
	 * Constructeur d'une production
	 * @param u tableau d'usines
	 * @param nbJours nombre de jours total d'activité
	 */
	
	public Production(Usine[] u, int nbJours) {
		this.affect = u;
		this.nbJours = nbJours;
		this.cout = 0;
	}
	
	/**
	 * @return la capacité journalière de chaque usine
	 */

	public double[][] getCapa() {
		double[][] capa = new double[this.affect.length][this.nbJours];
		for (int i = 0; i < this.affect.length; i++) {
			for (int j = 0; j < this.nbJours; j++) {
				capa[i][j] = this.affect[i].getCapaciteProd(j);
			}
		}
		return capa;
	}
	
	/**
	 * @return le stock journalier de chaque usine
	 */

	public double[][] getCapaStock() {
		double[][] capa = new double[this.affect.length][this.nbJours];
		for (int i = 0; i < this.affect.length; i++) {
			for (int j = 0; j < this.nbJours; j++) {
				capa[i][j] = this.affect[i].getCapaciteStock(j);
			}
		}
		return capa;
	}
	
	/**
	 * @return le coût de production journalier de chaque usine
	 */

	public double[][] getCoutProd() {
		double[][] prod = new double[this.affect.length][this.nbJours];
		for (int i = 0; i < this.affect.length; i++) {
			for (int j = 0; j < this.nbJours; j++) {
				prod[i][j] = this.affect[i].getcoutProd(j);
			}
		}
		return prod;
	}

	/**
	 * @return le coût de stockage journalier de chaque usine
	 */
	
	public double[][] getCoutStock() {
		double[][] stock = new double[this.affect.length][this.nbJours];
		for (int i = 0; i < this.affect.length; i++) {
			for (int j = 0; j < this.nbJours; j++) {
				stock[i][j] = this.affect[i].getcoutStock(j);
			}
		}
		return stock;
	}

	
	/**
	 * @param u objet de type Usine
	 * @return la demande journalière de chaque client affecté à une usine
	 * Pour cela on a choisi de diviser sa demande sur toute sa fenêtre 
	 */
	
	public double[][] getDemandeClientJour(Usine u) {
		this.test = new double[this.nbJours];
		double[][] demande = new double[u.getClients().size()][this.nbJours];

		Iterator<Client> iter = u.getClients().iterator();
		int j = 0;
		while (iter.hasNext()) {
			Client c = iter.next();
			double tailleFen = 1;
			if (c.getD2() != c.getD1()) {
				tailleFen = c.getD2() - c.getD1() + 1;
			}
			double dem = c.getDemande() / tailleFen;
			for (int i = 0; i < this.nbJours; i++) {
				if (i >= c.getD1() - 1 && i <= c.getD2() - 1) {
					demande[j][i] = dem;
				} else {
					demande[j][i] = 0;
				}
			}
			j++;
		}
		return demande;
	}

	
	/**
	 * @param u objet de type Usine
	 * @return
	 */
	
	public double[][] getFenetre(Usine u) {

		double[][] fenetre = new double[u.getClients().size()][2];

		Iterator<Client> iter = u.getClients().iterator();
		int j = 0;
		while (iter.hasNext()) {
			Client c = iter.next();
			fenetre[j][0] = c.getD1();
			fenetre[j][1] = c.getD2();
			j++;

		}
		return fenetre;
	}

	public double[][] getPenalite(Usine u) {
		double[][] penalite = new double[u.getClients().size()][this.nbJours];

		Iterator<Client> iter = u.getClients().iterator();
		int j = 0;
		while (iter.hasNext()) {
			Client c = iter.next();
			for (int k = 0; k < this.nbJours; k++) {
				penalite[j][k] = c.getPenalites(k);

			}
			j++;

		}
		return penalite;

	}

	/*
	 * Pl 2 Recherche du co�t minimum de production et stockage
	 */

	public void plProduction() {

		for (int i = 0; i < this.affect.length; i++) {

			// lecture du modele prod
			OplSolver solver1 = new OplSolver("Production2.mod");

			// traduction des correspondances entre les noms dans le modele et
			// les donnees
			OplData data1 = new OplData(solver1.getOplEnv());
			data1.add("nbJours", this.nbJours);
			data1.add("nbClients", this.affect[i].getClients().size());
			data1.add("coutP", getCoutProd()[i]);
			data1.add("coutS", getCoutStock()[i]);
			data1.add("capaP", getCapa()[i]);
			data1.add("capaS", getCapaStock()[i]);
			data1.add("demande", getDemandeClientJour(this.affect[i]));
			data1.add("fenetre", getFenetre(this.affect[i]));
			data1.add("coutPenalite", getPenalite(this.affect[i]));

			// resolution du probleme

			int status1 = solver1.solve(data1);

	

			// recuperation de la solution optimale

			if (status1 == 0) {
				double coutUsine = solver1.getOptValue();
				this.cout = this.cout + coutUsine;
				double[] solProd = solver1.getDoubleArray("p", "Jour");
				double[] solStock = solver1.getDoubleArray("s", "Jour");
				double[][] solVente = solver1.getDoubleArray("Quantite", "Client", "Jour");
				
				for (int j = 0; j < this.nbJours; j++) {
					this.affect[i].setProd(solProd[j], j);
					this.affect[i].setStock(solStock[j], j);
				}

				Iterator<Client> iter = this.affect[i].getClients().iterator();
				int j = 0;
				while (iter.hasNext()) {
					Client c = iter.next();
					c.setMarchandiseTot(solVente[j]);
					j++;
				}	
			}
			data1.end();
			solver1.end();
		}
	}

	public double[] getTest() {
		return this.test;
	}

	/**
	 * @return le coût total de la production et du stockage sur toute la durée de l'activité
	 */
	
	public double getCoutProdStock() {
		return this.cout;
	}
	
	/**
	 * @return le tableau des usines 
	 */

	public Usine[] getUsines() {
		return this.affect;
	}

}
