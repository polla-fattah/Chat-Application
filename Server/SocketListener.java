import java.net.*;

class SocketListener
{
	public ServerSocket listener;
	public int connected;
	SocketListener(int c, ServerSocket l)
	{
		listener = l;
		connected = c;
	}
}
