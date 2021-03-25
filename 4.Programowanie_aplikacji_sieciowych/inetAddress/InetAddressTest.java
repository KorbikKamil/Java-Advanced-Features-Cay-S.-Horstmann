package inetAddress;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Program demonstrujący zastosowanie klasy InetAddress. W wierszu poleceń
 * przekazujemy mu jako parametr nazwę hosta; w razie braku adresu zostanie wyświetlony
 * adres lokalnego hosta.
 *
 * @author Cay Horstmann
 * @version 1.02 2012-06-05
 */
public class InetAddressTest {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String host = args[0];
            InetAddress[] addresses = InetAddress.getAllByName(host);
            for (InetAddress a : addresses)
                System.out.println(a);
        } else {
            InetAddress localHostAddress = InetAddress.getLocalHost();
            System.out.println(localHostAddress);
        }
    }
}
