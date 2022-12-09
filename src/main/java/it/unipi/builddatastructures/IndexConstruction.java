package it.unipi.builddatastructures;

import it.unipi.bean.Posting;
import it.unipi.bean.Token;
import org.mapdb.DB;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IndexConstruction {

    //dimensione per costruire i blocchi messa ora per prova a 3000 su una small collection
    public final static int SPIMI_TOKEN_STREAM_MAX_LIMIT = 3000;
    public final static List<Token> tokenStream = new ArrayList<>();
    public static int BLOCK_NUMBER = 0; //indice da usare per scrivere i file parziali dell'inverted index
    public static void buildDataStructures(DB db) {
        try {
            File myObj = new File("./src/main/resources/collections/small_collection.tsv");

            Scanner myReader = new Scanner(myObj, StandardCharsets.UTF_8);
            BufferedWriter writer_doc_index = new BufferedWriter(new FileWriter("./src/main/resources/output/document_index.tsv"));
            writer_doc_index.write("DOC_ID" + "\t" + "DOC_NO" + "\t" + "DOC_LEN" + "\n");

            System.out.println("----------------------START GENERATING INVERTED INDEX BLOCKS----------------------");
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                // Handling of malformed lines
                if (!data.contains("\t"))
                    continue;

                String[] row = data.split("\t");
                String doc_no = row[0];
                String text = row[1];

                // Add document to the document index
                documentIndexAddition(doc_no, text, writer_doc_index);

                // Parsing/tokenization of the document
                parseDocumentBody(Integer.parseInt(doc_no), text);
            }

            writer_doc_index.close();
            myReader.close();
            System.out.println("----------------------INVERTED INDEX BLOCKS READY----------------------");
            MergeBlocks.mergeBlocks(db, BLOCK_NUMBER);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void parseDocumentBody(int doc_id, String text) {
        Tokenizer tokenizer = new Tokenizer(text);
        Map<String, Integer> results = tokenizer.tokenize();

        for (String token : results.keySet())
            tokenStream.add(new Token(token, doc_id, results.get(token)));

        // Add token to tokenStream until we reach a size threshold
        if (tokenStream.size() >= SPIMI_TOKEN_STREAM_MAX_LIMIT) {
            // Create the inverted index of the block
            invertedIndexSPIMI();
            // clear the stream of token
            tokenStream.clear();
            BLOCK_NUMBER++;
        }
    }
    private static void invertedIndexSPIMI() {

        // Pseudocode at slide 59
        File output_file = new File("./src/main/resources/intermediate_postings/inverted_index" + BLOCK_NUMBER + ".tsv");

        // one dictionary for each block
        HashMap<String, ArrayList<Posting>> dictionary = new HashMap<>();
        ArrayList<Posting> postings_list;

        //while (Runtime.getRuntime().freeMemory() > 0) {
        for (Token token : IndexConstruction.tokenStream) {
            if (!dictionary.containsKey(token.getTerm()))
                postings_list = addToDictionary(dictionary, token.getTerm());
            else
                postings_list = dictionary.get(token.getTerm());

            if (!postings_list.contains(null)) {
                int capacity = postings_list.size() * 2;
                postings_list.ensureCapacity(capacity); //aumenta la length dell arraylist
            }

            postings_list.add(new Posting(token.getDoc_id(), token.getFrequency()));
        }
        //}

        //faccio il sort del vocabolario per facilitare la successiva fase di merging
        TreeMap<String, ArrayList<Posting>> sorted_dictionary = new TreeMap<>(dictionary);

        try {
            //scrivo sul file
            FileWriter myWriter = new FileWriter(output_file);
            myWriter.write("TERM" + "\t" + "POSTING_LIST" + "\n");

            for (String term : sorted_dictionary.keySet()) {
                myWriter.write(term + "\t");

                for (Posting p : sorted_dictionary.get(term))
                    myWriter.write(p.getDoc_id() + ":" + p.getTerm_frequency() + " ");

                myWriter.write("\n");
            }
            System.out.println("WRITTEN BLOCK " + BLOCK_NUMBER);
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
    private static ArrayList<Posting> addToDictionary(Map<String, ArrayList<Posting>> vocabulary, String token) {
        int capacity = 1;
        ArrayList<Posting> postings_list = new ArrayList<>(capacity);
        vocabulary.put(token, postings_list);
        return postings_list;
    }
    private static void documentIndexAddition(String doc_no, String text, BufferedWriter writer) throws IOException {
        int doc_len = text.getBytes().length;
        writer.write(Integer.parseInt(doc_no) + "\t" + doc_no + "\t" + doc_len + "\n");
    }

}