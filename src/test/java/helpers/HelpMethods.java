package helpers;

public class HelpMethods {

    public static int getIdOfFirstUserOnPage(int pageNumber) {

        int per_page = 6;
        return (pageNumber - 1) * per_page + 1;
    }


}
