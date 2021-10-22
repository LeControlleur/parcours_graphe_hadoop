package com.estia.app;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;


public class ParcoursGraphes {

	public enum GRAPH_COUNTERS {
        NB_NODES_UNFINISHED
    };
    public static void main(String[] args) throws Exception
    {
        Configuration conf=new Configuration();
        String[] ourArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
        String input_path=ourArgs[0];
        String output_path_prefix=ourArgs[1];
        String output_path="";
        int nb_step=0;
        long nb_nodes_non_black=0;

        while(true)
        {
            System.out.println("Itération n° " + (nb_step + 1));
            System.out.println("Il y a " + nb_nodes_non_black + " noeuds non noirs.");

            if(nb_step>0)
            {
                input_path=output_path+"/part-r*";
                if(nb_nodes_non_black==0)
                {
                    System.out.println("All nodes seen; final output directory: "+output_path);
                    break;
                }
            }

            nb_step=nb_step+1;
            output_path=output_path_prefix+"-step-"+nb_step;
            System.out.println("Execution cycle #"+nb_step+": input '"+input_path+"', output '"+output_path+"'");
            Job job=Job.getInstance(conf, "Parcours de graphes avec Format particulier");
            
            job.setJarByClass(ParcoursGraphes.class);
            job.setMapperClass(ParcoursGraphesMap.class);
            job.setReducerClass(ParcoursGraphesReduce.class);
            
			job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(GraphNodeWritable.class);
            job.setInputFormatClass(ParcoursGraphesInputFormat.class);
            job.setOutputFormatClass(ParcoursGraphesOutputFormat.class);

            
			FileInputFormat.addInputPath(job, new Path(input_path));
            FileOutputFormat.setOutputPath(job, new Path(output_path));
            
			if(!job.waitForCompletion(true))
            {
                System.out.println("ERROR: execution cycle #"+nb_step+" failed.");
                System.exit(-1);
            }
            Counters cn=job.getCounters();
            Counter c1=cn.findCounter(GRAPH_COUNTERS.NB_NODES_UNFINISHED);
            if(c1!=null)
                nb_nodes_non_black=c1.getValue();
        }
        System.exit(0);
    }

}
