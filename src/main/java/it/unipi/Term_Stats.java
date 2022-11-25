package it.unipi;

public class Term_Stats {
    int doc_frequency;
    int coll_frequency;
    long actual_offset;

    public Term_Stats(int doc_frequency, int coll_frequency, long actual_offset) {
        this.doc_frequency = doc_frequency;
        this.coll_frequency = coll_frequency;
        this.actual_offset = actual_offset;
    }

    public int getDoc_frequency() {
        return doc_frequency;
    }

    public void setDoc_frequency(int doc_frequency) {
        this.doc_frequency = doc_frequency;
    }

    public int getColl_frequency() {
        return coll_frequency;
    }

    public void setColl_frequency(int coll_frequency) {
        this.coll_frequency = coll_frequency;
    }

    public long getActual_offset() {
        return actual_offset;
    }

    public void setActual_offset(long actual_offset) {
        this.actual_offset = actual_offset;
    }
}

