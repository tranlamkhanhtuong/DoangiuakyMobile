package uitcourse.j11.nt118.appmusichtcl.Service;

public class APIService {
    private static String base_url = "https://appmusicprojectuit.000webhostapp.com/Server/";

    public  static Dataservice getService()
    {
        return APIRetrofitClient.getClient(base_url).create(Dataservice.class);
    }

}
