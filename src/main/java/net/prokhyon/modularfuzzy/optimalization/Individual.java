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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Individual<T> that = (Individual<T>) o;

        return chromosomeSequence != null ? chromosomeSequence.equals(that.chromosomeSequence) : that.chromosomeSequence == null;
    }

    @Override
    public int hashCode() {
        return chromosomeSequence != null ? chromosomeSequence.hashCode() : 0;
    }

}
