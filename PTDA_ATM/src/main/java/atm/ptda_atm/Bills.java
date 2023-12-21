package atm.ptda_atm;

import java.util.HashMap;

public class Bills {
    private HashMap<String, Object> bills  = new HashMap<String, Object>();

    Bills() {
        bills.put("123456789",new Services("12345",1234.12));
        bills.put("123456789012345",new TheState(1234.12));
    }

    public HashMap<String, Object> getPayment() {
        return bills;
    }
}
