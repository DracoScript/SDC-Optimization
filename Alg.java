import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

///////////////////////////////////////////////////////////////////////////////
// ------------------------------------------------------------------------- //
// ------------------------------ Algorithms ------------------------------- //
// ------------------------------------------------------------------------- //
///////////////////////////////////////////////////////////////////////////////

/**
 * Algorithms Class
 */
public class Alg {
    
    // Global Variable
    public static String inputfile;         // Input file path + name
    public static String outputfile;        // Output file path + name
    public static List<String> outprint;    // Output data to write on file
    public static double[][][] C;           // Cost Matrix
    public static int G = 100;              // Number of generations
    public static int P = 10;               // Number of generations for each checkpoint
    public static int Ssize = 5;            // Solutions size
    public static int total = 0;            // Amount of debris
    public static List<Integer> Best;       // Best Solution
    public static int Kb;                   // Fitness of Best Solution
    public static int mdays = 365;          // Maximum days allowed for the mission
    public static int repeat = 1;           // Number of repetitions to run in the same cost matrix
    public static boolean noprint = false;  // Number of repetitions to run in the same cost matrix
    public static long timer;               // Timer to keep track of how long the algorithms are taking

    /**
     * Main Function
     *  Check for first argument expecting a specific algorithm call
     */
    public static void main(String[] args) throws IOException {

        // Initiate argument variables
        List<String> algorithm = new ArrayList<String>();
        double alpha = 0.99;
        int loop = 1;
        int popsize = 10;
        int elitesize = 2;
        double mp = 0.1;

        // Argument Parse
        int argpos = 0;
        while (argpos < args.length && args[argpos].startsWith("-")) {

            // Read current argument
            String arg = args[argpos++];

            // Algorithm
            if (arg.equals("-a") || arg.equals("--algorithm")) {
                if (argpos < args.length){
                    if (!args[argpos].equals("SA") && !args[argpos].equals("GA")) {
                        System.err.println("\nWrong algorithm "+args[argpos]+" is unknown.)\n");
                        TerminateWithHelp();
                    }
                    algorithm.add(args[argpos++]);
                }
                else {
                    System.err.println("\nAlgorithm must be informed (-a NAME | --algorithm NAME)\n");
                    TerminateWithHelp();
                }
            }

            // Input
            if (arg.equals("-i") || arg.equals("--input")) {
                if (argpos < args.length)
                    inputfile = args[argpos++];
                else {
                    System.err.println("\nInput file not infomed (-i filename | --input filename)\n");
                    TerminateWithHelp();
                }
            }

            // Output
            if (arg.equals("-o") || arg.equals("--output")) {
                if (argpos < args.length)
                    outputfile = args[argpos++];
                else {
                    System.err.println("\nOutput file not infomed (-o filename | --output filename)\n");
                    TerminateWithHelp();
                }
            }

            // Generations
            if (arg.equals("-g") || arg.equals("--generations")) {
                if (argpos < args.length)
                    G = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nNumber of generations not infomed (-g number | --generations number)\n");
                    TerminateWithHelp();
                }
            }

            // Generation Checkpoint
            if (arg.equals("-c") || arg.equals("--checkpoint")) {
                if (argpos < args.length)
                    P = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nNumber of generations for each checkpoint not (-c number | --checkpoint number)\n");
                    TerminateWithHelp();
                }
            }

