package eu.obrok.easywake;


import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.List;

class AsyncTCPReceiver extends AsyncTask<Void, Void, Void> implements TCPReceiver {

    private static final int SERVERPORT = 55056;
    private DatagramSocket socket;

    private List<TCPListener> listeners = new LinkedList<>();

    @Override
    protected Void doInBackground(Void... params) {
        try {
            socket = new DatagramSocket(SERVERPORT);
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            System.out.println("Listening on port " + SERVERPORT);
            socket.receive(packet);

            System.out.println("Received packet from " + packet.getAddress());
            for (TCPListener listener : listeners)
                listener.packetReceived();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
        return null;
    }

    @Override
    public void addListener(TCPListener listener) {
        listeners.add(listener);
    }
}
