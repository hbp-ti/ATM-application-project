package PTDA_ATM;

import java.util.HashMap;

public class Bills {
    private HashMap<String, Object> bills  = new HashMap<String, Object>();

    Bills() {
        bills.put("123456789", new Services("12345", 123.12));
        bills.put("123456789012345", new TheState(124.12));

        bills.put("135792468", new Services("98765", 13.56));
        bills.put("246801357924680", new TheState(678.91));

        bills.put("159263123", new Services("79354", 97.65));
        bills.put("268374951682739", new TheState(542.10));

        bills.put("147258369", new Services("75395", 653.21));
        bills.put("369258147369258", new TheState(865.43));

        bills.put("123456645", new Services("65432", 13.45));
        bills.put("987654321987654", new TheState(679.01));

        bills.put("111222333", new Services("44455", 77.88));
        bills.put("777888999777888", new TheState(99.00));

        bills.put("123451234", new Services("13579", 976.54));
        bills.put("246801357911213", new TheState(36.78));

        bills.put("987654321", new Services("79156", 88.88));
        bills.put("543210987654321", new TheState(777.77));
    }

    public HashMap<String, Object> getPayment() {
        return bills;
    }
}
