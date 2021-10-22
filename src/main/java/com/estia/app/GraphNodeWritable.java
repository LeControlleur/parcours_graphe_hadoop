package com.estia.app;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import java.util.ArrayList;
import java.util.List;

public class GraphNodeWritable implements Writable {

    private Text couleur;
    private IntWritable profondeur;
    private List<Text> fils;

    public GraphNodeWritable() {
        this.couleur = new Text("");
        this.profondeur = new IntWritable(0);
        this.fils = new ArrayList<Text>();
    }

    public Text getCouleur() {
        return this.couleur;
    }

    public IntWritable getProfondeur() {
        return this.profondeur;
    }

    public List<Text> getFils() {
        return this.fils;
    }

    public void setCouleur(Text new_couleur) {
        this.couleur = new_couleur;
    }

    public void setProfondeur(IntWritable new_profondeur) {
        this.profondeur = new_profondeur;
    }

    public void setFils(List<Text> new_fils) {
        this.fils = new_fils;
    }

    public String get_serialized() {
        String str = "";

        int taille_fils = fils.size();
        if (taille_fils >= 1) {
            str = fils.get(0).toString();
            if (taille_fils > 1) {
                for (int i = 1; i < taille_fils; i++) {
                    str = String.join(",", str, fils.get(i).toString());
                }
            }
        }

        str += "|" + this.couleur.toString() + "|" + this.profondeur.toString();
        return str;
    }

    public void write(DataOutput dataOutput) throws IOException {
        couleur.write(dataOutput); // write couleur
        profondeur.write(dataOutput); // write profondeur
        dataOutput.writeInt(fils.size()); // write size of list
        for (int i = 0; i < fils.size(); i++) {
            fils.get(i).write(dataOutput); // write all the value of list of children
        }
    }

    public void readFields(DataInput dataInput) throws IOException {
        couleur.readFields(dataInput); // read familyId
        profondeur.readFields(dataInput); // read totalAge
        int size = dataInput.readInt(); // read size of list
        fils = new ArrayList<Text>(size);
        for (int i = 0; i < size; i++) { // read all the values of list
            Text text = new Text();
            text.readFields(dataInput);
            fils.add(text);
        }
    }

}
