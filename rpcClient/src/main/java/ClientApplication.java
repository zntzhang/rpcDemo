import Proxy.ProxyFactory;
import entity.User;
import remote.IUserRemote;

public class ClientApplication {
    public static void main(String[] args) throws Exception {
        IUserRemote userRemote = ProxyFactory.create(IUserRemote.class);
        User user = userRemote.getUserById("1");
        System.out.println(user);
    }
}
