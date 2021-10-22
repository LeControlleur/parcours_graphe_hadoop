package com.estia.app;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;

public class ParcoursGraphesReduce extends Reducer<Text, GraphNodeWritable, Text, GraphNodeWritable> {

    public void reduce(Text key, Iterable<GraphNodeWritable> values, Context context) throws IOException, InterruptedException {
        Iterator<GraphNodeWritable> i = values.iterator();

        GraphNodeWritable new_node = new GraphNodeWritable();
        new_node.setProfondeur(new IntWritable(-1));
        new_node.setFils(new ArrayList<Text>());
        new_node.setCouleur(new Text());

        while (i.hasNext()) {
            GraphNodeWritable node = i.next();
            int depth;
            String colour;

            try {
                depth = node.getProfondeur().get();
            } catch (Exception e) {
                depth = -2;
            }
            if (depth == -2)
                continue;
            
            if (depth > new_node.getProfondeur().get())
                new_node.setProfondeur(new IntWritable(depth));

            if (node.getFils().size() > new_node.getFils().size())
                new_node.setFils(node.getFils());

            colour = node.getCouleur().toString();
            if ((new_node.getCouleur().toString().equals(""))
                    || ((new_node.getCouleur().toString().equals("BLANC") && (colour.equals("GRIS") || colour.equals("BLACK")))
                            || (new_node.getCouleur().toString().equals("GRIS") && (node.getCouleur().toString().equals("BLACK"))))) {
                new_node.setCouleur(node.getCouleur());
            }
        }

        if (!new_node.getCouleur().toString().equals("NOIR"))
            context.getCounter(ParcoursGraphes.GRAPH_COUNTERS.NB_NODES_UNFINISHED).increment(1);

        context.write(key, new_node);
    }

}