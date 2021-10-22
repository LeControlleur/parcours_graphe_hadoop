package com.estia.app;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class ParcoursGraphesMap extends Mapper<Text, GraphNodeWritable, Text, GraphNodeWritable> {

	private static final String GREY = "GRIS";

	protected void map(Text key, GraphNodeWritable node, Context context) throws IOException, InterruptedException {
		
		if (node.getCouleur().toString().equals(GREY)) {
			if (!node.getFils().get(0).toString().equals("")) {
				for (int i = 0; i < node.getFils().size(); i++) {
					GraphNodeWritable new_node = new GraphNodeWritable();
					new_node.setCouleur(new Text(GREY));
					new_node.setProfondeur(new IntWritable(node.getProfondeur().get() + 1));
					context.write(node.getFils().get(i), new_node);
				}
			}
			node.setCouleur(new Text("NOIR") );
			context.write(key, node);
		} else {
			context.write(key, node);
		}
	}
}
