package atm.ptda_atm;

import java.util.HashMap;

public class Bills {
    private HashMap<String, Object> bills  = new HashMap<String, Object>();

    private void addPayment() {
        bills.put("12345",new Services("1234",1234.12));
        bills.put("12345",new TheState(1234.12));
    }

    public HashMap<String, Object> getPayment() {
        return bills;
    }
}
