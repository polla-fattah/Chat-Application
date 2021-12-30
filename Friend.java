
class Friend
{
	private String name;
	private boolean status;

	public Friend(String friend)
	{
		name = new String (friend.substring(1, friend.length()));
		setStatus(friend.charAt(0));
	}
	public String getName()
	{
		return name;
	}
	public boolean getStatus()
	{
		return status;
	}
	public void setStatus(boolean b)
	{
		status = b ;
	}
	public void setStatus(char b)
	{
		status = (b == '1');
	}
	public String toString()
	{
		return name;
	}
}