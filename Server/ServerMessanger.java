import java.util.*;

public class ServerMessanger
{
	private Map onlineUserList;
	private DataBaseController dcontroller;
	private ContactController contactController;
	private LoginControler login;
	private Map publicRoom;
	ServerMessanger()
	{
		onlineUserList = new HashMap();
		dcontroller = new DataBaseController();
		publicRoom = new HashMap();

		String roomsName[] = getRooms().split(";");
		for (int i = 0; i < roomsName.length ; i++)
		{
			publicRoom.put(roomsName[i].trim(), new PublicRooms(roomsName[i]));
		}

		contactController = new ContactController(dcontroller, onlineUserList,this, publicRoom);
		login = new LoginControler(dcontroller, contactController, onlineUserList);
		contactController.start();
		login.start();
	}
	public String getRooms()
	{
		return "Schools and Education;Computer;Sport;Science;Familly;Arts;Health;Business;"
				+"Cultures and Community;Music;Religion and Beliefs";
	}
}