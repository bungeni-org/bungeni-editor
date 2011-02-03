package org.bungeni.utils.compare;
/** 
 * DifferenceChain - stores a chain of related BungeniNodeDifference objects
 * @author Ashok
 */
 public class DifferenceChain {
        /**
         * the node difference object
         */
        public BungeniNodeDifference diff;
        /**
         * next difference in chain
         */
        public DifferenceChain nextDifference = null;
        /**
         * previous difference in chain
         */
        public DifferenceChain prevDifference = null;
        
        @Override
        public String toString(){
            String output = "";
            output += "--- BEGIN DIFF CHAIN size ("+chainSize()+")--- \n";
            output += "DIFF : " + diff.toString() + "\n";
            DifferenceChain nextDiff = nextDifference;
            while (nextDiff != null) {
                output += "DIFF : " + nextDiff.diff.toString()  + "\n";
                nextDiff = nextDiff.nextDifference;
            }
            output += " --- END DIFFERENCE CHAIN --- \n";
            return output;
        }
        
/**
 * Returns the chainSize of the current chain
 * @return int
 */
        public int chainSize(){
            int n=1;
            DifferenceChain nextDiff = nextDifference;
            while (nextDiff != null) {
                n++;
                nextDiff = nextDiff.nextDifference;
            }
            return n;
        }
        
    }