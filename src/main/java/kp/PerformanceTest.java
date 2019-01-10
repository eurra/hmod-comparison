
package kp;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import kp.v1.hmod.RunnerHmod;
import kp.v1.simple.RunnerSimple;

public class PerformanceTest {    
    public static void main(String[] args) throws IOException {
        PrintWriter simpleWriter = new PrintWriter(new FileWriter("results-simple.txt"));
        PrintWriter hmodWriter = new PrintWriter(new FileWriter("results-hmod.txt"));
        int reps = 10000;
        
        // Simple
        for(int i = 0; i < reps; i++)
            RunnerSimple.run(10000, null);
        
        for(int i = 0; i < reps; i++)
            RunnerSimple.run(10000, simpleWriter);
        
        simpleWriter.close();
        
        // hMod
        for(int i = 0; i < reps; i++)
            RunnerHmod.run(RunnerHmod.getLoader(10000), null);
        
        for(int i = 0; i < reps; i++)
            RunnerHmod.run(RunnerHmod.getLoader(10000), hmodWriter);
        
        hmodWriter.close();       
    }
        
}
