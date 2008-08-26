using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Genetic;

namespace Chapter06GeneticTSP
{
    public class TSPGeneticAlgorithm: GeneticAlgorithm<int>
    {
	public TSPGeneticAlgorithm( City []cities,  int populationSize,
			 double mutationPercent,  double percentToMate,
			 double matingPopulationPercent,  int cutLength)
		{
		this.MutationPercent = mutationPercent;
		this.MatingPopulation = matingPopulationPercent;
		this.PopulationSize = populationSize;
		this.PercentToMate = percentToMate;
		this.CutLength = cutLength;
		this.PreventRepeat = true;

		this.Chromosomes = new TSPChromosome[this.PopulationSize];
		for (int i = 0; i < this.Chromosomes.Length; i++) {

			 TSPChromosome c = new TSPChromosome(this, cities);
			SetChromosome(i, c);
		}
		SortChromosomes();
	}
    }
}
