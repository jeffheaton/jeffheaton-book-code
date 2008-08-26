using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Threading;

namespace HeatonResearchNeural.Genetic
{
    class MateWorker<GENE_TYPE>
    {
        private Chromosome<GENE_TYPE> mother;
        private Chromosome<GENE_TYPE> father;
        private Chromosome<GENE_TYPE> child1;
        private Chromosome<GENE_TYPE> child2;
        private ManualResetEvent eventHandler;

        /// <summary>
        /// Construct a MateWorker class to handle one mating.
        /// </summary>
        /// <param name="mother">The mother to mate.</param>
        /// <param name="father">The father to mate.</param>
        /// <param name="child1">The first offspring.</param>
        /// <param name="child2">The second offspring.</param>
        public MateWorker(Chromosome<GENE_TYPE> mother, Chromosome<GENE_TYPE> father,
             Chromosome<GENE_TYPE> child1, Chromosome<GENE_TYPE> child2)
        {
            this.mother = mother;
            this.father = father;
            this.child1 = child1;
            this.child2 = child2;
        }

        /// <summary>
        /// Set the event that will be used to signal when the mating is complete.
        /// </summary>
        /// <param name="eventHandler">The event object to use.</param>
        public void SetEvent(ManualResetEvent eventHandler)
        {
            this.eventHandler = eventHandler;
        }

        /// <summary>
        /// Perform the mating.
        /// </summary>
        public void Call()
        {
            this.mother.Mate(this.father,
                    this.child1,
                    this.child2);
            if (this.eventHandler != null)
            {
                this.eventHandler.Set();
            }
        }
    }
}
