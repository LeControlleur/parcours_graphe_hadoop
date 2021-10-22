/*
  M2 MBDS - Big Data/Hadoop
	Année 2019/2020
  --
  TP2: parcours de graphe & programmation Hadoop avancée.
  --
  GraphInputFormat.java: InputFormat hadoop custo (utilise GraphRecordReader pour lire les GraphNodeWritable à partir du format textuel).
*/
package com.estia.app;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import java.io.IOException;

public class ParcoursGraphesInputFormat extends FileInputFormat<Text, GraphNodeWritable> {
  // Le seul rôle de cette classe est de fournir un RecordReader propre au type GraphNodeWritable: une instance de notre classe GraphRecordReader.
	public RecordReader<Text, GraphNodeWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		// RecordReader custo.
		return new ParcoursGraphesRecordReader();
	}
}
