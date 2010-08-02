package org.bungeni.trackchanges.rss;

import java.util.ArrayList;
import java.util.List;
import org.bungeni.trackchanges.registrydata.BungeniBill;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniBillDataProvider {

public static List<BungeniBill> getData(){
     return new ArrayList<BungeniBill>() {
        {
            add(new BungeniBill("/ke/bills/en/equal-opp-bill/01", "Equal Opportunities Bill", "848524", "2009-03-12"));
            add(new BungeniBill("/ke/bills/en/finance-bill/01", "Finance Bill", "863524", "2009-01-02"));
        }
    };
}

}
