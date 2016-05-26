package com.google.server;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ServerActivity extends Activity {

  private static final int DEFAULT_PORT = 8080;

  private Server server;
  private String host;
  private int port = DEFAULT_PORT;  // TODO(lizlooney): allow port to be changed.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_server);
  }

  @Override
  public void onResume() {
    super.onResume();

    server = new Server(port, this);
    try {
      server.start();
      showP2pInfo();
      showHostAndPort();
    } catch (IOException e) {
      RobotLog.logStacktrace(e);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (server != null) {
      TextView textView = (TextView) findViewById(R.id.host_and_port);
      textView.setText("");
      textView.requestLayout();
      server.stop();
      server = null;
    }
  }

  private void showHostAndPort() {
    final Runnable updateTextView = new Runnable() {
      @Override
      public void run() {
        if (host != null) {
          TextView textView = (TextView) findViewById(R.id.host_and_port);
          textView.setText(host + ":" + port);
          textView.requestLayout();
        }
      }
    };

    Runnable getHost = new Runnable() {
      @Override
      public void run() {
        try {
          host = getLocalHostLANAddress().getHostAddress();
          runOnUiThread(updateTextView);
        } catch (UnknownHostException e) {
          RobotLog.logStacktrace(e);
        }
      }
    };
    new Thread(getHost).start();
  }

  /**
   * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
   * <p/>
   * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
   * that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same
   * way as regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not
   * specify the algorithm used to select the address returned under such circumstances, and will often return the
   * loopback address, which is not valid for network communication. Details
   * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
   * <p/>
   * This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
   * most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer
   * a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
   * first site-local address if the machine has more than one), but if the machine does not hold a site-local
   * address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
   * <p/>
   * If this method cannot find a non-loopback address using this selection algorithm, it will fall back to
   * calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
   * <p/>
   *
   * @throws UnknownHostException If the LAN address of the machine cannot be found.
   */
  private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
    try {
      InetAddress candidateAddress = null;
      // Iterate all NICs (network interface cards)...
      for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
        NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
        // Iterate all IP addresses assigned to each card...
        for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
          InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
          if (!inetAddr.isLoopbackAddress()) {

            if (inetAddr.isSiteLocalAddress()) {
              // Found non-loopback site-local address. Return it immediately...
              return inetAddr;
            }
            else if (candidateAddress == null) {
              // Found non-loopback address, but not necessarily site-local.
              // Store it as a candidate to be returned if site-local address is not subsequently found...
              candidateAddress = inetAddr;
              // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
              // only the first. For subsequent iterations, candidate will be non-null.
            }
          }
        }
      }
      if (candidateAddress != null) {
        // We did not find a site-local address, but we found some other non-loopback address.
        // Server might have a non-site-local address assigned to its NIC (or it might be running
        // IPv6 which deprecates the "site-local" concept).
        // Return this non-loopback candidate address...
        return candidateAddress;
      }
      // At this point, we did not find a non-loopback address.
      // Fall back to returning whatever InetAddress.getLocalHost() returns...
      InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
      if (jdkSuppliedAddress == null) {
        throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
      }
      return jdkSuppliedAddress;
    }
    catch (Exception e) {
      UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
      unknownHostException.initCause(e);
      throw unknownHostException;
    }
  }

  private void showP2pInfo()  {
    // try to get the wifi direct assistant.
    WifiDirectAssistant wda;
    try {
      wda = WifiDirectAssistant.getWifiDirectAssistant(null);
      wda.enable();
    } catch (NullPointerException e) {
      RobotLog.i("Cannot start Wifi Direct Assistant");
      wda = null;
    }

    String groupOwnerName = "";
    String groupPassphrase = "";
    String groupInterface = "";
    String groupNetworkName = "";
    if (wda != null)  {
      groupOwnerName = wda.getGroupOwnerName();
      groupPassphrase = wda.getPassphrase();
      groupInterface = wda.getGroupInterface();
      groupNetworkName = wda.getGroupNetworkName();
    }

    TextView textP2pGroupOwner = (TextView) findViewById(R.id.p2p_group_owner);
    textP2pGroupOwner.setText(groupOwnerName);
    TextView textP2pNetworkName = (TextView) findViewById(R.id.p2p_network_name);
    textP2pNetworkName.setText(groupNetworkName);
    TextView textPassphrase = (TextView) findViewById(R.id.p2p_passphrase);
    textPassphrase.setText(groupPassphrase);
  }
}
