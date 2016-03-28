/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracemixer;

import java.io.IOException;

/**
 *
 * @author nassopoulos-g
 */
public class Main {

    public static boolean help = true;
    public static boolean flagFedX = false;
    public static int block = 3;
    public static int delay = 3;
    public static boolean flagSerial = true;
    public static boolean flagMix = false;

    /**
     * This method prints a usage menu, when it is passed as argument or when an
     * arguments are not syntaxically correct when they are passed
     */
    public static void usage() {

        System.out.println(" Usage : --engine or -e <engine_used_for_traces>: for isolated queries' traces used as input, which are generated with either \"anapsid\" or \"fedx\" (by default \"fedx\")");
        System.out.println("         --order or -o <execution_order_simulation>: for setting \"serial\" or \"random\" simulated execution, of the isolated queries' traces used as input (by default \"serial\")");
        System.out.println("         --block or -b <block_size_of_continius_subqueries>: for setting the max size of block of continius subqueries, comming from the same isolated query's trace");
        System.out.println("         --delay or -d <delay_between_consequtive_subqueries>: for setting the max delay between two consequtive subqueries, of the produced mixed federated log");
        System.out.println("         --help or -h: for showing help");
    }

    /**
     * This method gets all the input arguments and treats every case
     *
     * @param args
     */
    public static void getParameteres(final String[] args) {

        if (args.length > 0) {

            System.out.println("User defined parameters of FETA: ");
        }

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {
                case "--engine":
                case "-e":
                    if (args[i + 1].startsWith("-")) {

                        System.out.println("Please give the engine name used for the input traces (\"fedx\" or \"anapsid\"): ");
                        System.exit(-1);
                    } else if (args[i + 1].equalsIgnoreCase("fedx")) {
                        flagFedX = true;
                    } else if (args[i + 1].equalsIgnoreCase("anapsid")) {

                        flagFedX = false;
                    } else {

                        System.out.println("Please give the engine name used for the input traces (\"fedx\" or \"anapsid\"): ");
                        System.exit(-1);
                    }
                    i++;
                    break;
                case "--order":
                case "-o":
                    if (args[i + 1].startsWith("-")) {

                        System.out.println("Please give the order wth wich queries will be executed (\"serial\" or \"random\"): ");
                        System.exit(-1);
                    } else if (args[i + 1].equalsIgnoreCase("serial")) {
                        flagSerial = true;
                    } else if (args[i + 1].equalsIgnoreCase("random")) {

                        flagSerial = false;
                    } else {

                        System.out.println("Please give the order wth wich queries will be executed (\"serial\" or \"random\"): ");
                        System.exit(-1);
                    }
                    i++;
                    break;
                case "--block":
                case "-b":
                    if (args[i + 1].startsWith("-")) {

                        System.out.println("Please give the max size of block of continius subqueries, from each isolated query trace's execution: ");
                        System.exit(-1);
                    } else {
                        block = Integer.parseInt(args[i + 1]);
                    }

                    i++;
                    break;
                case "--delay":
                case "-d":
                    if (args[i + 1].startsWith("-")) {

                        System.out.println("Please give the max delay between two consequtive subqueries, of the produced mixed federated log");
                        System.exit(-1);
                    } else {
                        delay = Integer.parseInt(args[i + 1]);
                    }

                    i++;
                    break;
                case "--help":
                case "-h":
                    help = true;
                    break;
                default:
                    System.out.println("Unknow argument");
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic heremixe

        if (help) {
            usage();
        } else {

            MixTraces myMixer = new MixTraces(flagFedX, flagSerial, flagMix, block, delay);
            myMixer.startMixer();
        }

    }

}