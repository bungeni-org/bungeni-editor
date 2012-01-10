

package org.bungeni.editor.metadata;

/**
 * Language description object
 * captures language codes in both iso6339-1 and 6339-2
 * @author Ashok Hariharan
 */
 public class LanguageCode implements Comparable {
        private String languageCode;
        private String languageCode2;
        private String languageName;
        
        public LanguageCode(String langC, String langC2, String langN) {
            languageCode = langC;
            languageCode2 = langC2;
            languageName = langN;
        }
        
        @Override
        public String toString(){
            return getLanguageName();
        }

  public int compareTo(Object t) {
        LanguageCode lt = (LanguageCode) t;
        return this.getLanguageCode().compareTo(lt.getLanguageCode());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object t){
        LanguageCode lt = (LanguageCode) t;
        if (getLanguageCode().equals(lt.getLanguageCode())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.languageCode != null ? this.languageCode.hashCode() : 0);
        return hash;
    }

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * @return the languageName
     */
    public String getLanguageName() {
        return languageName;
    }


    public String getLanguageCode2(){
        return languageCode2;
    }
    }