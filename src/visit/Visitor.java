package visit;

import app.user.Artist;
import app.user.User;
import app.user.Host;

public interface Visitor {
    /**
     * viziteaza un user
     * @param user
     * @return
     */
    public String visit(User user);

    /**
     * viziteaza un artist
     * @param artist
     * @return
     */
    public String visit(Artist artist);

    /**
     * viziteaza un host
     * @param host
     * @return
     */
    public String visit(Host host);
}
