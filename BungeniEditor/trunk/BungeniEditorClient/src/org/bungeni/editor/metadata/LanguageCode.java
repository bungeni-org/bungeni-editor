

package org.bungeni.editor.metadata;

/**
 * Language description object
 * captures language codes in both iso6339-1 and 6339-2
 * @author Ashok Hariharan
 */
 public class LanguageCode implements Comparable {
        private String languageCodeAlpha2;
        private String languageCodeAlpha3;
        private String languageName;
        
        public LanguageCode(String langAlpha2, String langAlpha3, String langN) {
            languageCodeAlpha2 = langAlpha2;
            languageCodeAlpha3 = langAlpha3;
            languageName = langN;
        }
        
        @Override
        public String toString(){
            return getLanguageName();
        }

  public int compareTo(Object t) {
        LanguageCode lt = (LanguageCode) t;
        return this.getLanguageCodeAlpha3().compareTo(lt.getLanguageCodeAlpha3());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object t){
        LanguageCode lt = (LanguageCode) t;
        if (getLanguageCodeAlpha3().equals(lt.getLanguageCodeAlpha3())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.languageCodeAlpha3 != null ? this.languageCodeAlpha3.hashCode() : 0);
        return hash;
    }

    /**
     * @return the languageName
     */
    public String getLanguageName() {
        return languageName;
    }


    public String getLanguageCodeAlpha2(){
        return this.languageCodeAlpha2;
    }

   public String getLanguageCodeAlpha3(){
        return this.languageCodeAlpha3;
    }



 }