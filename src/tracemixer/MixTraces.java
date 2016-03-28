/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracemixer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import static java.lang.Math.abs;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author nassopoulos-g
 */
public class MixTraces {

    List<BufferedReader> br = new LinkedList<>();
    FileWriter fileWritter = null;
    BufferedWriter bufferWritter = null;
    String currTime = "";
    boolean flagFedX;
    int continious;
    int delay;
    boolean flagSerial;
    boolean flagMix;
    String collection = "MT";
    String epochTimestamp = "";
    int cntFi = 0;

    public MixTraces(boolean engineFedX, boolean orderSer, boolean enableMix, int sizeBlock, int pairWiseDelay) {

        flagFedX = engineFedX;
        flagSerial = orderSer;
        flagMix = enableMix;
        continious = sizeBlock;
        delay = pairWiseDelay;
    }

    public void init(String filePrefix) throws FileNotFoundException, IOException {

        List<String> myList = new LinkedList();

        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")) {
                cntFi++;
                System.out.println("File " + listOfFiles[i].getName());
                myList.add(listOfFiles[i].getName());
            }
        }

        for (int i = 1; i <= cntFi; i++) {

            BufferedReader myBr = new BufferedReader(new FileReader(filePrefix + Integer.toString(i) + ".txt"));
            br.add(myBr);
        }

        fileWritter = new FileWriter("capture.log", false);

        Collections.sort(myList);

        System.out.print(myList);

        bufferWritter = new BufferedWriter(fileWritter);
    }

    public boolean elemInListEquals(List<Integer> outerList, int search) {

        for (int item : outerList) {
            if (item == search) {

                return true;
            }
        }

        return false;
    }

    public List<Integer> getSequence(int sizeSeq, int maxRand, boolean duplicate) {

        List<Integer> sequence = new LinkedList<>();
        int n = 0;

        do {

            Random rand = new Random();

            n = rand.nextInt(maxRand) + 1;
            /* if(duplicate){
               
             n =  1; 
             }
             else{
             n = rand.nextInt(maxRand) + 1;
             }*/
            // 

            if (!elemInListEquals(sequence, n) || duplicate) {

                sequence.add(n);
            }
        } while (sequence.size() < sizeSeq);

        return sequence;
    }

    public void startMixer() throws FileNotFoundException, IOException {

        init(collection);
        List<Integer> sequence = null;

        if (flagSerial) {

            sequence = new LinkedList<>();

            File folder = new File(".");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")) {

                    sequence.add(i);
                }
            }
        } else {

            sequence = getSequence(cntFi, cntFi, false);
        }

        System.out.println("Synthetic order: " + sequence);
        List<Integer> currLine = new LinkedList<>();
        List<Integer> maxReader = null;
        boolean flagSkip = false;
        boolean flagEndRound = false;
        boolean flagTerminate = false;

        int cntPost = 0;

        int countNew = 0;
        for (int i = 0; i < sequence.size(); i++) {

            currLine.add(0);
        }

        while (true) {

            if (flagMix) {

                maxReader = getSequence(cntFi, continious, true);
            } else {
                maxReader = new LinkedList<>();

                File folder = new File(".");
                File[] listOfFiles = folder.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".txt")) {
                        maxReader.add(lineNumberCount(listOfFiles[i].getName()));
                    }
                }

            }

            //List<Integer> maxReader = getSequence(cntFiles, 5, true);
            String sCurrentLine = "";

            for (int i = 0; i < sequence.size(); i++) {

                int cnt = 0;
                cntPost = 0;
                countNew = 0;
                 // bufferWritter.write("File: "+(i+1)+" packets to parse: " + maxReader.get(i)+"\n");

                //System.out.println("File: "+(i+1)+"Entries to parse: " + maxReader.get(i));
                flagSkip = false;
                flagEndRound = false;
                flagTerminate = false;
                sCurrentLine = "";
                // bufferWritter.write("file" + sequence.get(i) +"\n");
                // while (!flagTerminate&&(sCurrentLine = br.get(i).readLine()) != null

                br.get(i).close();

                BufferedReader myBr = new BufferedReader(new FileReader(collection + Integer.toString(i + 1) + ".txt"));
                br.set(i, myBr);
                System.out.println("**********br size: " + br.size());
                System.out.println("Current query " + collection + (i + 1) + " loading lines: " + maxReader.get(i));
                while (!flagTerminate && (sCurrentLine = br.get(i).readLine()) != null
                        && cntPost <= maxReader.get(i)) {

                    countNew++;
                    int cntCurr = currLine.get(i);
                    if (countNew <= cntCurr) {

                        continue;
                    }
                    currLine.set(i, cntCurr + 1);

                    if (i == 3 && currLine.get(i) == 336) {

                        int rar = 0;
                    }

                    if (flagFedX && sCurrentLine.contains("POST") || !flagFedX && (sCurrentLine.contains("GET") && sCurrentLine.contains("/sparql/?query"))) {

                        cntPost++;
                        cnt++;
                        flagSkip = true;

                        if (cntPost == maxReader.get(i)) {

                            flagEndRound = true;
                        }
                    } else if (sCurrentLine.contains("} }") || (sCurrentLine.contains("GET") && !sCurrentLine.contains("/sparql/?query"))
                            || sCurrentLine.contains("\"boolean\": false}") || sCurrentLine.contains("\"boolean\": true}")) {
                        if (!sCurrentLine.contains("GET")) {

                            System.out.println(sCurrentLine);
                            //  bufferWritter.write("File: " + (i + 1) + " lINE " + currLine.get(i) + "--" + sCurrentLine + "\n");
                            bufferWritter.write(sCurrentLine + "\n");

                        }
                        if (flagEndRound) {

                            flagTerminate = true;
                        }

                        flagSkip = false;
                    }

                    if (flagSkip) {

                        if (sCurrentLine.contains("Date: ")) {

                            String time = sCurrentLine.substring(sCurrentLine.indexOf("GMT") - 9, sCurrentLine.indexOf("GMT") - 1);

                            if (epochTimestamp.equalsIgnoreCase("")) {

                                time = "00:00:01";
                                epochTimestamp = "00:00:01";
                            }

                            time = genRandTimeStamp(time);
                            String tmpLineLeft = sCurrentLine.substring(0, sCurrentLine.indexOf("GMT") - 9);
                            tmpLineLeft += time;
                            tmpLineLeft += sCurrentLine.substring(sCurrentLine.indexOf("GMT") - 1);
                            sCurrentLine = tmpLineLeft;
                        }

                        System.out.println(sCurrentLine);
                        // bufferWritter.write("File: " + (i + 1) + " lINE " + currLine.get(i) + "--" + sCurrentLine + "\n");
                        bufferWritter.write(sCurrentLine + "\n");
                    }

                    sCurrentLine = "";
                    //  System.out.println("Current query "+collection+(i+1)+ " loading lines: "+cntPost);

                }
                System.out.println("Helloooooooooo read from " + collection + (i + 1) + " lines: " + cntPost);
            }

            boolean flag = true;
            for (int k = 0; k < br.size(); k++) {

                if (br.get(k).readLine() != null) // if(cntPost<1209) 
                {
                    flag = false;
                }
            }

            if (flag) {

                break;
            }

        }

        bufferWritter.close();

    }

    public String genRandTimeStamp(String Time) {

        String newTime = "";

        int timeInSec = 0;

        int hour = 0, min = 0, second = 0;

        // System.out.println("-------------------------------------");
        if (currTime.equalsIgnoreCase("")) {

            currTime = Time;
        }

        Random rand = new Random();
        //int n = rand.nextInt(continious);
        int n = rand.nextInt(delay);
        System.out.println("Deeeeeeeeeeeeeeeelay: " + n);
        // System.out.println("*****************************************Random" + n);

        hour = Integer.parseInt(currTime.substring(0, 2));
        min = Integer.parseInt(currTime.substring(3, 5));
        second = Integer.parseInt(currTime.substring(6, 8));

        timeInSec = hour * 3600 + min * 60 + second;

        int intVal = abs(timeInSec + n);

        hour = intVal / 3600;
        min = (intVal % 3600) / 60;
        second = intVal % 60;

        newTime = String.format("%02d:%02d:%02d", hour, min, second);

        //System.out.println("time after " + Time);
        //System.out.println("-------------------------------------");
        currTime = newTime;
        return newTime;
    }

    /**
     * This method parses a source log file and counts its total number of lines
     *
     * @param fileName path to source log file
     * @return total number of log lines
     * @throws java.io.FileNotFoundException
     */
    public int lineNumberCount(String fileName) throws FileNotFoundException {

        int linesNumber = 0;

        /* Calculate maxLineNumber and  linePercentage */
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fileName)))) {
            lnr.skip(Long.MAX_VALUE);

            linesNumber = lnr.getLineNumber();

        } catch (IOException e) {

            e.printStackTrace(System.out);
        }

        return linesNumber;
    }
}