using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Genetic;

namespace Chapter06GeneticTSP
{
    public class TSPChromosome: Chromosome<int>
    {
        	protected City []cities;

	public TSPChromosome( TSPGeneticAlgorithm owner,  City []cities) {
		this.GA = owner;
		this.cities = cities;

		 int []genes = new int[this.cities.Length];
		 bool []taken = new bool[cities.Length];

         Random rand = new Random();

		for (int i = 0; i < genes.Length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < genes.Length - 1; i++) {
			int icandidate;
			do {
				icandidate = (int) (rand.NextDouble() * genes.Length);
			} while (taken[icandidate]);
			genes[i] = icandidate;
			taken[icandidate] = true;
			if (i == genes.Length - 2) {
				icandidate = 0;
				while (taken[icandidate]) {
					icandidate++;
				}
				genes[i + 1] = icandidate;
			}
		}
		this.Genes = genes;
		CalculateCost();

	}

	override public void CalculateCost()  {
		double cost = 0.0;
		for (int i = 0; i < this.cities.Length - 1; i++) {
			 double dist = this.cities[GetGene(i)]
					.proximity(this.cities[GetGene(i + 1)]);
			cost += dist;
		}
		this.Cost = cost;

	}

	override public void Mutate() {
        Random rand = new Random();
		 int length = this.Genes.Length;
		 int iswap1 = (int) (rand.NextDouble() * length);
         int iswap2 = (int)(rand.NextDouble() * length);
		 int temp = GetGene(iswap1);
		SetGene(iswap1, GetGene(iswap2));
		SetGene(iswap2, temp);
	}

    }
}