            // Solution Size
            if (arg.equals("-s") || arg.equals("--size")) {
                if (argpos < args.length)
                    Ssize = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nSolution size not informed (-s number | --size number)\n");
                    TerminateWithHelp();
                }
            }

            // Maximum Days Restriction
            if (arg.equals("-d") || arg.equals("--days")) {
                if (argpos < args.length)
                    mdays = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nMaximum number of days for each mission not informed (-d number | --days number)\n");
                    TerminateWithHelp();
                }
            }

            // Repetitions
            if (arg.equals("-r") || arg.equals("--repeat")) {
                if (argpos < args.length)
                    repeat = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nNumber of repetitions not informed (-r number | --repeat number)\n");
                    TerminateWithHelp();
                }
            }

            // Ignore prints
            if (arg.equals("-n") || arg.equals("--noprint")) {
                noprint = true;
            }

            // SA - Temperature Decay Rate
            if (arg.equals("-t") || arg.equals("--temperature")) {
                if (argpos < args.length)
                    alpha = Double.parseDouble(args[argpos++]);
                else {
                    System.err.println("\nTemperature decay rate not informed (-t number | --temperature number)\n");
                    TerminateWithHelp();
                }
            }

            // SA - Loop
            if (arg.equals("-l") || arg.equals("--loop")) {
                if (argpos < args.length)
                    loop = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nAmount of loops without changing temperature not defined (-l number | --loop number)\n");
                    TerminateWithHelp();
                }
            }

            // GA - Population Size
            if (arg.equals("-p") || arg.equals("--popsize")) {
                if (argpos < args.length)
                    popsize = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nPopulation size not informed (-p number | --popsize number)\n");
                    TerminateWithHelp();
                }
            }

            // GA - Elite Size
            if (arg.equals("-e") || arg.equals("--elitesize")) {
                if (argpos < args.length)
                    elitesize = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nAmount of elite individuals not informed (-e number | --elitesize number)\n");
                    TerminateWithHelp();
                }
            }

            // GA - Mutation Probability
            if (arg.equals("-m") || arg.equals("--mutation")) {
                if (argpos < args.length)
                    mp = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\nMutation probability not informed (-m number | --mutation number)\n");
                    TerminateWithHelp();
                }
            }

            // Help
            if (arg.equals("-h") || arg.equals("--help")) {
                TerminateWithHelp();
            }

        }

        // Check required arguments
        if (algorithm.size() <= 0) {
            System.err.println("\nMissing argument algorithm (-a NAME | --algorithm NAME)\n");
            TerminateWithHelp();
        }

        // Prepare time dependent Cost Matrix
        PrepareCostMatrix();

        // Adjust number of generations
        if (G < 0)
            G = 1;

        // Adjust number of debris (to collect and total)
        if (C.length > 0) {
            if (C[0].length > 0)
                total = C[0][0].length-1;
        }
        if (Ssize > total)
            Ssize = total;

        // Adjust number of maximum days restriction
        if (mdays < 0)
            mdays = 1;

        // Adjust number of generations for each checkpoint
        if (P <= 0 || P > G)
            P = G;

        // Adjust elite size to avoid losing 1 individual in population when filling next generation
        if (elitesize < popsize) {
            if (elitesize % 2 != popsize % 2)
                elitesize++;
        }
        else
            elitesize = popsize;

        String baseoutputfile = outputfile;
        int filenumber = 0;

        // Call Algorithms
        for (int r = repeat ; r > 0 ; r--) {

            for (String A : algorithm) {

                // Output file name
                if (baseoutputfile != null) {
                    outputfile = baseoutputfile+"_"+(++filenumber)+"_"+A+".stat";
                    outprint = new ArrayList<String>();
                }

                // Initialize timer
                timer = 0;

                // Verify which algorithm to run
                if (A.equals("SA")) {
                    // Simulated Annealing
                    SA(args, alpha, loop);
                }
                else if (A.equals("GA")) {
                    // Genetic Algorithm
                    GA(args, popsize, elitesize, mp);
                }

                // Show Results
                if (!noprint) {

                    System.out.println("Results:");
                    System.out.print("  Best Solution = [ ");
                    for (int i : Best) {
                        System.out.print(i+" ");
                    }
                    System.out.println("]");
                    if (Kb <= mdays)
                        System.out.println("  Mission Duration (Fitness) = "+Kb+" days");
                    else
                        System.out.println("  Fitness = over "+mdays+" limit");
                    System.out.println("  Time Consumed: "+(timer)+"ns\n");

                }

                // Write output file
                if (baseoutputfile != null) {
                    PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
                    for (String line : outprint)
                        writer.println(line);
                    writer.close();
                }

            }

        }

        return;

    }

    /**
     * Terminate with Help Function
     */
    public static void TerminateWithHelp() {

        // Print Help Text
        System.err.println("COMMAND LINE PARAMETERS:\n"
                          +"    INPUT               TYPE DEFAULT  DESCRIPTION\n"
                          +"\n"
                          +"  required:\n"
                          +"    -a, --algorithm   string    none  selected algorithm\n"
                          +"                                        SA = Simulated Annealing\n"
                          +"                                        GA = Genetic Algorithm\n"
                          +"\n"
                          +"  optional:\n"
                          +"    -i, --input       string    none  path to debris data file\n"
                          +"    -o, --output      string    none  path to output file\n"
                          +"    -g, --generations    int     100  number of generations\n"
                          +"    -c, --checkpoint     int      10  generations between checkpoints\n"
                          +"    -s, --size           int       5  solution size (number of debris)\n"
                          +"    -d, --days           int     365  limit of mission days\n"
                          +"    -r, --repeat         int       1  repeat algorithms in same matrix\n"
                          +"    -n, --noprint       bool          avoid printing in console\n"
                          +"\n"
                          +"    -t, --temperature double    0.99  [SA only] temperature decay rate\n"
                          +"    -l, --loop           int       1  [SA only] loops/generation\n"
                          +"\n"
                          +"    -p, --popsize        int      10  [GA only] population size\n"
                          +"    -e, --elitesize      int       2  [GA only] amount of elite\n"
                          +"    -m, --mutation    double     0.1  [GA only] mutation probability\n"
                          );

        // Terminate
        System.exit(0);

    }

    /**
     * Simulated Annealing Function
     */
    public static void SA(String[] args, double alpha, int loop) {

        // Header
        if (!noprint) {

            System.out.println("\n"
                              +"///////////////////////////////////////////////////////////////////////////////\n"
                              +"// ------------------------------------------------------------------------- //\n"
                              +"// -------------------------- Simulated Annealing -------------------------- //\n"
                              +"// ------------------------------------------------------------------------- //\n"
                              +"///////////////////////////////////////////////////////////////////////////////\n"
                              +"\n"
                              +"Subject: Bio-inspired Computing\n"
                              +"Student: Eric G. Müller\n");

            // Print parameters
            System.out.println("Parameters:");
            System.out.println("  Number of generations = "+G+" (checkpoint at each "+P+")");
            System.out.println("  Number of debris to collect = "+Ssize);
            System.out.println("  [Restriction] Maximum number of days in mission = "+mdays);
            System.out.println("  Temperature decay rate = "+alpha);
            System.out.println("");

        }

        // Variable declaration
        int generation = 0;                         // Initial generation
        int loopcount = 0;                          // Count iterations that are not changing temperature
        double t = 0;                               // Initial Temperature
        List<Integer> S;                            // Initial Solution

        // Step 1: Calculate initial temperature
        int K4t = 0;
        int delta = 0;

        // Initiate timer
        timer = System.currentTimeMillis();

        // Generate 10 more random solutions
        for (int j = 0 ; j < 10 ; j++){

            // Generates a new random solution
            S = new ArrayList<Integer>();
            for (int i = 0 ; i < Ssize ; i++) {

                // Create a proposed value and only accept it if it isn't in the solution
                int r;
                do {
                    r = 1 + (int)(Math.random() * total);
                } while (S.contains(r));

                // Add the random value to the solution
                S.add(r);
            }

            // Add up the difference in fitness
            if (j != 0)
                delta += Math.abs(K4t-K(S));
            K4t = K(S);

        }

        // Calculate temperature based on Johnson's method
        t = -delta/Math.log(0.8);
        if (!noprint)
            System.out.println("Initial Temperature: "+t);

        // Step 2: Generate initial solution
        if (!noprint)
            System.out.print("Initial Solution: [ ");
        S = new ArrayList<Integer>();
        for (int i = 0 ; i < Ssize ; i++) {

            // Create a proposed value and only accept it if it isn't in the solution
            int r;
            do {
                r = 1 + (int)(Math.random() * total);
            } while (S.contains(r));

            // Add the random value to the solution
            S.add(r);

            if (!noprint)
                System.out.print(r+" ");
        }
        if (!noprint)
            System.out.println("]\n");
        int Ks = K(S);

        // Step 3: Set initial solution as best
        Best = S;
        Kb = Ks;

        // Step 4: Repeat
        do {

            // Step 6: Tweak current solution
            List<Integer> R = Tweak(S, total);

            // Step 7: Verify if tweaked solution must be used instead of the current solution
            int Kr = K(R);
            if (Kr < Ks) {
                // Step 8: Replace current solution for tweaked solution
                S = R;
                Ks = Kr;
            }

            // Step 9: Decrease temperatura
            if (++loopcount == loop) {
                t = t * alpha;
                ++generation;
                loopcount = 0;
            }

            // Step 10: Verify if current solution is best
            if (Ks < Kb) {
                Best = S;
                Kb = Ks;
            }

            // Checkpoint
            if (!noprint && loopcount == 0 && generation%P == 0) {

                System.out.print("["+generation+"] current solution: S = [ ");
                for (int i : S)
                    System.out.print(i+" ");
                System.out.println("] -> Fitness = "+Ks);
                System.out.print("["+generation+"] best solution: Best = [ ");
                for (int i : Best)
                    System.out.print(i+" ");
                System.out.println("] -> Fitness = "+Kb);
                System.out.println("["+generation+"] time: "+(System.currentTimeMillis()-timer)+"ns\n");

            }

            // Output
            if (outputfile != null)
                outprint.add(generation+" "+Kb+" "+Ks+" "+(System.currentTimeMillis()-timer));

        // Step 12: Stop condition if maximum number of generations reached or best is the ideal solution
        } while (generation < G || Kb <= 0);

        // Terminate timer
        timer = System.currentTimeMillis() - timer;

        // Step 13: Return best solution (already global variables)
        return;

    }

    /**
     * Genetic Algorithm Function
     */
    public static void GA(String[] args, int popsize, int elitesize, double mp) {

        // Header
        if (!noprint) {

            System.out.println("\n"
                              +"///////////////////////////////////////////////////////////////////////////////\n"
                              +"// ------------------------------------------------------------------------- //\n"
                              +"// --------------------------- Genetic Algorithm --------------------------- //\n"
                              +"// ------------------------------------------------------------------------- //\n"
                              +"///////////////////////////////////////////////////////////////////////////////\n"
                              +"\n"
                              +"Subject: Bio-inspired Computing\n"
                              +"Student: Eric G. Müller\n");

            // Print parameters
            System.out.println("Parameters:");
            System.out.println("  Number of generations = "+G+" (checkpoint at each "+P+")");
            System.out.println("  Number of debris to collect = "+Ssize);
            System.out.println("  [Restriction] Maximum number of days in mission = "+mdays);
            System.out.println("  GA - Population Size = "+popsize);
            System.out.println("  GA - Amount of Elite individuals = "+elitesize);
            System.out.println("  GA - Mutation probability = "+mp);
            System.out.println("");

        }

        // Variable declaration
        int generation = 0;                         // Initial generation

        // Step 3: Initiate population
        List<List<Integer>> pop = new ArrayList<List<Integer>>();
        List<Integer> Ks = new ArrayList<Integer>();

        // Initiate timer
        timer = System.currentTimeMillis();

        // Step 4: In each population
        for (int j = 0 ; j < popsize ; j++) {

            // Step 5: Create a random solution for each individual
            pop.add(new ArrayList<Integer>());
            for (int i = 0 ; i < Ssize ; i++) {

                // Create a proposed value and only accept it if it isn't in the solution
                int r;
                do {
                    r = 1 + (int)(Math.random() * total);
                } while (pop.get(j).contains(r));

                // Add the random value to the solution
                pop.get(j).add(r);
            }
            Ks.add(K(pop.get(j)));

        }

        // Step 6: Initiate Best Solution
        Best = new ArrayList<Integer>();
        Kb = 99999;

        // Step 7: Repeat
        do {

            // Step 8: For each individual
            for (int j = 0 ; j < popsize ; j++) {

                // Step 9: Verify if the current individual is the best
                if (Best.size() <= 0 || Ks.get(j) < Kb) {
                    Best = pop.get(j);
                    Kb = Ks.get(j);
                }

            }

            // Step 11: Start next generation with the elite
            List<List<Integer>> nextgen = new ArrayList<List<Integer>>();
            List<Integer> nextKs = new ArrayList<Integer>();
            int refK = -1;
            for (int i = 0 ; i < elitesize ; i++) {

                // Check all individuals to find the elite one (starting from the last elite)
                int Ke = Ks.get(0);
                int je = 0;
                for (int j = 1 ; j < popsize ; j++) {
                    if (Ks.get(j) > refK && Ks.get(j) < Ke) {
                        je = j;
                        Ke = Ks.get(j);
                    }
                }

                // Add elite one to next generation and set new reference
                refK = Ke;
                nextgen.add(pop.get(je));
                nextKs.add(Ke);

            }

            // Step 12: Fill the rest of the next generation
            for (int i = (popsize - elitesize)/2 ; i > 0 ; --i) {

                // Step 13: Crossover in random position using 2 parents choosen by tournament
                List<List<Integer>> Children = Crossover(pop.get(Tournament(Ks, 4)), pop.get(Tournament(Ks, 4)));

                // Step 14: Add children to next generation after Mutation
                Children = Mutation(Children, 0.1);
                for (List<Integer> child : Children) {
                    nextgen.add(child);
                    nextKs.add(K(child));
                }

            }

            // Step 15: New Generation
            pop = nextgen;
            Ks = nextKs;

            // Next generation
            ++generation;

            // Checkpoint
            if (!noprint) {

                if ((generation) % P == 0) {

                    System.out.println("["+generation+"] current Fitness = "+Ks);
                    System.out.println("["+generation+"] best Fitness = "+Kb);
                    System.out.println("["+generation+"] time: "+(System.currentTimeMillis()-timer)+"ns\n");

                }

            }

            // Output
            if (outputfile != null)
                outprint.add(generation+" "+Kb+" "+Ks+" "+(System.currentTimeMillis()-timer));

        // Step 16: Stop condition if maximum number of generations reached or best is the ideal solution
        } while (generation < G || Kb <= 0);

        // Terminate timer
        timer = System.currentTimeMillis() - timer;

        // Step 17: Return best solution (already global variables)
        return;

    }

    /**
     * Tweak Function
     */
    public static List<Integer> Tweak(List<Integer> R, int total) {

        // Determine where to tweak
        int pos = (int)(Math.random() * R.size());

        // Create a proposed value and only accept a different value
        int r;
        do {
            r = 1 + (int)(Math.random() * total);
        } while (r == R.get(pos));

        // If the tweaked value is already on the solution, switch it
        if (R.contains(r)) {
            for (int i = 0 ; i < R.size() ; i++) {
                if (R.get(i) == r) {
                    R.set(i, R.get(pos));
                    break;
                }
            }
        }

        // Tweak the solution with the random value
        R.set(pos, r);

        return R;

    }

    /**
     * Tournament Function
     */
    public static int Tournament(List<Integer> Ks, int amount) {

        // Generate the first random position
        int winpos = (int)(Math.random() * Ks.size());

        // Compare with other random positions
        for (int i = 1 ; i < amount ; i++) {

            // Generate next random positions
            int pos = (int)(Math.random() * Ks.size());

            // Replace position based on best fitness
            if (Ks.get(pos) < Ks.get(winpos))
                winpos = pos;

        }

        return winpos;

    }

    /**
     * Crossover Function
     */
    public static List<List<Integer>> Crossover(List<Integer> Pa, List<Integer> Pb) {

        // Initiate children
        List<List<Integer>> Children = new ArrayList<List<Integer>>();
        Children.add(new ArrayList<Integer>());
        Children.add(new ArrayList<Integer>());

        // Find where to cross
        int cross = (int)(Math.random() * Pa.size());

        // Build new child
        for (int i = 0 ; i < Pa.size() ; i++) {

            // Input values are added based on crossing point
            int Ca = Pa.get(i);
            int Cb = Pb.get(i);
            if (i >= cross) {
                Ca = Pb.get(i);
                Cb = Pa.get(i);
            }

            // Add value to children
            Children.get(0).add(Ca);
            Children.get(1).add(Cb);

            // Verify an solve repeating values
            if (Children.get(0).contains(Ca)) {
                for (int j = 0 ; j < i ; j++) {
                    if (Children.get(0).get(j) == Ca) {
                        Children.get(0).set(i, Children.get(0).get(j));
                        Children.get(0).set(j, Ca);
                        break;
                    }
                }
            }
            if (Children.get(1).contains(Cb)) {
                for (int j = 0 ; j < i ; j++) {
                    if (Children.get(1).get(j) == Cb) {
                        Children.get(1).set(i, Children.get(1).get(j));
                        Children.get(1).set(j, Cb);
                        break;
                    }
                }
            }

        }

        return Children;

    }

    /**
     * Mutation Function
     */
    public static List<List<Integer>> Mutation(List<List<Integer>> Children, double mp) {

        // Each child must try to mutate
        for (List<Integer> child : Children) {

            // Try mutating each index of the solution
            for (int j = 0 ; j < child.size() ; j++) {

                // Only mutate if random value is under mutation probability
                if (Math.random() >= mp)
                    continue;

                // Create a proposed value and only accept a different value
                int r;
                do {
                    r = 1 + (int)(Math.random() * total);
                } while (r == child.get(j));

                // If the tweaked value is already on the solution, switch it
                if (child.contains(r)) {
                    for (int i = 0 ; i < child.size() ; i++) {
                        if (child.get(i) == r) {
                            child.set(i, child.get(j));
                            break;
                        }
                    }
                }

                // Tweak the solution with the random value
                child.set(j, r);

            }

        }

        // Return as children
        return Children;

    }

    /**
     * Fitness Function
     */
    public static int K(List<Integer> S) {

        // Initiate day count
        int days = 0;

        // Verify cost of edge based on each debris
        for (int j = 0 ; j < S.size() ; j++) {

            // If cost exceeds the limit of avaiable mission days in Cost Matrix return a big number
            if (days >= C.length)
                return mdays + 25;

            // Check how many days for transfer from current position to next debris + 5 days for processing
            if (j > 0)
                days += C[days][S.get(j-1)][S.get(j)] + 5;
            else
                days += C[days][S.get(j)][0] + 5;

        }

        return days;
    }

    /**
     * Prepare Cost Matrix Function
     */
    public static void PrepareCostMatrix() {

        // ???? to be made, test with random values
        RandomCostMatrix(365, 99, 200);

    }

    /**
     * Random Cost Matrix Function
     */
    public static void RandomCostMatrix(int maxdate, int amount, int maxtransfer) {

        // Create maxdate cost matrices
        C = new double[maxdate][amount+1][amount+1];
        for (int d = 0 ; d < C.length ; d++) {
            // Current position index
            for (int c = 0 ; c < C[d].length ; c++) {
                // Days from current position to each possible next debris
                for (int n = 0 ; n < C[d][c].length ; n++) {
                    // If both current position and next debris are the same, forces 0 days
                    if (c == n)
                        C[d][c][n] = 0;
                    else
                        C[d][c][n] = (int)(Math.random() * maxtransfer);
                }
            }
        }

    }

}