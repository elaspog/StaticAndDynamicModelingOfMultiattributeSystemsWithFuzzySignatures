package net.prokhyon.modularfuzzy.optimalization;

import java.util.List;

public class Individual <T extends ChromosomeElement> {

    final List<T> chromosomeSequence;

    public Individual(final List<T> chromosomeSequence){

        this.chromosomeSequence = chromosomeSequence;
    }

    public List<T> getChromosomeSequence() {
        return chromosomeSequence;
    }

}
