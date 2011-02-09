
package org.bungeni.editor.metadata;

/**
 *
 * Encapsulates country codes in a class -- country codes are ISO3166
 *
 * @author Ashok Hariharan
 */
   public class CountryCode implements Comparable{
        private String countryCode;
        private String countryName;
        
        public CountryCode(){
            countryCode = countryName = "";
        }
        public CountryCode(String countryC, String countryN) {
            countryCode = countryC;
            countryName = countryN;
        }

        
        
        @Override
        public String toString(){
            return getCountryName();
        }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @return the countryName
     */
    public String getCountryName() {
        return countryName;
    }

    public String getCountryCodeLower(){
        return countryCode.toLowerCase();
    }

     public int compareTo(Object t) {
        CountryCode lt = (CountryCode) t;
        return this.countryCode.compareTo(lt.getCountryCode());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object t){
        CountryCode lt = (CountryCode) t;
        if (getCountryCode().equals(lt.getCountryCode())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.countryCode != null ? this.countryCode.hashCode() : 0);
        return hash;
    }
        
    }
