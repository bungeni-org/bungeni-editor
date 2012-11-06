package org.bungeni.utils.compare;

  public  class BungeniNodeDifference {

 

    public static enum DIFF_STATE { UPDATE, DELETE, INSERT, NONE};
    private DIFF_STATE diffState = DIFF_STATE.NONE;

    private Integer originalIndex = -1;
    private String originalName = "";

    private Integer updateFromIndex = -1;
    private String updateFromName = "" ;
            
           
                public DIFF_STATE getDiffState() {
                    return diffState;
                }

                public Integer getOriginalIndex() {
                    return originalIndex;
                }

                public String getOriginalName() {
                    return originalName;
                }

                public Integer getUpdateFromIndex() {
                    return updateFromIndex;
                }

                public String getUpdateFromName() {
                    return updateFromName;
                }
        
    
            public void diffInsert(String updateFrom, Integer updFromIndex) {
                originalIndex = -1;
                originalName = "";
                updateFromName = updateFrom;
                updateFromIndex = updFromIndex;
                diffState = DIFF_STATE.INSERT;
            }
            
            
            public void diffUpdate(String orig, Integer origIndex, String updateFrom, Integer updFromIndex) {
                originalIndex = origIndex;
                originalName = orig;
                updateFromName = updateFrom;
                updateFromIndex = updFromIndex;
                diffState = DIFF_STATE.UPDATE;
            }
            
            public void diffDelete (String orig, Integer origIndex) {
                originalIndex = origIndex;
                originalName = orig;
                diffState = DIFF_STATE.DELETE;
            }
            
            
    @Override
            public String toString(){
                String output = "";
                output = "DIFF_STATE=" + diffState.toString() + "; ";
                output += "Original=index:"+originalIndex+",Name="+originalName+ "; ";
                output += "Updated=index:"+updateFromIndex+",Name="+updateFromName+ "; ";
                return output;
            }
            
            public String getDiffKey(){
                return this.originalIndex+"->"+this.updateFromIndex;
            }
    
            
        }
        