package it.unipi;

import java.util.List;

//• Document index:
//  • Contains information on the single documents, such as the URL, title, document length, pagerank, etc…
//• Contains one element for each indexed document
//• Keeps the mapping between documents and docids
//• Given docid, we need to be able to look up URL
//• Maybe store document size and pagerank
//• Simplest approach: records ordered by docid
//• More space-efficient:
//  • Store URLs in alphabetic order, maybe compressed
//  • Replace URL in above table by pointer or offset
//  • Also allows lookup of docID by URL

public class Document_Index {
    List<Doc_Stats> doc_index;

    public Document_Index(List<Doc_Stats> doc_index) {
        this.doc_index = doc_index;
    }

    public List<Doc_Stats> getDoc_index() {
        return doc_index;
    }

    public void setDoc_index(List<Doc_Stats> doc_index) {
        this.doc_index = doc_index;
    }

    public void print(){
        for (Doc_Stats docIndex : this.doc_index) {
            System.out.println("DOC_NO: " + docIndex.getDoc_no() + "    DOC_LEN: " + docIndex.getLength());
        }
    }
}
