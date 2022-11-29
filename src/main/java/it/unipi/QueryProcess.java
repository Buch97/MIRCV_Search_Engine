package it.unipi;


import java.io.*;
import java.util.*;

import static it.unipi.App.num_docs;

public class QueryProcess {
    //ricevo query da riga di comando
    //ricavo i termini della query inserita dall'utente
    //calcolo query length e query term frequency
    //accedo alle postings relative ai termini della query
    //implemento daat
    //ritorno a video il docno dei 10/20 documenti piu rilevanti
    //cedo nuovamente il comando all'utente per inserire la prossima query
    public static void parseQuery(String query, int k) throws IOException {
        Tokenizer tokenizer = new Tokenizer(query);
        Map<String, Integer> query_term_frequency = tokenizer.tokenize();
        Integer query_length = 0;
        for (String token : query_term_frequency.keySet()) {
            query_length += query_term_frequency.get(token);
            System.out.println(token + " " + query_term_frequency.get(token));
        }
        System.out.println("Query length = " + query_length);
        daatScoring(query_term_frequency, k);
    }

    private static void daatScoring(Map<String, Integer> query, int k) throws IOException {
        //lexicon: ci leggo offset dell'inverted index relativo alla posting di un termine
        //inverted_index: ci leggo la posting list
        //document_index: ci leggo la doc_length del documento che sto processando
        ArrayList<String> L = new ArrayList<>();
        PriorityQueue<Results> R = new PriorityQueue<>(k);
        int[] pos = new int[query.size()];
        Arrays.fill(pos,0);

        RandomAccessFile file_reader = new RandomAccessFile(new File("./src/main/resources/output/inverted_index.tsv"), "r");
        for(String term : query.keySet()){
            //devo leggere dal dizionario lasciandolo su disco, non va mai messo in memoria!!!
            //long posting_list_location = memory_lexicon.get(term);
            //accedo direttamente all'offset del file
            //file_reader.seek(posting_list_location);
            L.add(file_reader.readLine());
        }
        //devo trovarmi min e max doc_id
        int docid = minDocId(pos, num_docs);
        int last_docid = maxDocId(pos, num_docs);
        //!!!!!!!!!
        //capire come funziona memory-mapped file e anche mapDB con HTreeMap data structure
        //i file non vanno salvati come testo o csv ma con altre strutture dati disk based che sono piu veloci rispetto alla ricerca da file txt

    }

    private static int maxDocId(int[] pos, int num_docs) {
        return 0;
    }

    private static int minDocId(int[] pos, int num_docs) {
        return 0;
    }
}