import java.util.*;
class PublicRooms
{
	private String roomName;
	private Map users;

	public PublicRooms(String roomN)
	{
		roomName = roomN;
		users = new HashMap();
	}
	public String toString()
	{
		return roomName;
	}
	public void addUser(User user)
	{
		User temp;
		Object key;
		Set keys = users.keySet();
		String userID = user.toString();

		for (Iterator i = keys.iterator() ; i.hasNext();)
		{
			key = i.next();
			temp = (User)users.get(key);
			temp.output("51"+ userID);
		}

		users.put(user.toString(), user);
		user.output("54" + roomName + ":" + creatUserList());
	}//end of addUser(User user)

	private String creatUserList()
	{
		User temp;
		Object key;
		Set keys = users.keySet();
		String list = "";

		for (Iterator i = keys.iterator() ; i.hasNext();)
		{
			key = i.next();
			temp = (User)users.get(key);
			list += temp.toString() + ";";
		}
		return list;
	}

	public void removeUser(User user)
	{
		String userID = user.toString();
		Object ob = users.remove(userID);
		if(ob == null)
			return;

		User temp;
		Object key;
		Set keys = users.keySet();
		for (Iterator i = keys.iterator() ; i.hasNext();)
		{
			key = i.next();
			temp = (User)users.get(key);
			temp.output("52"+ userID);
			System.out.println("dodododod");

		}
	}//end of removeUser(User user)

	public void sendMessage(String message)
	{
		User temp;
		Object key;
		Set keys = users.keySet();
		for (Iterator i = keys.iterator() ; i.hasNext();)
		{
			key = i.next();
			temp = (User)users.get(key);
			temp.output("50"+ message);
		}
	}
}

