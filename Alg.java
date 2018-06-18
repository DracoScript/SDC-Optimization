import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

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
        int tweakType = -1;
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

            // SA - Tweak Type
            if (arg.equals("-w") || arg.equals("--tweak")) {
                if (argpos < args.length)
                    tweakType = Integer.parseInt(args[argpos++]);
                else {
                    System.err.println("\ntype of tweak not defined (-w number | --tweak number)\n");
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
        System.out.println("Running Algorithms...\n");
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
                    SA(args, alpha, loop, tweakType);
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
                          +"    -w, --tweak          int      -1  [SA only] type of tweak\n"
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
    public static void SA(String[] args, double alpha, int loop, int tweakType) {

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
            List<Integer> R;
            if (tweakType == -1)
                R = Tweak(S, total);
            else
                R = TweakImprove(S, Ks, total, tweakType);

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
            if (loopcount == 0 && outputfile != null) {
                String data = new String(generation+" "+Kb+" [");
                for (int i : Best)
                    data += i+",";
                outprint.add(data+"] "+(System.currentTimeMillis()-timer));
            }

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
            if (outputfile != null) {
                String data = new String(generation+" "+Kb+" [");
                for (int i : Best)
                    data += i+",";
                outprint.add(data+"] "+(System.currentTimeMillis()-timer));
            }

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
     * Tweak Function
     */
    public static List<Integer> TweakImprove(List<Integer> R, int Kr, int total, int amount) {

        // Determine where to tweak
        int pos = (int)(Math.random() * R.size());

        // Find the value to be replaced that minimized the solution
        List<Integer> T = R;
        int Kb = Kr;
        int Kt;
        int r = pos;
        for (int i = 1 ; i < C[0].length ; i++) {
            T.set(pos, i);
            Kt = K(T);
            if (Kt < Kb) {
                r = i;
                Kb = Kt;
                if (--amount <= 0)
                    break;
            }
        }

        // Only accept a different value
        if (r == R.get(pos)) {
            do {
                r = 1 + (int)(Math.random() * total);
            } while (r == R.get(pos));
        }

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
            if (i < cross) {

                // Add parent values
                Children.get(0).add(Pa.get(i));
                Children.get(1).add(Pb.get(i));

            }
            if (i >= cross) {

                int Ca = Pb.get(i);
                int Cb = Pa.get(i);

                // Verify an solve repeating values
                if (Children.get(0).contains(Ca)) {
                    for (int j = 0 ; j < i ; j++) {
                        if (Children.get(0).get(j) == Ca) {
                            if (Children.get(0).contains(Pb.get(j))) {
                                while (Children.get(0).contains(Ca)) {
                                    Ca = 1 + (int)(Math.random() * total);
                                }
                            }
                            else {
                                Ca = Pb.get(j);
                            }
                            break;
                        }
                    }
                }
                if (Children.get(1).contains(Cb)) {
                    for (int j = 0 ; j < i ; j++) {
                        if (Children.get(1).get(j) == Cb) {
                            if (Children.get(1).contains(Pa.get(j))) {
                                while (Children.get(1).contains(Cb)) {
                                    Cb = 1 + (int)(Math.random() * total);
                                }
                            }
                            else {
                                Cb = Pa.get(j);
                            }
                            break;
                        }
                    }
                }

                // Add new values
                Children.get(0).add(Ca);
                Children.get(1).add(Cb);

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

        return (int)days;
    }

    /**
     * Prepare Cost Matrix Function
     */
    public static void PrepareCostMatrix() {

        // ???? to be made, test with random values
        List<String[][]> tle = LoadTLE();
        if (tle == null) {

            // If couldn't read TLE data from input a random cost matrix will be used
            System.out.println("Generating Random Cost Matrix...\n");
            RandomCostMatrix(365, 100, 200);

        }
        else {

            // After reading TLE data from input the cost matrix must be generated
            System.out.println("Generating TLE Based Cost Matrix...\n");
            SimulateCostMatrix(365, tle);

        }


    }

    /**
     * Load TLE from input
     */
    public static List<String[][]> LoadTLE() {

        // Read input file
        if (inputfile == null)
            return null;

        try {

            // Initiate file reader
            BufferedReader input = new BufferedReader(new FileReader(inputfile));

            // Read data and store
            List<String[][]> readData = new ArrayList<String[][]>();
            String line = input.readLine();
            String[][] currData = new String[2][13];
            while (line != null && line.length() > 0) {

                // Verify line number
                switch (line.charAt(0)) {

                    // First line of a TLE
                    case '1':
                        // Satellite Number
                        currData[0][0] = line.substring(2, 6);
                        // Classification (U=Unclassified)
                        currData[0][1] = ""+line.charAt(7);
                        // International Designator (Last two digits of launch year)
                        currData[0][2] = line.substring(9, 10);
                        // International Designator (Launch number of the year)
                        currData[0][3] = line.substring(11, 13);
                        // International Designator (Piece of the launch)
                        currData[0][4] = line.substring(14, 16);
                        // Epoch Year (Last two digits of year)
                        currData[0][5] = line.substring(18, 19);
                        // Epoch (Day of the year and fractional portion of the day)
                        currData[0][6] = line.substring(20, 31);
                        // First Time Derivative of the Mean Motion
                        currData[0][7] = line.substring(33, 42);
                        // Second Time Derivative of Mean Motion (decimal point assumed)
                        currData[0][8] = line.substring(44, 51);
                        // BSTAR drag term (decimal point assumed)
                        currData[0][9] = line.substring(53, 60);
                        // Ephemeris type
                        currData[0][10] = ""+line.charAt(62);
                        // Element number
                        currData[0][11] = line.substring(64, 67);
                        // Checksum (Modulo 10) = (Letters, blanks, periods, plus signs = 0; minus signs = 1)
                        currData[0][12] = ""+line.charAt(68);
                        break;

                    // Second line of a TLE
                    case '2':
                        // Satellite Number
                        currData[1][0] = line.substring(2, 6);
                        // Inclination [Degrees]
                        currData[1][1] = line.substring(8, 15);
                        // Right Ascension of the Ascending Node [Degrees]
                        currData[1][2] = line.substring(17, 24);
                        // Eccentricity (decimal point assumed)
                        currData[1][3] = line.substring(26, 32);
                        // Argument of Perigee [Degrees]
                        currData[1][4] = line.substring(34, 41);
                        // Mean Anomaly [Degrees]
                        currData[1][5] = line.substring(43, 50);
                        // Mean Motion [Revs per day]
                        currData[1][6] = line.substring(52, 62);
                        // Revolution number at epoch [Revs]
                        currData[1][7] = line.substring(63, 67);
                        // Checksum (Modulo 10)
                        currData[1][8] = ""+line.charAt(68);
                        // add TLE to data and reset current data
                        readData.add(currData);
                        currData = new String[2][13];
                        break;

                }

                // Próxima linha
                line = input.readLine();

            }

            return readData;

        }
        catch (Exception e) {
            System.err.println("Error: "+e);
        }

        return null;

    }

    /**
     * Simulate Cost Matrix Function
     */
    public static void SimulateCostMatrix(int maxdate, List<String[][]> tle) {

        // Empiric Vehicle Values
        double forwardAccel = 0.1; // Revolutions / day²
        double orbitalAccel = 5; // Degrees / day²

        // Initiate debris information
        double[][] debris = new double[tle.size()+1][4];
        debris[0][0] = 0; // Inclination - Launch considered at equator line
        debris[0][1] = 0; // Ascending Node - Since launch is at equator, there is no ascending node
        debris[0][2] = 0; // Argument of Perigee - Launch considered at greenwich meridian
        debris[0][3] = 0; // Mean Motion - Launch is stationary

        // Read TLE data to be used in calculations
        for (int d = 1 ; d < debris.length ; d++) {
            debris[d][0] = Double.parseDouble(tle.get(d-1)[1][1]); // Inclination
            debris[d][1] = Double.parseDouble(tle.get(d-1)[1][2]); // Ascending Node
            debris[d][2] = Double.parseDouble(tle.get(d-1)[1][4]); // Argument of Perigee
            debris[d][3] = Double.parseDouble(tle.get(d-1)[1][6]); // Mean Motion
        }

        // Compute time for orbital changes (same for every date)
        double[][] timeLatitude = new double[debris.length][debris.length];
        double[][] timeLongitude = new double[debris.length][debris.length];
        for (int d1 = 0 ; d1 < debris.length ; d1++) {
            for (int d2 = 0 ; d2 < debris.length ; d2++) {
                // Since this matrix is simetric, there is no need to calculate repeating values                
                if (d1 <= d2) {
                    // Step 1: Inclination Correction = Accelerate until mean inclination, then Slowdown (same as Accelerate time)
                    timeLatitude[d1][d2] = 2 * Position2Time(Math.abs(debris[d1][0] - debris[d2][0]), orbitalAccel);
                    // Step 2: Ascending Node Correction = Accelerate until mean ascending node, then Slowdown (same as Accelerate time)
                    timeLongitude[d1][d2] = 2 * Position2Time(Math.abs(debris[d1][1] - debris[d2][1]), orbitalAccel);
                }
                else {
                    timeLatitude[d1][d2] = timeLatitude[d2][d1];
                    timeLongitude[d1][d2] = timeLongitude[d2][d1];
                }
            }
        }

        // Create maxdate cost matrices
        C = new double[maxdate][tle.size()+1][tle.size()+1];
        for (int d = 0 ; d < C.length ; d++) {
            // Current position index
            for (int c = 0 ; c < C[d].length ; c++) {
                // Days from current position to each possible next debris
                for (int n = 0 ; n < C[d][c].length ; n++) {
                    if (c == n) {
                        // If both current position and next debris are the same, forces 0 days
                        C[d][c][n] = 0;
                    }
                    else {
                        // Step 3: Motion & Position Correction = Accelerate/Slowdown to reach same speed and position (both directions are checked)
                        double timeInOrbit = SpeedAndPostion2Time((PositionOverTime(debris[c][2], debris[c][3], d) - PositionOverTime(debris[n][2], debris[n][3], d)), (debris[c][3] - debris[n][3]), forwardAccel);
                        // Step 4: Calculate simulated time cost from current to next position
                        C[d][c][n] = Math.max(Math.max(timeLatitude[c][n], timeLongitude[c][n]), timeInOrbit);
                    }
                }
            }
        }

    }

    /**
     * Uniform Motion - Position after Time
     *  UM Position Formula -> s' = s + v.t
     */
    public static double PositionOverTime(double position, double speed, int time) {

        // Ignoring full revolutions
        return  (position + (speed * time)) % 360;

    }

    /**
     * Uniformly Accelerated Motion - Time for Stationary to Stationary Positions Change
     *  UAM Position Formula -> s' = s + v.t + a.t²/2
     *                       -> t² + 2.v.t/a = 2.(s'-s)/a
     *                       -> t² = 2.(s'-s)/a
     *                       -> t = sqrt(2.(s'-s)/a)
     *                       -> accelerate half the way, then slowdown the rest to keep 0 speed
     */
    public static double Position2Time(double position, double acceleration) {

        // 2 times the time needed to accelerate to half the way, since it takes the same amount of time to slowdown
        return  2 * Math.sqrt(position / acceleration);

    }

    /**
     * Uniformly Accelerated Motion - Time for Position and Speed Changes
     *  UAM Speed Formula -> v' = v + a.t
     *                    -> t = (v'-v) / a
     *  UAM Position Formula -> s' = s + v.t + a.t²/2
     *                       -> 0 = (s-s') + v.t + a.t²/2
     *                       -> 0 = (s-s') + (v).t + (a/2).t²
     *                       -> t = Best Value of Bhaskara Solving for Roots
     */
    public static double SpeedAndPostion2Time(double position, double speed, double acceleration) {

        // Compute time to change speed to match
        double timeMotion = speed / acceleration;

        // Compute position variation for that speed variations
        double positionMotion = (speed * timeMotion) + (acceleration * (Math.pow(timeMotion, 2)) / 2) % 360;

        // Compute time for starting position (after changing speed) to half way point to be reached
        double c = (positionMotion - (position/2));
        double delta = (Math.pow(speed, 2)) - (2 * acceleration * c); // b²-4.a.c;

        // Search a delta that leads to real roots (negative delta means imaginary roots)
        int aux = 1;
        while (delta < 0) {
            c += aux * 360;
            double auxdelta = (Math.pow(speed, 2)) - (2 * acceleration * c); // b²-4.a.c;
            if (auxdelta < delta)
                aux *= -1;
            delta = auxdelta;
        }

        // Compute roots
        double root1 = (-speed + Math.sqrt(delta)) / (2 * acceleration); // (-b + sqrt(detla)) / 2a
        double root2 = (-speed - Math.sqrt(delta)) / (2 * acceleration); // (-b - sqrt(detla)) / 2a

        // Choose the best root
        double time = Math.min(Math.abs(root1), Math.abs(root2));

        // Compute the time for the second half of the position change, when speed is matched
        time += Math.sqrt(Math.abs(position) / acceleration);

        return time;
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
                        C[d][c][n] = (Math.random() * maxtransfer);
                }
            }
        }

    }

}